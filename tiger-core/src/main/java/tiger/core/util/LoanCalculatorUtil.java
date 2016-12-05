/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.util;

import org.apache.commons.collections.CollectionUtils;
import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRecordItemDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * 贷款计算工具类
 * ~ 防止贷款方面的计算逻辑
 * ~ 注意: 计算逻辑而否业务逻辑
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 28, 2015 11:36:00 AM yiliang.gyl Exp $
 */
public class LoanCalculatorUtil {

    /**
     * 逾期违约金计算器
     *
     * @param amount      本金
     * @param rate        逾期违约金率
     * @param overdueDays 逾期天数
     * @return
     */
    public static double calOverdueMoney(double amount, double rate, int overdueDays) {
        double money = rate * amount * overdueDays;
        if (money < 0) {
            return 0.00;
        } else {
            return money;
        }
    }


    /**
     * 利息计算器
     *
     * @param 贷款金额
     * @param 月利率/百分之
     * @param 贷款月份
     * @return 贷款利息
     */
    public static double calInterest(double principal, double inRate, int month) {
        return principal * inRate * month;
    }

    /**
     * 贷款期间数
     *
     * @param current the current
     * @param total   the total
     * @return the string
     */
    public static String calProcess(int current, int total) {
        return "(" + current + "/" + total + ")";
    }


    /**
     * 合并账单计算器
     *
     * @param bills
     * @return
     */
    public static Double mergeBills(List<LoanRecordDomain> bills) {
        if (CollectionUtils.isEmpty(bills)) {
            return 0.00;
        }
        Double amount = 0.00;
        for (LoanRecordDomain bill : bills) {
            amount += bill.getAmount();
        }
        return amount;
    }

    /**
     * 统计相关类型详情的值
     *
     * @param items
     * @param types
     * @return
     */
    public static Double sumRecordItems(List<LoanRecordItemDomain> items, List<LoanPayItemEnum> types) {
        Double amount = 0.00;
        for (LoanRecordItemDomain item : items) {
            if (types.contains(item.getItemType())) {
                if (item.getItemModel() == LoanPayItemModeEnum.INCOME) {
                    amount += item.getAmount();
                } else if (item.getItemModel() == LoanPayItemModeEnum.OUTCOME) {
                    amount -= item.getAmount();
                }
            }
        }
        return amount;
    }

    public static Double sumRecordItems(List<LoanRecordItemDomain> items, LoanPayItemEnum type) {
        List<LoanPayItemEnum> list = new ArrayList<>();
        list.add(type);
        return sumRecordItems(items, list);
    }

}
