package com.example.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.weblog.common.domain.dos.BlogSettingsDO;
import com.example.weblog.common.domain.dos.CommentDO;
import com.example.weblog.common.domain.mapper.BlogSettingsMapper;
import com.example.weblog.common.domain.mapper.CommentMapper;
import com.example.weblog.common.enums.CommentStatusEnum;
import com.example.weblog.common.enums.ResponseCodeEnum;
import com.example.weblog.common.exception.BizException;
import com.example.weblog.common.utils.Response;
import com.example.weblog.web.convert.CommentConvert;
import com.example.weblog.web.event.PublishCommentEvent;
import com.example.weblog.web.model.vo.comment.*;
import com.example.weblog.web.service.CommentService;
import com.example.weblog.web.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import toolgood.words.IllegalWordsSearch;
import toolgood.words.IllegalWordsSearchResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BlogSettingsMapper blogSettingsMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private IllegalWordsSearch wordsSearch;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public Response findQQUserInfo(FindQQUserInfoReqVO findQQUserInfoReqVO) {
        String qq = findQQUserInfoReqVO.getQq();

        //校验qq号
        if (!StringUtil.isPureNumber(qq)) {
            log.warn("昵称输入的格式不是QQ号：{}", qq);
            throw new BizException(ResponseCodeEnum.NOT_QQ_NUMBER);
        }

        // 头像地址（腾讯官方 QQ 头像 CDN）
        String avatar = String.format("http://q1.qlogo.cn/g?b=qq&nk=%s&s=640", qq);
        // 邮箱
        String mail = qq.trim() + "@qq.com";

        // 请求腾讯官方接口获取 QQ 昵称
        String nickname = qq; // 默认使用 QQ 号作为昵称
        try {
            String url = String.format("https://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=%s", qq);
            // 该接口返回 GBK 编码的 JSONP，需要以字节数组接收后手动转码
            byte[] bytes = restTemplate.getForObject(url, byte[].class);
            String result = new String(bytes, StandardCharsets.UTF_8);
            log.info("==> 请求腾讯官方接口获取QQ用户信息，结果: {}", result);

            // 解析 JSONP 响应：portraitCallBack({"qq号":[...,"昵称",...]})
            int jsonStart = result.indexOf('(');
            int jsonEnd = result.lastIndexOf(')');
            if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                String jsonStr = result.substring(jsonStart + 1, jsonEnd);
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> root = objectMapper.readValue(jsonStr, Map.class);
                List<Object> qqData = (List<Object>) root.get(qq);
                if (qqData != null && qqData.size() > 6) {
                    nickname = String.valueOf(qqData.get(6));
                }
            }
        } catch (Exception e) {
            log.warn("获取 QQ 用户信息失败，将使用 QQ 号作为昵称: {}", e.getMessage());
        }

        return Response.success(FindQQUserInfoRspVO.builder()
                .avatar(avatar)
                .nickname(nickname)
                .mail(mail)
                .build());
    }

    /**
     * 发布评论
     *
     * @param publishCommentReqVO
     * @return
     */
    @Override
    @Transactional
    public Response publishComment(PublishCommentReqVO publishCommentReqVO) {
        // 回复的评论 ID
        Long replyCommentId = publishCommentReqVO.getReplyCommentId();
        // 评论内容
        String content = publishCommentReqVO.getContent();
        // 昵称
        String nickname = publishCommentReqVO.getNickname();

        // 查询博客设置相关信息（约定的 ID 为 1）
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectById(1L);
        // 是否开启了敏感词过滤
        boolean isCommentSensiWordOpen = blogSettingsDO.getIsCommentSensiWordOpen();
        // 是否开启了审核
        boolean isCommentExamineOpen = blogSettingsDO.getIsCommentExamineOpen();

        // 设置默认状态（正常）
        Integer status = CommentStatusEnum.NORMAL.getCode();
        // 审核不通过原因
        String reason = "";

        // 如果开启了审核, 设置状态为待审核，等待博主后台审核通过
        if (isCommentExamineOpen) {
            status = CommentStatusEnum.WAIT_EXAMINE.getCode();
        }

        // 评论内容是否包含敏感词
        boolean isContainSensitiveWord = false;
        // 是否开启了敏感词过滤
        if (isCommentSensiWordOpen) {
            // 校验评论中是否包含敏感词
            isContainSensitiveWord = wordsSearch.ContainsAny(content);

            if (isContainSensitiveWord) {
                // 若包含敏感词，设置状态为审核不通过
                status = CommentStatusEnum.EXAMINE_FAILED.getCode();
                // 匹配到的所有敏感词组
                List<IllegalWordsSearchResult> results = wordsSearch.FindAll(content);
                List<String> keywords = results.stream().map(result -> result.Keyword).collect(Collectors.toList());
                // 不同过的原因
                reason = String.format("系统自动拦截，包含敏感词：%s", keywords);
                log.warn("此评论内容中包含敏感词: {}, content: {}", keywords, content);
            }
        }

        // 构建 DO 对象
        CommentDO commentDO = CommentDO.builder()
                .avatar(publishCommentReqVO.getAvatar())
                .content(content)
                .mail(publishCommentReqVO.getMail())
                .createTime(LocalDateTime.now())
                .nickname(nickname)
                .routerUrl(publishCommentReqVO.getRouterUrl())
                .website(publishCommentReqVO.getWebsite())
                .replyCommentId(replyCommentId)
                .parentCommentId(publishCommentReqVO.getParentCommentId())
                .status(status)
                .reason(reason)
                .build();

        // 新增评论
        commentMapper.insert(commentDO);

        Long commentId = commentDO.getId();

        // 发送评论发布事件
        eventPublisher.publishEvent(new PublishCommentEvent(this, commentId));

        // 根据评论状态返回对应提示信息（不抛异常，否则事务会回滚，评论无法存入数据库）
        if (isContainSensitiveWord)
            return Response.fail(ResponseCodeEnum.COMMENT_CONTAIN_SENSITIVE_WORD);

        if (Objects.equals(status, CommentStatusEnum.WAIT_EXAMINE.getCode()))
            return Response.fail(ResponseCodeEnum.COMMENT_WAIT_EXAMINE);

        return Response.success();
    }

    /**
     * 查询页面所有评论
     *
     * @param findCommentListReqVO
     * @return
     */
    @Override
    public Response findCommentList(FindCommentListReqVO findCommentListReqVO) {
        // 路由地址
        String routerUrl = findCommentListReqVO.getRouterUrl();

        // 查询该路由地址下所有评论（仅查询状态正常的）
        List<CommentDO> commentDOS = commentMapper.selectByRouterUrlAndStatus(routerUrl, CommentStatusEnum.NORMAL.getCode());
        // 总评论数
        Integer total = commentDOS.size();

        List<FindCommentItemRspVO> vos = null;
        // DO 转 VO
        if (!CollectionUtils.isEmpty(commentDOS)) {
            // 一级评论
            vos = commentDOS.stream()
                    .filter(commentDO -> Objects.isNull(commentDO.getParentCommentId())) // parentCommentId 父级 ID 为空，则表示为一级评论
                    .map(commentDO -> CommentConvert.INSTANCE.convertDO2VO(commentDO))
                    .collect(Collectors.toList());

            // 循环设置评论回复数据
            vos.forEach(vo -> {
                Long commentId = vo.getId();
                List<FindCommentItemRspVO> childComments = commentDOS.stream()
                        .filter(commentDO -> Objects.equals(commentDO.getParentCommentId(), commentId)) // 过滤出一级评论下所有子评论
                        .sorted(Comparator.comparing(CommentDO::getCreateTime)) // 按发布时间升序排列
                        .map(commentDO -> {
                            FindCommentItemRspVO findPageCommentRspVO = CommentConvert.INSTANCE.convertDO2VO(commentDO);
                            Long replyCommentId = commentDO.getReplyCommentId();
                            // 若二级评论的 replayCommentId 不等于一级评论 ID, 前端则需要展示【回复 @ xxx】，需要设置回复昵称
                            if (!Objects.equals(replyCommentId, commentId)) {
                                // 设置回复用户的昵称
                                Optional<CommentDO> optionalCommentDO = commentDOS.stream()
                                        .filter(commentDO1 -> Objects.equals(commentDO1.getId(), replyCommentId)).findFirst();
                                if (optionalCommentDO.isPresent()) {
                                    findPageCommentRspVO.setReplyNickname(optionalCommentDO.get().getNickname());
                                }
                            }
                            return findPageCommentRspVO;
                        }).collect(Collectors.toList());

                vo.setChildComments(childComments);
            });
        }

        return Response.success(FindCommentListRspVO.builder()
                .total(total)
                .comments(vos)
                .build());
    }
}
