/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.dto.list.AccountWorkspaceListDTO;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanCustomerTypeEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.dal.query.LoanQuery;
import tiger.core.base.PageResult;
import tiger.core.domain.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 贷款服务.
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 23, 2015 10:33:51 AM yiliang.gyl Exp $
 */
public interface LoanService {

    /**
     * 插入一条贷款信息
     * ~ 只插入基本信息.
     *
     * @param loanDomain the loan template domain
     * @return the int
     */
    LoanDomain insert(LoanDomain loanDomain);

    /**
     * 删除一条.
     *
     * @param id the id
     * @return the int
     */
    boolean delete(long id);

    /**
     * 通过id获取.
     *
     * @param id the id
     * @return the loan template by id
     */
    LoanDomain read(Long id);

    /**
     * 通过ids获取轻量loanLightDomain列表
     *
     * @param loanIds
     * @return
     */
    List<LoanLightDomain> lightBatchRead(List<Long> loanIds);

    /**
     * 通过 keyId 和 accountId 查询项目
     *
     * @param keyId
     * @param accountId
     * @return
     */
    List<LoanDomain> findByKeyId(String keyId, long accountId);

    /**
     * 更新.
     *
     * @param loanDomain the loan template domain
     * @return the LoanDomain
     */
    LoanDomain update(LoanDomain loanDomain);

    /**
     * 取所有满足loanQueryDomain条件的贷款列表.
     * 已完成分页
     *
     * @param query the query
     * @return the page result
     */
    PageResult<List<LoanDomain>> listLoanListDomain(LoanQuery query);

    /**
     * 获取所有.
     *
     * @param query the query
     * @return the list
     */
    List<LoanDomain> all(LoanQuery query);

    /**
     * 为一个项目关联客户.
     *
     * @param loanId           the loan id
     * @param customerDomain   the customer domain
     * @param customerTypeEnum the customer type enum
     * @return the int
     */
    boolean relateCustomer(long loanId, CustomerDomain customerDomain, LoanCustomerTypeEnum customerTypeEnum);

    /**
     * 为一个项目关联抵押物.
     *
     * @param loanId the loan id
     * @param pawnId the pawn id
     * @return the int
     */
    boolean relatePawn(long loanId, long pawnId);

    /**
     * 判断当前用户是否是项目所有者.
     *
     * @param loanId    the loan id
     * @param accountId the account id
     * @return true, if is owner
     */
    boolean isOwner(long loanId, long accountId);

    /**
     * 获取贷款人和担保人信息(包括附件信息,列表输出).
     *
     * @param id the id
     * @return the loan loaner by id
     */
    Map<LoanCustomerTypeEnum, CustomerDomain> getLoanerById(long id);

    /**
     * 更新贷款人信息,只更新快照
     *
     * @param id       the id
     * @param customer the customer domain
     * @return true, if is success
     */
    Boolean updateLoanerById(Long id, CustomerDomain customer);

    /**
     * 获取贷款履约记录.
     *
     * @param id the id
     * @return the loan records by id
     */
    List<LoanRecordDomain> getLoanRecordsById(long id);

    /**
     * 获取通过操作方式过滤的贷款履约记录
     *
     * @param id
     * @param actions
     * @return
     */
    List<LoanRecordDomain> getLoanRecordsByIdAndActionTypes(long id, List<LoanActionEnum> actions);

    /**
     * 获取带条件(包含)的贷款履约记录
     *
     * @param loanId the loan id
     * @param states the states
     * @return the loan record by loan id and include status
     */
    List<LoanRecordDomain> getLoanRecordByInclusiveStatus(long loanId, List<LoanStatusEnum> states);

    /**
     * 获取带条件(包含)的贷款履约记录
     *
     * @param loanId the loan id
     * @param states the states
     * @return the loan record by loan id and include status
     */
    List<LoanRecordDomain> getLoanRecordByExclusiveStatus(long loanId, List<LoanStatusEnum> states);

