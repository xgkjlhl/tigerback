/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.dataobject.LoanRecordDO;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanMergedBillDomain;
import tiger.core.domain.LoanRecordDomain;

import java.util.List;

/**
 * 贷款收付服务.
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 30, 2015 4:13:38 PM yiliang.gyl Exp $
 */
public interface LoanPayService {

    /**
     * 一个项目放款
     *
     * @param loanRecordDomain the loanRecordDomain
     * @return true, if successful
     */
    boolean launch(LoanRecordDomain loanRecordDomain, List<LoanRecordDomain> payBackItems);

    /**
     * 放款账单
     *
     * @param loanDomain the loan domain
     * @return the loan record domain
     */
    LoanRecordDomain calLaunchBill(LoanDomain loanDomain);

    /**
     * 期间还款账单.
     *
     * @param loanDomain the loan domain
     * @return the list
     */
    List<LoanRecordDomain> calProcessBills(LoanDomain loanDomain);

    /**
     * 存储一条还款记录
     *
     * @param loanRecordDomain the loan record domain
     * @return true, if successful
     */
    LoanRecordDomain saveLoanRecordDomain(LoanRecordDomain loanRecordDomain);

    /**
     * 更新一条还款记录
     *
     * @param loanRecordDomain
     * @return
     */
    LoanRecordDomain updateLoanRecordDomain(LoanRecordDomain loanRecordDomain);

    /**
     * 删除一条还款记录
     *
     * @param loanRecordId
     * @return
     */
    boolean deleteLoanRecordDomain(Long loanRecordId);

    /**
     * 重新计算还款账单
     *
     * @param loanRecordDomain ~必须存在实际还款日期
     * @return 计算后的账单
     */
    LoanRecordDomain calPayBill(LoanRecordDomain loanRecordDomain);

    /**
     * 单期还款
     *
     * @param loanRecordDomain
     * @return
     */
    boolean payLoanRecord(LoanRecordDomain loanRecordDomain);

    /**
     * 还款服务
     *
     * @param loanMergedBillDomain 还款列表
     * @return
     */
    boolean payLoanRecords(LoanMergedBillDomain loanMergedBillDomain);
    /**
     * 客户手动修改放款收款明细的合同的放款接口
     *
     * @param loanRecordDomain
     * @param  loanRocordDos
     * @return
     */
     boolean manualCreateLoanToLaunch(LoanRecordDomain loanRecordDomain,List<LoanRecordDO> loanRocordDos);

}
