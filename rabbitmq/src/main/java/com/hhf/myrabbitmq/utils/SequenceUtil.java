package com.hhf.myrabbitmq.utils;

import com.hhf.common.idworker.snowflake.SequenceService;
import com.hhf.common.utils.SpringContextHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 生产流水号的工具类
 *
 * @author _g5niusx
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SequenceUtil {

    /**
     * 获取流水号
     *
     * @return 流水号
     */
    public static String getSeqNo() {
        SequenceService sequenceService = SpringContextHolder.getBean(SequenceService.class);
        return String.valueOf(sequenceService.nextId());
    }
}
