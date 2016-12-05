/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.loan.calculator;

import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.util.DateUtil;
import tiger.common.util.loan.LoanDateUtil;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRecordItemDomain;
import tiger.core.domain.extraDomain.ExtraField;
import tiger.core.util.LoanAssembleUtil;
import tiger.core.util.LoanCalculatorUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 贷款放款/还款计算类
 * ~ 支持多种计算方式 TemplateMehtod
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 27, 2015 10:34:41 AM yiliang.gyl Exp $
 */
public abstract class AbstractLoanPayCalculator {

    /**
     * 获取需要生成的所有方法
     *
     * @param loanDomain the loan domain
     *
     * @return the list
     */
    public List<LoanRecordDomain> run(LoanDomain loanDomain) {
        List<LoanRecordDomain> loanRecordDomains = new ArrayList<>();
        //1 计算放款的放款的资金
        LoanRecordDomain initDomain = calInitPay(loanDomain);
        if (initDomain != null) {
            loanRecordDomains.add(initDomain);
        }

        //2. 计算期间还款资金流向
        List<LoanRecordDomain> middleItems = this.calProcessPaybacks(loanDomain);
        if (middleItems != null && middleItems.size() > 0) {
            for (LoanRecordDomain domain : middleItems) {
                domain.setOrder(loanRecordDomains.size() + 1);
                loanRecordDomains.add(domain);
            }
        }
        //3. 计算尾款收取资金流向
        LoanRecordDomain endItem = this.calLastPay(loanDomain);
        if (endItem != null) {
            endItem.setOrder(loanRecordDomains.size() + 1);
            loanRecordDomains.add(endItem);
        }
        return loanRecordDomains;
    }

    /**
     * 放款流程计算
     *
     * @return the loan record domain
     */
    public LoanRecordDomain calInitPay(LoanDomain loanDomain) {
        LoanRecordDomain loanRecordDomain = new LoanRecordDomain();
        /** 放款日期 */
        loanRecordDomain.setDeadLine(loanDomain.getStartDate()); //首次还款日期
        loanRecordDomain.setTheoryDeadLine(loanDomain.getStartDate());
        loanRecordDomain.setTheoryInterest(0.00);
        //1. 计算放款金额
        setAmount(loanDomain, loanRecordDomain);
        loanRecordDomain.setType(LoanActionEnum.LOAN_LAUNCH);
        loanRecordDomain.setState(LoanStatusEnum.WAIT_LAUNCH);
        loanRecordDomain.setOrder(1);//第一个排序
        loanRecordDomain.setLoanPayItemModeEnum(LoanPayItemModeEnum.OUTCOME);
        return loanRecordDomain;
    }


    public LoanRecordDomain calLastPay(LoanDomain loanDomain) {
        LoanRecordDomain endItem = this.calEndPay(loanDomain);
        if (endItem != null) {
            endItem.setType(LoanActionEnum.LOAN_END_BACK);
            endItem.setState(LoanStatusEnum.PAY_PROCESS);
            endItem.setLoanPayItemModeEnum(LoanPayItemModeEnum.INCOME);
            //计算尾款还款日期
            endItem.setDeadLine(calEndBackDeadLine(loanDomain));

            endItem.setLoanId(loanDomain.getId());
            rebackMoney(loanDomain, endItem);
            LoanAssembleUtil.calRecordAmount(endItem);
            endItem.setTheoryInterest(0.00);
            endItem.setTheoryAmount(endItem.getAmount());
            endItem.setTheoryDeadLine(endItem.getDeadLine());

        }
        return endItem;
    }


    public LoanRecordDomain recalItem(LoanDomain loanDomain, LoanRecordDomain loanRecordDomain) {
        //1. 计算逾期违约金
        Date payDate = loanRecordDomain.getActualDate();
        Long day = DateUtil.getDiffDays(payDate, loanRecordDomain.getDeadLine());
        if (day > 0) {
            //添加逾期违约金
            double overdueMoney = LoanCalculatorUtil.calOverdueMoney(loanDomain.getAmount(), loanDomain.getPenaltyRate(), day.intValue());
            LoanRecordItemDomain item = new LoanRecordItemDomain();
            item.setItemType(LoanPayItemEnum.PENALTY_MONEY);
            item.setAmount(overdueMoney);
            item.setLoanRecordId(loanRecordDomain.getId());
            item.setItemModel(LoanPayItemModeEnum.INCOME);
            item.setOrder(0);
            loanRecordDomain.addItem(item);
        }
        //2. 计算是否有补充条款
        this.callItems(loanDomain, loanRecordDomain);
        LoanAssembleUtil.calRecordAmount(loanRecordDomain);
        return loanRecordDomain;
    }

