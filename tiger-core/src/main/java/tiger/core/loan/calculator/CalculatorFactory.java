/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.loan.calculator;

import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.List;

/**
 * 计算还款放款的小工厂
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 30, 2015 3:25:09 PM yiliang.gyl Exp $
 */
public class CalculatorFactory {

    /**
     * The calculator.
     */
    private AbstractLoanPayCalculator calculator;

    /**
     * The loan domain.
     */
    private LoanDomain loanDomain;

    /**
     * 构造方法
     *
     * @param loanDomain the loan domain
     */
    public CalculatorFactory(LoanDomain loanDomain) {
        initCalculator(loanDomain.getPayWay());
        this.loanDomain = loanDomain;
    }

    /**
     * 根据类型初始化计算器
     * ~ 先息后本
     * ~ 等额本息
     *
     * @param payWayEnum the pay way enum
     */
    private void initCalculator(LoanPayWayEnum payWayEnum) {
        if (payWayEnum == LoanPayWayEnum.EVEN_PRINCIPAL_INTEREST) {
            // ~ 等额本息
            calculator = new EvenInterestCalculator();
        } else if (payWayEnum == LoanPayWayEnum.CIRCLE_END_INTEREST_PRINCIPAL) {
            // ~ 先息后本
            calculator = new CircleEndInterestCalculator();
        } else if (payWayEnum == LoanPayWayEnum.MATCHING_PRINCIPAL) {
            // ~ 等额本金
            calculator = new MatchingPrincipalCalculator();
        }
        if (this.calculator == null) {
            throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL.getCode(), "还款方式设置错误");
        }
    }

    /**
     * 放款
     *
     * @return the launch item
     */
    public LoanRecordDomain getLaunchItem() {
        return calculator.calInitPay(loanDomain);
    }

    /**
     * 所有款项列表
     * ~ 放款 + 期间 + 尾款
     *
     * @return the all items
     */
    public List<LoanRecordDomain> getAllItems() {
        return calculator.run(loanDomain);
    }

    /**
     * 期间还款
     *
     * @return the process items
     */
    public List<LoanRecordDomain> getProcessItems() {
        return calculator.calProcessPaybacks(loanDomain);
    }

    /**
     * 尾款
     *
     * @return the end items
     */
    public LoanRecordDomain getEndItems() {
        return calculator.calLastPay(loanDomain);
    }

    /**
     * 重新计算一期还款的应还金额
     * ~ 根据实际还款时间调整
     *
     * @param loanRecordDomain
     * @return
     */
    public LoanRecordDomain recalculatePayback(LoanRecordDomain loanRecordDomain) {
        return calculator.recalItem(loanDomain, loanRecordDomain);
    }
}
