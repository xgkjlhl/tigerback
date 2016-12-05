/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.loan.calculator;

import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanPayItemEnum;
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
 * 等额本息计算类
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 27, 2015 10:37:56 AM yiliang.gyl Exp $
 */
public class EvenInterestCalculator extends AbstractLoanPayCalculator {

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
        // 合同尾款日期
        final Date endBackDeadLine = calEndBackDeadLine(loanDomain);

        List<LoanRecordDomain> loanRecordDomains = new ArrayList<>(loanDomain.getPayTotalCircle());
        // 1. 计算每期的利息
        double interestMoney = LoanCalculatorUtil.calInterest(loanDomain.getAmount(),
            loanDomain.getInterestRate(), loanDomain.getPayCircle());
        // 2. 计算每次还款 本金
        Double principalMoney = loanDomain.getAmount() / loanDomain.getPayTotalCircle();
        Date deadLine;

        // 3. 添加
        for (int i = 1; i <= loanDomain.getPayTotalCircle(); ++i) {
            deadLine = LoanDateUtil.getLoanEndDate(loanDomain.getStartPayDate(), (i - 1) * loanDomain.getPayCircle());

            // 1. 收款日期 与比 合同尾款日期,
            // 2. 最后一期
            if (deadLine.after(endBackDeadLine) || i == loanDomain.getPayTotalCircle()) {
                deadLine = endBackDeadLine;
            }

            LoanRecordDomain loanRecordDomain = LoanAssembleUtil.instanceRecordOfProcessCase(
                principalMoney,
                interestMoney,
                loanDomain.getCostSchedule()
            );

            // 设置Record中信息
            LoanAssembleUtil.calRecordAmount(loanRecordDomain);

            loanRecordDomain.setTheoryAmount(loanRecordDomain.getAmount());
            loanRecordDomain.setTheoryInterest(interestMoney);
            loanRecordDomain.setTheoryDeadLine(deadLine);
            loanRecordDomain.setDeadLine(deadLine);
            loanRecordDomain.setLoanId(loanDomain.getId());
            loanRecordDomain.setState(LoanStatusEnum.PAY_PROCESS);
            loanRecordDomain.setLoanPayItemModeEnum(LoanPayItemModeEnum.INCOME);
            loanRecordDomain.setType(LoanActionEnum.LOAN_BACK);

            loanRecordDomain.setProcess(LoanCalculatorUtil.calProcess(i, loanDomain.getPayTotalCircle()));
            loanRecordDomain.setOrder(i);

            // 添加loanRecordDomain
            loanRecordDomains.add(loanRecordDomain);
        }
        return loanRecordDomains;
    }

    /**
     * @see AbstractLoanPayCalculator#calEndPay(LoanDomain)
     */
    @Override
    public LoanRecordDomain calEndPay(LoanDomain loanDomain) {
        // 结清期项目，没有本金和利息
        return LoanAssembleUtil.instanceRecordOfEndCase(0.00, 0.00);
    }


    @Override
    protected List<LoanRecordItemDomain> callItems(LoanDomain loanDomain, LoanRecordDomain loanRecordDomain) {
        return null;
    }

}
