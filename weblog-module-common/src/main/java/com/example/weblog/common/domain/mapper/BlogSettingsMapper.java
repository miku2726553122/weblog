package com.example.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.weblog.common.domain.dos.BlogSettingsDO;

public interface BlogSettingsMapper extends BaseMapper<BlogSettingsDO> {

    default String getAvatar(int id) {
        return selectOne(new LambdaQueryWrapper<BlogSettingsDO>().eq(BlogSettingsDO::getId, id)).getAvatar();
    }
}

