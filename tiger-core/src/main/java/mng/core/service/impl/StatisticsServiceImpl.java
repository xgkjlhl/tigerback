/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package mng.core.service.impl;

import mng.core.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.example.AccountExample;
import tiger.common.dal.dataobject.example.LoanExample;
import tiger.common.dal.dto.mng.StatisticsCountByDateDTO;
import tiger.common.dal.enums.StatisticsUnitEnum;
import tiger.common.dal.persistence.AccountMapper;
import tiger.common.dal.persistence.LoanMapper;
import tiger.common.dal.persistence.StatisticsMapper;
import tiger.common.util.DateUtil;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 20:37 alfred_yuan Exp $
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private StatisticsMapper statisticsMapper;

    /**
     * @see StatisticsService#calAccountGrowthRate(Date, Date)
     */
    @Override
    public int calAccountGrowthRate(Date from, Date to) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria()
                .andCreateTimeBetween(from, to);

        return accountMapper.countByExample(accountExample);
    }

    /**
     * @see StatisticsService#calLoanGrowthRate(Date, Date)
     */
    @Override
    public int calLoanGrowthRate(Date from, Date to) {
        LoanExample loanExample = new LoanExample();
        loanExample.createCriteria()
                .andIsDeleteEqualTo(false)
                .andCreateTimeBetween(from, to);

        return loanMapper.countByExample(loanExample);
    }

    /**
     * @see StatisticsService#sumAccountBeforeDate(Date)
     */
    public int sumAccountBeforeDate(Date date) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria()
                .andCreateTimeLessThan(date);

        return accountMapper.countByExample(accountExample);
    }

    /**
     * @see StatisticsService#accountGrowthRangeStatistics(Date, Date, StatisticsUnitEnum)
     */
    @Override
    public List<StatisticsCountByDateDTO> accountGrowthRangeStatistics(Date from, Date to, StatisticsUnitEnum unit) {
        checkParameters(from, to, unit);

        return encapsulateData(from, to, unit,
                statisticsMapper.countAccountByDayInRange(unit.getBegin(from), DateUtil.getDayEnd(to)));
    }

    /**
     * @see StatisticsService#sumLoanBeforeDate(Date)
     */
    @Override
    public int sumLoanBeforeDate(Date date) {
        LoanExample example = new LoanExample();
        example.createCriteria()
                .andCreateTimeLessThan(date)
                .andIsDeleteEqualTo(false);

        return loanMapper.countByExample(example);
    }

    /**
     * @see StatisticsService#loanGrowthRangeStatistics(Date, Date, StatisticsUnitEnum)
     */
    @Override
    public List<StatisticsCountByDateDTO> loanGrowthRangeStatistics(Date from, Date to, StatisticsUnitEnum unit) {
        checkParameters(from, to, unit);

        return encapsulateData(from, to, unit,
                statisticsMapper.countLoanByDayInRange(unit.getBegin(from), DateUtil.getDayEnd(to)));
    }

    /**
     * 检查参数是否合法
     *
     * @param from
     * @param to
     * @param unit
     */
    private void checkParameters(Date from, Date to, StatisticsUnitEnum unit) {
        if (from == null || to == null || unit == null) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER);
        }
    }

    /**
     * 将data填充到List当中, 根据unit和对应date
     *
     * @param from
     * @param to
     * @param unit
     * @param data
     * @return
     */
    private List<StatisticsCountByDateDTO> encapsulateData(Date from,
                                                           Date to,
                                                           StatisticsUnitEnum unit,
                                                           List<StatisticsCountByDateDTO> data) {
        // 计算需要返回的list大小
        Date realFrom = unit.getBegin(from);
        int size = unit.getDiff(realFrom, unit.getNextDate(to));
        List<StatisticsCountByDateDTO> countResult = Arrays.asList(new StatisticsCountByDateDTO[size]);

        StatisticsCountByDateDTO tempDTO;
        // 对已有的数据进行处理
        for (StatisticsCountByDateDTO dateResult : data) {
            int offset = unit.getDiff(realFrom, dateResult.getDate());
            if ((tempDTO = countResult.get(offset)) != null) {
                tempDTO.setCount(tempDTO.getCount() + dateResult.getCount());
            } else {
                countResult.set(offset, dateResult);
            }
        }

        // 填充其他数据
        Date tempDate = realFrom;
        for (int i = 0; i < size; i++) {
            if ((tempDTO = countResult.get(i)) == null) {
                tempDTO = new StatisticsCountByDateDTO();
                countResult.set(i, tempDTO);
            }
            // 规范时间
            tempDTO.setDate(tempDate);
            tempDate = unit.getNextDate(tempDate);
        }

        return countResult;
    }
}
