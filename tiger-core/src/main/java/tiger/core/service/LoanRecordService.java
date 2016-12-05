/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.dataobject.LoanRecordDO;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.query.LoanFiscalQuery;
import tiger.common.dal.query.LoanPaybackQuery;
import tiger.core.base.PageResult;
import tiger.core.domain.LoanFiscalListDomain;
import tiger.core.domain.LoanPaybackListDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRecordItemDomain;

import java.util.List;

/**
 * 项目还款款项记录服务
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 30, 2015 9:37:02 PM yiliang.gyl Exp $
 */
public interface LoanRecordService {


    /**
     * 更新还款记录
     *
     * @param loanRecordDomain
     * @return
     */
    LoanRecordDomain update(LoanRecordDomain loanRecordDomain);

    /**
     * 更新贷款记录的熟知详情列表
     * 如果recordItemDomains为空,表示删除详情列表
     *
     * @param recordId
     * @param recordItemDomains
     * @return
     */
    LoanRecordDomain updateRecordItem(Long recordId, List<LoanRecordItemDomain> recordItemDomains);

    /**
     * 分页获取款项列表
     *
     * @param query the query
     * @return the paybacks
     */
    PageResult<List<LoanPaybackListDomain>> getRecordsByPage(LoanPaybackQuery query);

    /**
     * 获取所有款项列表
     *
     * @param query
     * @return
     */
    List<LoanPaybackListDomain> getAllRecords(LoanPaybackQuery query);

    /**
     * 统计款项列表
     *
     * @param query
     * @return
     */
    Integer countRecords(LoanPaybackQuery query);

    /**
     * 通过id 获取一起款项的详情
     *
     * @param recordId the record id
     * @return the loan record by id
     */
    LoanRecordDomain getLoanRecordById(long recordId);

    /**
     * 批量获取款项详情
     *
     * @param recordIds
     * @return
     */
    List<LoanRecordDomain> getLoanRecordsByIds(List<Long> recordIds);


    /**
     * 获取还款类对象
     */
    LoanFiscalListDomain getFiscalDetailsList(LoanFiscalQuery loanFiscalQuery);

    /**
     * 根据loanIds获取款项列表
     *
     * @param loanIds
     * @return
     */
    List<LoanRecordDomain> getLoanRecordsByLoanIds(List<Long> loanIds);

    /**
     * @param loanIds
     * @param actionEnums
     * @return
     */
    List<LoanRecordDomain> getLoanRecordsByLoanIdsAndExcludeTypes(List<Long> loanIds,
                                                                  List<LoanActionEnum> actionEnums);

    /**
     * 根据还款账单编号将记录放入垃圾箱
     *
     * @param ids
     * @return
     */
    Boolean trashLoanRecordsByLoanIds(List<Long> ids);

    /**
     * 根据还款账单编号还原还款账单
     *
     * @param ids
     * @return
     */
    Boolean recoverLoanRecordsByLoanIds(List<Long> ids);

    /**
     * 根据还款账单编号删除记录
     *
     * @param ids
     * @return
     */
    Boolean deleteLoanRecordsByIds(List<Long> ids);

    /**
     * 根据还款账单编号删除还款账单
     *
     * @param loanRecordIds
     * @return
     */
    Boolean deleteLoanRecordItemsByLoanRecordIds(List<Long> loanRecordIds);

    /**
     * 把target的非null字段更新在对应ids的loanRecord中
     *
     * @param target
     * @param ids
     * @return
     */
    boolean updateLoanRecordsByIds(LoanRecordDomain target, List<Long> ids);

    /**
     * 取消该笔还款记录,并恢复至详情
     *
     * @param loanRecordDomain
     * @return
     */
    LoanRecordDomain revertLoanRecord(LoanRecordDomain loanRecordDomain);
    /**
     * 通过LoanID查询LoanRecord
     *
     * @param loanId
     * @return
     */
     List<LoanRecordDO> getLoanRecordByLoanIdAndStatus(long loanId);
}
