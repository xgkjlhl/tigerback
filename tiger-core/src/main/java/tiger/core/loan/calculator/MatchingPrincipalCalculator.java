/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.loan.calculator;

import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.util.loan.LoanDateUtil;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRecordItemDomain;
import tiger.core.util.LoanAssembleUtil;
import tiger.core.util.LoanCalculatorUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 等额本金计算类
 *
 * @author alfred_yuan
 * @version v 0.1 10:58 alfred_yuan Exp $
 */
public class MatchingPrincipalCalculator extends AbstractLoanPayCalculator {
    /**
     * @see AbstractLoanPayCalculator#initAdditionProcess(LoanDomain, LoanRecordDomain)
     */
    @Override
    public void initAdditionProcess(LoanDomain loanDomain, LoanRecordDomain initDomain) {
        // 没有放款补充流程
    }

    /**
     * @see AbstractLoanPayCalculator#calProcessPaybacks(LoanDomain)
     */
    @Override
    public List<LoanRecordDomain> calProcessPaybacks(LoanDomain loanDomain) {
        // 0. 初始化信息
        List<LoanRecordDomain> loanRecordDomains = new ArrayList<>(loanDomain.getPayTotalCircle());

        // 合同尾款日期
        final Date endBackDeadLine = calEndBackDeadLine(loanDomain);

        Date deadLine;
        Date startPayDate = loanDomain.getStartPayDate();
        int payCircle = loanDomain.getPayCircle();
        int monthSpan = 0;
        Long loanId = loanDomain.getId();
        Double amount = loanDomain.getAmount();
        int payTotalCircle = loanDomain.getPayTotalCircle();
        // 1. 计算第一期的利息 和 之后
        double payInterest = LoanCalculatorUtil.calInterest(amount, loanDomain.getInterestRate(), payCircle);
        // 2. 计算每次还款 本金
        Double payAmount = amount / payTotalCircle;
        // 3. 计算每期利息的减少额度
        double minusInterest = LoanCalculatorUtil.calInterest(payAmount, loanDomain.getInterestRate(), payCircle);
        // 3. 添加
        for (int step = 1; step <= loanDomain.getPayTotalCircle(); ++step) {
            deadLine = LoanDateUtil.getLoanEndDate(startPayDate, monthSpan);
            // 1. 收款日期 与比 合同尾款日期,
            // 2. 最后一期
            if (deadLine.after(endBackDeadLine) || step == loanDomain.getPayTotalCircle()) {
                deadLine = endBackDeadLine;
            }

            LoanRecordDomain loanRecordDomain = LoanAssembleUtil.instanceRecordOfProcessCase(
                payAmount,
                payInterest,
                loanDomain.getCostSchedule()
            );

            // 设置Record中信息
            LoanAssembleUtil.calRecordAmount(loanRecordDomain);

            loanRecordDomain.setTheoryAmount(loanRecordDomain.getAmount());
            loanRecordDomain.setTheoryInterest(payInterest);
            loanRecordDomain.setTheoryDeadLine(deadLine);
            loanRecordDomain.setDeadLine(deadLine);
            loanRecordDomain.setLoanId(loanId);
            loanRecordDomain.setState(LoanStatusEnum.PAY_PROCESS);
            loanRecordDomain.setLoanPayItemModeEnum(LoanPayItemModeEnum.INCOME);
            loanRecordDomain.setType(LoanActionEnum.LOAN_BACK);
            loanRecordDomain.setProcess(LoanCalculatorUtil.calProcess(step, payTotalCircle));
            loanRecordDomain.setOrder(step);

            // 添加loanRecordDomain
            loanRecordDomains.add(loanRecordDomain);
            // 更新每期还款的利息,和
            payInterest -= minusInterest;
            monthSpan += payCircle;
        }
        return loanRecordDomains;
    }

    /**
     * @see AbstractLoanPayCalculator#calEndPay(LoanDomain)
     */
    @Override
    protected LoanRecordDomain calEndPay(LoanDomain loanDomain) {
        // 结清期项目，本金和利息均为0
        return LoanAssembleUtil.instanceRecordOfEndCase(0.00, 0.00);
    }

    /**
     * @see AbstractLoanPayCalculator#callItems(LoanDomain, LoanRecordDomain)
     */
    @Override
    protected List<LoanRecordItemDomain> callItems(LoanDomain loanDomain, LoanRecordDomain loanRecordDomain) {
        // 暂无其他需要计算的还款项目
        return null;
    }
}