    /**
     * 获取用户所有非states状态的贷款列表.
     * states如果为null或为空则返回该用户所有的贷款
     *
     * @param accountId the account id
     * @param states    the states
     * @return the exclusive loans
     */
    List<LoanDomain> getExclusiveLoans(long accountId, List<LoanStatusEnum> states);

    /**
     * 获取用户所有states状态的贷款列表.
     * states如果为null或为空则返回null
     *
     * @param accountId the account id
     * @param states    the states
     * @return the inclusive loans
     */
    List<LoanDomain> getInclusiveLoans(long accountId, List<LoanStatusEnum> states);


    /**
     * 为一组loanRecord对象添加详情items
     *
     * @param domains
     */
    void addRecordItemsToRecords(List<LoanRecordDomain> domains);


    /**
     * 更新项目状态
     */
    boolean updateLoanState(long loanId, LoanStatusEnum status);

    /**
     * 判断项目是否可以完结
     * ~ 是否只有尾款没有还
     *
     * @param loanId
     * @return
     */
    boolean canFinish(long loanId);

    /**
     * 更新项目的最近需操作时间
     *
     * @param loanId
     * @return
     */
    boolean updateOperateDate(Long loanId);

    /**
     * 根据id删除抵押物附件
     *
     * @param attachId
     * @return
     */
    boolean deleteLoanAttach(long loanId, long attachId);

    /**
     * 获取指定operateDate所有收款中的loans
     *
     * @param operateDate
     * @return
     */
    List<LoanDomain> getPayProcessLoansAtOperateDate(Date operateDate);

    /**
     * 获取指定operateDate之前的所有收款中的loans
     *
     * @param operateDate
     * @return
     */
    List<LoanDomain> getPayProcessLoansBeforeOperateDate(Date operateDate);

    /**
     * 根据合同id获取当前正在履行的履约记录
     * 即,将合同调整前已完成的履约记录 和 异常修正 排除
     *
     * @param loanId
     * @return
     */
    List<LoanRecordDomain> getCurrentLoanRecordsById(Long loanId);

    /**
     * 指派合同给其他用户
     *
     * @param loanId
     * @param accountId
     * @return
     */
    Boolean assignLoanById(Long loanId, Long accountId);

    /**
     * 得到loanId贷款合同的最近一笔还款账单
     *
     * @param loanId
     * @return
     */
    LoanRecordDomain getRecentToPayRecord(Long loanId);

    /**
     * 根据workspaceId获取该工作空间的所有贷款合同id
     *
     * @param workspaceId
     * @return
     */
    List<Long> getLoanIdsByWorkspaceId(Long workspaceId);


    /**
     * 删除ids的贷款合同
     *
     * @param ids
     * @return
     */
    boolean delete(List<Long> ids);

    /**
     * 获取给定workspace下loan数目
     *
     * @param workspaceId
     * @return
     */
    int countWorkspaceLoans(Long workspaceId);

    /**
     * 根据loanQuery对贷款合同进行计数
     *
     * @param loanQuery
     * @return
     */
    int countLoan(LoanQuery loanQuery);

    /**
     * 获取workspaceIds下素有的合同id
     *
     * @param workspaceIds
     * @return
     */
    List<AccountWorkspaceListDTO> getWorkSpaceLoanIds(List<Long> workspaceIds);

    /**
     * 通过ids获取轻量loanLightDomain列表, 包括已经删除了的
     *
     * @param loanIds
     * @return
     */
    List<LoanLightDomain> lightBatchReadAll(List<Long> loanIds);
    /**
     * 客户修改合同的放款收款明细的合同
     *
     * @param loanDomain  合同信息
     * @param modifyLoanRecordDomains  客户修改的还款明细列表
     * @return  List<LoanRecordDomain>
     */
    List<LoanRecordDomain> manualInsertLoan(LoanDomain loanDomain,List<ModifyLoanRecordDomain> modifyLoanRecordDomains);
}
