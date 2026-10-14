package com.example.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.weblog.common.domain.dos.StatisticsArticlePVDO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsArticlePVMapper extends BaseMapper<StatisticsArticlePVDO> {
    /**
     * 对指定日期的文章 PV 访问量进行 +1
     * @param date
     * @return
     */
    default int increasePVCount(LocalDate date) {
        // 先尝试更新
        int updated = update(null, Wrappers.<StatisticsArticlePVDO>lambdaUpdate()
                .setSql("pv_count = pv_count + 1")
                .eq(StatisticsArticlePVDO::getPvDate, date));

        // 如果更新失败（记录不存在），则插入新记录
        if (updated == 0) {
            StatisticsArticlePVDO newRecord = StatisticsArticlePVDO.builder()
                    .pvDate(date)
                    .pvCount(1L)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            return insert(newRecord);
        }
        return updated;
    }

    /**
     * 查询最近一周的文章 PV 访问量记录
     * @return
     */
    default List<StatisticsArticlePVDO> selectLatestWeekRecords() {
        return selectList(Wrappers.<StatisticsArticlePVDO>lambdaQuery()
                .le(StatisticsArticlePVDO::getPvDate, LocalDate.now().plusDays(1)) // 小于明天
                .orderByDesc(StatisticsArticlePVDO::getPvDate)
                .last("limit 7")); // 仅查询七条
    }
}
