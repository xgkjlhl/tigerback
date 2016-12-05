/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package mng.core.service;

import tiger.common.dal.dto.mng.StatisticsCountByDateDTO;
import tiger.common.dal.enums.StatisticsUnitEnum;

import java.util.Date;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 20:33 alfred_yuan Exp $
 */
public interface StatisticsService {

    /**
     * 获取从from到to时间段内用户增长数量
     *
     * @param from
     * @param to
     * @return
     */
    int calAccountGrowthRate(Date from, Date to);

    /**
     * 获取从from到to时间段内合同增长数量
     *
     * @param from
     * @param to
     * @return
     */
    int calLoanGrowthRate(Date from, Date to);

    /**
     * 对date前的客户总数进行计数
     *
     * @param date
     * @return
     */
    int sumAccountBeforeDate(Date date);

    /**
     * 获取从from到to中account按unit分段的增长数量
     *
     * @param from
     * @param to
     * @param unit
     * @return
     */
    List<StatisticsCountByDateDTO> accountGrowthRangeStatistics(Date from, Date to, StatisticsUnitEnum unit);

    /**
     * 对date前的合同总数进行计数
     *
     * @param date
     * @return
     */
    int sumLoanBeforeDate(Date date);

    /**
     * 获取从from到to中loan按unit分段的增长数量
     *
     * @param from
     * @param to
     * @param unit
     * @return
     */
    List<StatisticsCountByDateDTO> loanGrowthRangeStatistics(Date from, Date to, StatisticsUnitEnum unit);

}
