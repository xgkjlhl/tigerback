/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.util;

import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRecordItemDomain;
import tiger.core.domain.extraDomain.ExtraField;

/**
 * 贷款对象封装类
 * ~ 用于抽象一些贷款计算对象的封装方法
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 28, 2015 1:56:10 PM yiliang.gyl Exp $
 */
public class LoanAssembleUtil {

    /**
     * 结清尾款的时候快速生成本息对象
     *
     * @param principalMoney the principal money
     * @param interestMoney  the interest money
     *
     * @return the loan record domain
     */
    public static LoanRecordDomain instanceRecordOfEndCase(double principalMoney,
                                                           double interestMoney) {
        LoanRecordDomain loanRecordDomain = new LoanRecordDomain();
        loanRecordDomain.addItem(LoanRecordItemDomain.instance(
            principalMoney,
            LoanPayItemEnum.PRINCIPAL_MONEY,
            LoanPayItemModeEnum.INCOME
        ));
        loanRecordDomain.addItem(LoanRecordItemDomain.instance(
            interestMoney,
            LoanPayItemEnum.INTEREST,
            LoanPayItemModeEnum.INCOME
        ));
        return loanRecordDomain;
    }

    public static LoanRecordDomain instanceRecordOfProcessCase(double principalMoney,
                                                               double interestMoney,
                                                               ExtraField scheduleCost) {
        LoanRecordDomain loanRecordDomain = instanceRecordOfEndCase(principalMoney, interestMoney);

        if (scheduleCost != null) {
            loanRecordDomain.addItem(LoanRecordItemDomain.instance(
                scheduleCost.getValue(),
                LoanPayItemEnum.COST_SCHEDULE,
                LoanPayItemModeEnum.INCOME,
                scheduleCost
            ));
        }


        return loanRecordDomain;
    }

    /**
     * 根据贷款的详情计算 Amount
     *
     * @param recordDomain 必须有amount字段的数值，作为计算基础本金
     */
    public static void calRecordAmount(LoanRecordDomain recordDomain) {
        double amount = 0.00;
        for (LoanRecordItemDomain item : recordDomain.getRecordItemDomains()) {
            if (item.getItemModel() == LoanPayItemModeEnum.INCOME) {
                amount = amount + item.getAmount();
            } else if (item.getItemModel() == LoanPayItemModeEnum.OUTCOME) {
                amount = amount - item.getAmount();
            }
        }
        //计算出最后的值
        recordDomain.setAmount(amount);
    }


}
