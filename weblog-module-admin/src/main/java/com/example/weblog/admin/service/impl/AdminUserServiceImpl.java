package com.example.weblog.admin.service.impl;

import com.example.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.example.weblog.admin.model.vo.user.FindUserInfoRspVO;
import com.example.weblog.admin.service.AdminUserService;
import com.example.weblog.common.domain.mapper.BlogSettingsMapper;
import com.example.weblog.common.domain.mapper.UserMapper;
import com.example.weblog.common.enums.ResponseCodeEnum;
import com.example.weblog.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogSettingsMapper blogSettingsMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        String username = updateAdminUserPasswordReqVO.getUsername();
        String password = updateAdminUserPasswordReqVO.getPassword();
        //加密密码
        String encodePassword = passwordEncoder.encode(password);
        //更新密码
        int count = userMapper.updatePasswordByUsername(username, encodePassword);

        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.USERNAME_NOT_FOUND);

    }


    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @Override
    public Response findUserInfo() {
        // 获取存储在 ThreadLocal 中的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //拿到用户名
        String username = authentication.getName();
        String avatar = blogSettingsMapper.getAvatar(1);
        return Response.success(FindUserInfoRspVO
                .builder()
                .username(username)
                .avatar(avatar)
                .build());

    }
}
