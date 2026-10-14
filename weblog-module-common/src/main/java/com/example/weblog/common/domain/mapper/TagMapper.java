package com.example.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.weblog.common.domain.dos.CategoryDO;
import com.example.weblog.common.domain.dos.TagDO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface TagMapper extends BaseMapper<TagDO> {
    /**
     * 分页查询
     * @param current
     * @param size
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    default Page<TagDO> selectPageList(long current, long size, String name, LocalDate startTime, LocalDate endTime) {
        //分页对象
        Page<TagDO> page=new Page<>(current, size);
        //构建查询条件
        LambdaQueryWrapper<TagDO> wrapper=new LambdaQueryWrapper<>();
        wrapper
            .like(Objects.nonNull(name),TagDO::getName, name)
            .ge(Objects.nonNull(startTime),TagDO::getCreateTime, startTime)
            .le(Objects.nonNull(endTime),TagDO::getCreateTime, endTime)
            .orderByDesc(TagDO::getCreateTime);

        return selectPage(page, wrapper);
    }

    /**
     * 根据标签模糊查询
      * @param key
     * @return
     */
    default List<TagDO> selectByKey(String key){
        LambdaQueryWrapper<TagDO> wrapper=new LambdaQueryWrapper<>();
        //构建查询条件
        wrapper.like(TagDO::getName, key)
                .orderByDesc(TagDO::getCreateTime);

        return selectList(wrapper);
    }

    /**
     * 根据标签 ID 批量查询
     * @param tagIds
     * @return
     */
    default List<TagDO> selectByIds(List<Long> tagIds) {
        return selectList(Wrappers.<TagDO>lambdaQuery()
                .in(TagDO::getId, tagIds));
    }
}