    /**
     * 放款补充流程
     * ~ 某些放款有未完成的流程需要补充
     *
     * @param loanDomain the loan domain
     * @param initDomain the init domain
     */
    public abstract void initAdditionProcess(LoanDomain loanDomain, LoanRecordDomain initDomain);

    /**
     * 期间收款计算
     */
    public abstract List<LoanRecordDomain> calProcessPaybacks(LoanDomain loanDomain);

    /**
     * 尾款计算
     */
    protected abstract LoanRecordDomain calEndPay(LoanDomain loanDomain);

    /**
     * 实际还款时各项 Item 计算
     */
    protected abstract List<LoanRecordItemDomain> callItems(LoanDomain loanDomain, LoanRecordDomain loanRecordDomain);


    // ~ private method

    /**
     * 计算放款金额
     *
     * @param loanDomain       the loan domain
     * @param loanRecordDomain the loan record domain
     */
    private void setAmount(LoanDomain loanDomain, LoanRecordDomain loanRecordDomain) {
        //1. 构建放款 
        loanRecordDomain.addItem(LoanRecordItemDomain.instance(loanDomain.getAmount(),
            LoanPayItemEnum.PRINCIPAL_MONEY, LoanPayItemModeEnum.OUTCOME));
        if (loanDomain.getBusinessType() != BusinessTypeEnum.BORROW) {
            loanRecordDomain.addItem(LoanRecordItemDomain.instance(
                loanDomain.getBondPerform(),
                LoanPayItemEnum.BOND_PERFORM_MONEY,
                LoanPayItemModeEnum.INCOME
            ));
            loanRecordDomain.addItem(LoanRecordItemDomain.instance(
                loanDomain.getCostService(),
                LoanPayItemEnum.COST_SERVICE,
                LoanPayItemModeEnum.INCOME
            ));
            loanRecordDomain.addItem(LoanRecordItemDomain.instance(
                loanDomain.getCostTemp(),
                LoanPayItemEnum.COST_TEMP,
                LoanPayItemModeEnum.INCOME
            ));
            loanRecordDomain.addItem(LoanRecordItemDomain.instance(
                loanDomain.getCostOther(),
                LoanPayItemEnum.COST_OTHER,
                LoanPayItemModeEnum.INCOME
            ));

            // 其他费用1
            ExtraField costOtherFirst = loanDomain.getCostOtherFirst();
            if (costOtherFirst != null) {
                loanRecordDomain.addItem(LoanRecordItemDomain.instance(
                    costOtherFirst.getValue(),
                    LoanPayItemEnum.COST_CUSTOMIZE_OTHER_FIRST,
                    LoanPayItemModeEnum.INCOME,
                    costOtherFirst
                ));
            }

            // 其他费用2
            ExtraField costOtherSecond = loanDomain.getCostOtherSecond();
            if (costOtherSecond != null) {
                loanRecordDomain.addItem(LoanRecordItemDomain.instance(
                    costOtherSecond.getValue(),
                    LoanPayItemEnum.COST_CUSTOMIZE_OTHER_SECOND,
                    LoanPayItemModeEnum.INCOME,
                    costOtherSecond
                ));
            }
        }
        //调用放款补充流程
        initAdditionProcess(loanDomain, loanRecordDomain);
        //计算最后金额
        LoanAssembleUtil.calRecordAmount(loanRecordDomain);

        //设置理论参数
        loanRecordDomain.setTheoryAmount(loanRecordDomain.getAmount());
        loanRecordDomain.setTheoryDeadLine(loanRecordDomain.getDeadLine());
    }

    /**
     * 退款流程
     *
     * @param loanDomain       the loan domain
     * @param loanRecordDomain the loan record domain
     */
    private void rebackMoney(LoanDomain loanDomain, LoanRecordDomain loanRecordDomain) {
        if (loanDomain.getBusinessType() != BusinessTypeEnum.BORROW) {
            loanRecordDomain.addItem(LoanRecordItemDomain.instance(loanDomain.getBondPerform(),
                LoanPayItemEnum.BOND_PERFORM_MONEY, LoanPayItemModeEnum.OUTCOME));
            loanRecordDomain.addItem(LoanRecordItemDomain.instance(loanDomain.getCostTemp(),
                LoanPayItemEnum.COST_TEMP, LoanPayItemModeEnum.OUTCOME));
        }
    }

    /**
     * 计算尾款还款日期
     *
     * @param loanDomain
     *
     * @return
     */
    protected Date calEndBackDeadLine(LoanDomain loanDomain) {
        return DateUtil.addDays(
            LoanDateUtil.getLoanEndDate(
                loanDomain.getStartDate(), loanDomain.getPayCircle() * loanDomain.getPayTotalCircle()
            ),
            -1
        );
    }
}
