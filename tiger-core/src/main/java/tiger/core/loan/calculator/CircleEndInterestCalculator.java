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
 * 先息后本
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 27, 2015 10:41:32 AM yiliang.gyl Exp $
 */
public class CircleEndInterestCalculator extends AbstractLoanPayCalculator {

    /**
     * @see AbstractLoanPayCalculator#initAdditionProcess(tiger.core.domain.LoanDomain,
     * tiger.core.domain.LoanRecordDomain)
     */
    @Override
    public void initAdditionProcess(LoanDomain loanDomain, LoanRecordDomain initDomain) {
    }

    /**
     * @see AbstractLoanPayCalculator#calProcessPaybacks(LoanDomain)
     */
    @Override
    public List<LoanRecordDomain> calProcessPaybacks(LoanDomain loanDomain) {
        List<LoanRecordDomain> domains = new ArrayList<>();
        //周期末还本付息
        // ~ 周期中只还利息
        double interest = LoanCalculatorUtil.calInterest(
            loanDomain.getAmount(),
            loanDomain.getInterestRate(),
            loanDomain.getPayCircle()
        );
        // 合同尾款日期
        final Date endBackDeadLine = calEndBackDeadLine(loanDomain);
        Date deadLine;
        LoanRecordDomain loanRecordDomain;
        for (int step = 1; step <= loanDomain.getPayTotalCircle(); step++) {
            loanRecordDomain = LoanAssembleUtil.instanceRecordOfProcessCase(
                0.00,
                interest,
                loanDomain.getCostSchedule()
            );
            deadLine = LoanDateUtil.getLoanEndDate(loanDomain.getStartPayDate(), (step - 1) * loanDomain.getPayCircle());
            if (deadLine.after(endBackDeadLine)) {
                deadLine = endBackDeadLine;
            }

            loanRecordDomain.setDeadLine(deadLine);
            LoanAssembleUtil.calRecordAmount(loanRecordDomain);
            loanRecordDomain.setLoanId(loanDomain.getId());
            loanRecordDomain.setState(LoanStatusEnum.PAY_PROCESS);
            loanRecordDomain.setType(LoanActionEnum.LOAN_BACK);

            loanRecordDomain.setLoanPayItemModeEnum(LoanPayItemModeEnum.INCOME);

            loanRecordDomain.setTheoryAmount(loanRecordDomain.getAmount());
            loanRecordDomain.setTheoryInterest(interest);
            loanRecordDomain.setTheoryDeadLine(loanRecordDomain.getDeadLine());

            loanRecordDomain.setProcess(LoanCalculatorUtil.calProcess(step, loanDomain.getPayTotalCircle()));
            loanRecordDomain.setOrder(step);

            domains.add(loanRecordDomain);
        }
        return domains;
    }

    /**
     * @see AbstractLoanPayCalculator#calEndPay(LoanDomain)
     */
    @Override
    public LoanRecordDomain calEndPay(LoanDomain loanDomain) {
        //周期末还款加入最后一期的利息
        //double interest = LoanCalculatorUtil.calInterest(loanDomain.getAmount(),
        //            loanDomain.getInterestRate(), loanDomain.getPayCircle());
        LoanRecordDomain loanRecordDomain = LoanAssembleUtil.instanceRecordOfEndCase(loanDomain.getAmount(), 0.00);

        return loanRecordDomain;
    }

    @Override
    protected List<LoanRecordItemDomain> callItems(LoanDomain loanDomain, LoanRecordDomain loanRecordDomain) {
        //除了计算违约金外，是不是还有其他需要计算的？
        loanDomain.getPenaltyRate();//逾期违约金率
        return null;
    }
}
