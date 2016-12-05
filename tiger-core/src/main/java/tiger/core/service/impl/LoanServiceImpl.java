/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tiger.common.dal.dataobject.LoanCustomerDO;
import tiger.common.dal.dataobject.LoanDO;
import tiger.common.dal.dataobject.LoanRecordDO;
import tiger.common.dal.dataobject.example.LoanCustomerExample;
import tiger.common.dal.dataobject.example.LoanExample;
import tiger.common.dal.dataobject.example.LoanRecordExample;
import tiger.common.dal.dto.list.AccountWorkspaceListDTO;
import tiger.common.dal.dto.LoanLightDTO;
import tiger.common.dal.enums.*;
import tiger.common.dal.persistence.LoanCustomerMapper;
import tiger.common.dal.persistence.LoanMapper;
import tiger.common.dal.persistence.LoanRecordItemsMapper;
import tiger.common.dal.persistence.LoanRecordMapper;
import tiger.common.dal.query.LoanQuery;
import tiger.common.util.DateUtil;
import tiger.common.util.JsonUtil;
import tiger.common.util.Paginator;
import tiger.common.util.loan.LoanDateUtil;
import tiger.core.base.PageResult;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.*;
import tiger.core.domain.convert.LoanConvert;
import tiger.core.domain.convert.LoanLightConvert;
import tiger.core.domain.convert.LoanRecordConvert;
import tiger.core.domain.convert.LoanRecordItemConvert;
import tiger.core.domain.extraDomain.ExtraField;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.AccountService;
import tiger.core.service.LoanPawnService;
import tiger.core.service.LoanPayService;
import tiger.core.service.LoanService;
import tiger.core.util.LoanAssembleUtil;
import tiger.core.util.LoanCalculatorUtil;

import java.text.ParseException;
import java.util.*;

/**
 * 贷款服务实现
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 23, 2015 10:34:01 AM yiliang.gyl Exp $
 */
@Service
public class LoanServiceImpl implements LoanService {

    private final Logger logger = Logger.getLogger(LoanServiceImpl.class);

    @Autowired
    private LoanPawnService loanPawnService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanCustomerMapper loanCustomerMapper;

    @Autowired
    private LoanPayService loanPayService;

    @Autowired
    private LoanRecordMapper loanRecordMapper;

    @Autowired
    private LoanRecordItemsMapper loanRecordItemsMapper;

    @Autowired
    private AccountService accountService;

    /**
     * @see LoanService#insert(tiger.core.domain.LoanDomain)
     */
    @Override
    @Transactional
    public LoanDomain insert(LoanDomain loanDomain) {
        LoanDO loanDO = LoanConvert.convertDomainToDO(loanDomain);
        if (logger.isInfoEnabled()) {
            logger.info("存储合同基本信息 [" + loanDO + "]");
        }
        loanDO.setIsDelete(false);
        loanDO.setOperateDate(loanDomain.getStartDate());

        int rtCode = loanMapper.insertSelective(loanDO);
        if (rtCode > 0) {
            LoanRecordDomain recordDomain = loanPayService.calLaunchBill(loanDomain);
            recordDomain.setLoanId(loanDO.getId());
            recordDomain.setWorkspaceId(loanDO.getWorkspaceId());
            if (recordDomain != null) {
                if (logger.isInfoEnabled()) {
                    logger.info("生成合同放款信息并入库,合同信息为 [" + recordDomain + "]");
                }
                LoanRecordDomain loanRecordDomain = loanPayService.saveLoanRecordDomain(recordDomain);
                if (loanRecordDomain == null) {
                    throw new TigerException(ErrorCodeEnum.DB_EXCEPTION.getCode(), "生成放款信息失败");
                }
            }
            return read(loanDO.getId());
        } else {
            logger.error("存储项目基本信息失败 [" + loanDO + "]");
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION.getCode(), "数据插入失败");
        }
    }

    /**
     *
     *
     * */
    public  List<LoanRecordDomain>  manualInsertLoan(LoanDomain loanDomain,List<ModifyLoanRecordDomain> modifyLoanRecordDomains){

        if(modifyLoanRecordDomains==null||modifyLoanRecordDomains.size()==0){
            logger.error("存储项目基本信息失败 [" + modifyLoanRecordDomains + "]");
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION.getCode(), "数据插入失败");
        }
        List<LoanRecordDomain> loanRecordDomains=new ArrayList<LoanRecordDomain>();
        //收款还款明细部分
        for(int i=0;i<modifyLoanRecordDomains.size()-1;i++) {
            ModifyLoanRecordDomain modifyLoanRecordDomain= modifyLoanRecordDomains.get(i);
            ExtraField ef=null;
            ExtraField resultEf=loanDomain.getCostSchedule();
            if(resultEf!=null){
                ef=new ExtraField();
                ef.setDescription(resultEf.getDescription());
                ef.setName(resultEf.getName());
                ef.setValue(modifyLoanRecordDomain.getCostSchedule());
            }
            LoanRecordDomain loanRecordDomain = LoanAssembleUtil.instanceRecordOfProcessCase(
                    modifyLoanRecordDomain.getAmount(),
                    modifyLoanRecordDomain.getInterest(),
                    ef
            );
            // 合同尾款日期
            final Date endBackDeadLine = calEndBackDeadLine(loanDomain);
            Date deadLine=null;
            try {
                 deadLine= DateUtil.parseDateNoTime(modifyLoanRecordDomain.getDiedLine(),DateUtil.webFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            modifyLoanRecordDomain.getDiedLine();
            loanRecordDomain.setDeadLine(deadLine);
            Double totalAmount=modifyLoanRecordDomain.getAmount()+modifyLoanRecordDomain.getInterest()+modifyLoanRecordDomain.getCostSchedule();
            loanRecordDomain.setAmount(totalAmount);
            loanRecordDomain.setLoanId(loanDomain.getId());
            loanRecordDomain.setState(LoanStatusEnum.WAIT_LAUNCH);
            loanRecordDomain.setType(LoanActionEnum.LOAN_BACK);
            loanRecordDomain.setLoanPayItemModeEnum(LoanPayItemModeEnum.INCOME);
            loanRecordDomain.setTheoryAmount(totalAmount);
            loanRecordDomain.setTheoryInterest(modifyLoanRecordDomain.getInterest());
            loanRecordDomain.setTheoryDeadLine(deadLine);
            loanRecordDomain.setProcess(LoanCalculatorUtil.calProcess(i+1,modifyLoanRecordDomains.size()-1));
            loanRecordDomain.setOrder(i+2);
            loanRecordDomain.setWorkspaceId(loanDomain.getWorkspaceId());
            loanRecordDomains.add(loanRecordDomain);
        }
        //尾款部分
        LoanRecordDomain loanRecordDomain = new LoanRecordDomain();
        ModifyLoanRecordDomain lastLoan=modifyLoanRecordDomains.get(modifyLoanRecordDomains.size()-1);
        // 合同尾款日期
        final Date endBackDeadLine = calEndBackDeadLine(loanDomain);
        Date deadLine=null;
        try {
            deadLine= DateUtil.parseDateNoTime(lastLoan.getDiedLine(),DateUtil.webFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Double totalAmount=lastLoan.getAmount()+lastLoan.getInterest()+
                lastLoan.getCostSchedule()-lastLoan.getBondPerformMoney()-lastLoan.getTempMoney();
        loanRecordDomain.setDeadLine(deadLine);
        loanRecordDomain.setAmount(totalAmount);
        loanRecordDomain.setLoanId(loanDomain.getId());
        loanRecordDomain.setState(LoanStatusEnum.WAIT_LAUNCH);
        loanRecordDomain.setType(LoanActionEnum.LOAN_END_BACK);
        loanRecordDomain.setLoanPayItemModeEnum(LoanPayItemModeEnum.INCOME);
        loanRecordDomain.setTheoryAmount(totalAmount);
        loanRecordDomain.setTheoryInterest(lastLoan.getInterest());
        loanRecordDomain.setTheoryDeadLine(deadLine);
        loanRecordDomain.setOrder(modifyLoanRecordDomains.size()+1);
        loanRecordDomain.setWorkspaceId(loanDomain.getWorkspaceId());
        createLastMoney(loanRecordDomain,lastLoan);
        loanRecordDomains.add(loanRecordDomain);
        return loanRecordDomains;
    }

    private void  createLastMoney(LoanRecordDomain loanRecordDomain,ModifyLoanRecordDomain lastLoan){
        loanRecordDomain.addItem(LoanRecordItemDomain.instance(lastLoan.getAmount(),
                LoanPayItemEnum.PRINCIPAL_MONEY,LoanPayItemModeEnum.INCOME));
        loanRecordDomain.addItem(LoanRecordItemDomain.instance(lastLoan.getInterest(),
                LoanPayItemEnum.INTEREST,LoanPayItemModeEnum.INCOME));
        loanRecordDomain.addItem(LoanRecordItemDomain.instance(lastLoan.getBondPerformMoney(),
                LoanPayItemEnum.BOND_PERFORM_MONEY,LoanPayItemModeEnum.OUTCOME));
        loanRecordDomain.addItem(LoanRecordItemDomain.instance(lastLoan.getTempMoney(),
                LoanPayItemEnum.COST_TEMP,LoanPayItemModeEnum.OUTCOME));
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



    /**
     * @see LoanService#delete(long)
     */
    @Override
    public boolean delete(long id) {
        return loanMapper.deleteByPrimaryKey(id) > 0;
    }

    /**
     * @see LoanService#read(Long)
     */
    @Override
    public LoanDomain read(Long id) {
        LoanDO loanDO = loanMapper.selectByPrimaryKey(id);
        if (null == loanDO) {
            return null;
        }
        LoanDomain loanDomain = LoanConvert.convertDoToDomain(loanDO);
        if (loanDO.getLoanPawnId() != null) {
            loanDomain.setLoanPawnDomain(loanPawnService.read(loanDO.getLoanPawnId()));
        }
        if (loanDO.getAccountId() != null) {
            loanDomain.setOwnerInfo(accountService.getBaseInfo(loanDO.getAccountId()));
        }
        return loanDomain;
    }

    /**
     * @see LoanService#lightBatchRead(List)
     */
    @Override
    public List<LoanLightDomain> lightBatchRead(List<Long> loanIds) {
        if (CollectionUtils.isEmpty(loanIds)) {
            return new ArrayList<>();
        }
        List<LoanLightDTO> loanLightDTOs = loanMapper.lightSelectByPrimaryKeys(loanIds, true);
        return LoanLightConvert.convert2Domains(loanLightDTOs);
    }

    /**
     * @see LoanService#findByKeyId(String, long)
     */
    @Override
    public List<LoanDomain> findByKeyId(String keyId, long accountId) {
        LoanExample example = new LoanExample();
        example.createCriteria().andKeyIdEqualTo(keyId).andAccountIdEqualTo(accountId);
        return LoanConvert.convertDOsToDomains(loanMapper.selectByExample(example));
    }

    /**
     * @see LoanService#update(tiger.core.domain.LoanDomain)
     */
    @Override
    public LoanDomain update(LoanDomain loanDomain) {
        if (loanDomain.getId() == null) {
            return null;
        }
        LoanDO loanDO = LoanConvert.convertDomainToDO(loanDomain);
        if (loanMapper.updateByPrimaryKeySelective(loanDO) <= 0) {
            logger.error("更新贷款失败,收到的参数为: [" + loanDomain + "]");
            return null;
        }
        return read(loanDomain.getId());
    }

    /**
     * @see LoanService#all(LoanQuery)
     */
    @Override
    public List<LoanDomain> all(LoanQuery query) {
        return LoanConvert.convertDOsToDomains(loanMapper.all(query));
    }

    /**
     * @see LoanService#relateCustomer(long, CustomerDomain, LoanCustomerTypeEnum)
     */
    @Override
    @Transactional
    public boolean relateCustomer(long loanId, CustomerDomain customerDomain,
                                  LoanCustomerTypeEnum customerTypeEnum) {
        LoanDomain loanDomain = read(loanId);
        if (loanDomain == null) {
            return false;
        }
        LoanCustomerDO loanCustomerDO = new LoanCustomerDO();
        loanCustomerDO.setCustomerId(customerDomain.getId());
        loanCustomerDO.setLoanId(loanId);
        loanCustomerDO.setOrder(0);
        loanCustomerDO.setType(customerTypeEnum.getCode());

        loanCustomerDO.setCustomerSnapshot(JsonUtil.toJson(customerDomain));
        return loanCustomerMapper.insert(loanCustomerDO) > 0;
    }

    /**
     * @see LoanService#relatePawn(long, long)
     */
    @Override
    public boolean relatePawn(long loanId, long pawnId) {
        return loanMapper.relateLoanPawn(loanId, pawnId) > 0;
    }

    /**
     * @see LoanService#isOwner(long, long)
     */
    @Override
    public boolean isOwner(long loanId, long accountId) {
        LoanDO loanDO = loanMapper.selectByPrimaryKey(loanId);
        if (loanDO == null) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        return loanDO.getAccountId().equals(accountId);
    }

    /**
     * @see LoanService#listLoanListDomain(LoanQuery)
     */
    @Override
    public PageResult<List<LoanDomain>> listLoanListDomain(LoanQuery query) {
        // 如果查询请求里限定贷款状态不为垃圾箱，则去除垃圾箱内项目
        query = setTrashStatus(query);

        // 设置默认的排序顺序
        if (query.getOrder() == null) {
            query.setOrder(DBOrderTypeEnum.DESC.getCode());
        }

        int totalItems = loanMapper.countLoans(query);

        // 分页器构建
        Paginator paginator = new Paginator();
        paginator.setItems(totalItems);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum()); // 目前选择的页码

        PageResult<List<LoanDomain>> results = new PageResult<>();
        results.setData(LoanConvert.convertDOsToDomains(
                loanMapper.queryLoans(query, paginator.getOffset(), paginator.getLength())));
        results.setPaginator(paginator);

        return results;
    }

    /**
     * @see LoanService#getLoanerById(long)
     */
    @Override
    public Map<LoanCustomerTypeEnum, CustomerDomain> getLoanerById(long id) {
        LoanCustomerExample example = new LoanCustomerExample();
        example.createCriteria().andLoanIdEqualTo(id);
        List<LoanCustomerDO> customerDOs = loanCustomerMapper.selectByExample(example);

        Map<LoanCustomerTypeEnum, CustomerDomain> customerMap = new HashMap<>();

        for (LoanCustomerDO dto : customerDOs) {
            CustomerDomain customer = JsonUtil.fromJson(dto.getCustomerSnapshot(), CustomerDomain.class);
            // 放到结果map中
            customerMap.put(LoanCustomerTypeEnum.getEnumByCode(dto.getType()), customer);
        }

        return customerMap;
    }

    /**
     * @see LoanService#updateLoanerById(Long, CustomerDomain)
     */
    @Override
    public Boolean updateLoanerById(Long id, CustomerDomain customer) {
        if (id == null || customer == null || customer.getId() == null) {
            return false;
        }

        LoanCustomerExample example = new LoanCustomerExample();
        example.createCriteria()
                .andLoanIdEqualTo(id)
                .andCustomerIdEqualTo(customer.getId());

        LoanCustomerDO loanCustomerDO = new LoanCustomerDO();
        loanCustomerDO.setCustomerSnapshot(JsonUtil.toJson(customer));

        return loanCustomerMapper.updateByExampleSelective(loanCustomerDO, example) > 0;
    }

    /**
     * @see LoanService#getLoanRecordsById(long)
     */
    @Override
    public List<LoanRecordDomain> getLoanRecordsById(long id) {
        // TODO 获取到的履约记录,需要有序
        List<LoanRecordDomain> recordDomains = LoanRecordConvert
                .convert2Domains(loanRecordMapper.getLoanRecordsByLoanId(id));
        addRecordItemsToRecords(recordDomains);
        return recordDomains;
    }

    /**
     * @see LoanService#getLoanRecordsByIdAndActionTypes(long, List)
     */
    @Override
    public List<LoanRecordDomain> getLoanRecordsByIdAndActionTypes(long id, List<LoanActionEnum> actions) {
        LoanRecordExample example = new LoanRecordExample();
        List<String> actionList = new ArrayList<>(actions.size());
        actions.forEach(p -> actionList.add(p.getCode()));
        example.createCriteria().andLoanIdEqualTo(id).andTypeIn(actionList);

        List<LoanRecordDomain> recordDomains = LoanRecordConvert.convert2Domains(loanRecordMapper.selectByExample(example));

        addRecordItemsToRecords(recordDomains);

        return recordDomains;

    }

    /**
     * @see LoanService#getLoanRecordByInclusiveStatus(long, java.util.List)
     */
    @Override
    public List<LoanRecordDomain> getLoanRecordByInclusiveStatus(long loanId,
                                                                 List<LoanStatusEnum> states) {
        if (states == null || states.size() == 0) {
            return new ArrayList<>();
        }
        List<LoanRecordDomain> recordDomains = LoanRecordConvert.convert2Domains(
                loanRecordMapper.getLoanRecordByInclusiveStatus(loanId, statusToStrArray(states)));
        addRecordItemsToRecords(recordDomains);
        return recordDomains;
    }

    /**
     * @see LoanService#getLoanRecordByExclusiveStatus(long, java.util.List)
     */
    @Override
    public List<LoanRecordDomain> getLoanRecordByExclusiveStatus(long loanId,
                                                                 List<LoanStatusEnum> states) {
        List<LoanRecordDomain> recordDomains = LoanRecordConvert.convert2Domains(
                loanRecordMapper.getLoanRecordByExclusiveStatus(loanId, statusToStrArray(states)));
        addRecordItemsToRecords(recordDomains);
        return recordDomains;
    }

    /**
     * @see LoanService#getExclusiveLoans(long, List)
     */
    @Override
    public List<LoanDomain> getExclusiveLoans(long accountId,
                                              List<LoanStatusEnum> states) {
        if (null == states) {
            states = new ArrayList<>();
        }
        return LoanConvert.convertDOsToDomains(
                loanMapper.exclusiveLoans(accountId, statusToStrArray(states)));
    }

    /**
     * @see LoanService#getInclusiveLoans(long, java.util.List)
     */
    @Override
    public List<LoanDomain> getInclusiveLoans(long accountId,
                                              List<LoanStatusEnum> states) {
        if (null == states || states.isEmpty()) {
            return new ArrayList<>();
        }
        return LoanConvert.convertDOsToDomains(
                loanMapper.inclusiveLoans(accountId, statusToStrArray(states)));
    }

    /**
     * @see LoanService#addRecordItemsToRecords(List)
     */
    public void addRecordItemsToRecords(List<LoanRecordDomain> domains) {
        if (CollectionUtils.isNotEmpty(domains)) {
            // 使用hashmap，方便查找
            Map<Long, LoanRecordDomain> recordMap = new HashMap<>();
            List<Long> recordIds = new ArrayList<>();
            for (LoanRecordDomain domain : domains) {
                recordIds.add(domain.getId());
                recordMap.put(domain.getId(), domain);
            }
            List<LoanRecordItemDomain> recordItemDomains = LoanRecordItemConvert
                    .convert2Domains(loanRecordItemsMapper.getRecordItemsByRecordIds(recordIds, null));
            // 将item添加到对应的record中
            for (LoanRecordItemDomain domain : recordItemDomains) {
                recordMap.get(domain.getLoanRecordId()).addItem(domain);
            }
        }
    }

    /**
     * @see LoanService#updateLoanState(long, LoanStatusEnum)
     */
    @Override
    public boolean updateLoanState(long loanId, LoanStatusEnum status) {
        LoanDO loanDO = new LoanDO();
        loanDO.setId(loanId);
        loanDO.setState(status.getCode());

        int rc = loanMapper.updateByPrimaryKeySelective(loanDO);

        return rc > 0;
    }

    /**
     * @see LoanService#canFinish(long)
     */
    @Override
    public boolean canFinish(long loanId) {
        LoanRecordExample loanRecordExample = new LoanRecordExample();
        List<String> loanTypes = new ArrayList<>();
        loanTypes.add(LoanActionEnum.LOAN_BACK.getCode());
        loanTypes.add(LoanActionEnum.LOAN_LAUNCH.getCode());
        loanRecordExample.createCriteria().andLoanIdEqualTo(loanId).andStateEqualTo(LoanStatusEnum.PAY_PROCESS.getCode())
                .andTypeIn(loanTypes);
        if (loanRecordMapper.countByExample(loanRecordExample) > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @see LoanService#updateOperateDate(Long)
     */
    @Override
    public boolean updateOperateDate(Long loanId) {
        LoanDomain loanDomain = read(loanId);
        if (loanDomain == null) {
            logger.error("未找到合同 [" + loanId + "]");
            return false;
        }
        if (loanDomain.getLoanStatus() == LoanStatusEnum.FINISH) {
            logger.info("合同 [" + loanId + "] 已完结，无需更新操作日期.");
            return false;
        }

        LoanRecordDomain recentToPay = getRecentToPayRecord(loanId);

        Date operateDate = null;
        if (recentToPay != null) {
            operateDate = recentToPay.getDeadLine();
            if (logger.isInfoEnabled()) {
                logger.info("合同 [" + loanId + "]操作日期更新: " + operateDate);
            }
        } else {
            logger.error("合同 [" + loanId + "] 没有任何可操作日期了");
        }

        LoanDO loanDO = new LoanDO();
        loanDO.setId(loanId);
        loanDO.setOperateDate(operateDate);

        return loanMapper.updateByPrimaryKeySelective(loanDO) > 0;
    }

    /**
     * @see LoanService#deleteLoanAttach(long, long)
     * TODO: 未实现的方法
     */
    @Override
    public boolean deleteLoanAttach(long loanId, long attachId) {
        return false;
    }

    /**
     * @see LoanService#getPayProcessLoansAtOperateDate(Date)
     */
    @Override
    public List<LoanDomain> getPayProcessLoansAtOperateDate(Date operateDate) {
        LoanExample example = new LoanExample();
        LoanExample.Criteria criteria = example.createCriteria();

        criteria.andStateEqualTo(LoanStatusEnum.PAY_PROCESS.getCode());
        criteria.andBusinessTypeEqualTo(BusinessTypeEnum.LOAN.getCode());
        criteria.andOperateDateEqualTo(operateDate);

        List<LoanDO> loanDOs = loanMapper.selectByExample(example);

        return LoanConvert.convertDOsToDomains(loanDOs);
    }

    /**
     * @see LoanService#getPayProcessLoansBeforeOperateDate(Date)
     */
    @Override
    public List<LoanDomain> getPayProcessLoansBeforeOperateDate(Date operateDate) {
        LoanExample example = new LoanExample();
        LoanExample.Criteria criteria = example.createCriteria();

        criteria.andStateEqualTo(LoanStatusEnum.PAY_PROCESS.getCode());
        criteria.andBusinessTypeEqualTo(BusinessTypeEnum.LOAN.getCode());
        criteria.andOperateDateLessThan(operateDate);

        List<LoanDO> loanDOs = loanMapper.selectByExample(example);

        return LoanConvert.convertDOsToDomains(loanDOs);
    }

    /**
     * @see LoanService#getCurrentLoanRecordsById(Long)
     */
    @Override
    public List<LoanRecordDomain> getCurrentLoanRecordsById(Long loanId) {
        if (loanId == null) {
            return null;
        }
        List<LoanRecordDomain> currentLoanRecords =
                LoanRecordConvert.convert2Domains(loanRecordMapper.getCurrentLoanRecordsByLoanId(loanId));

        addRecordItemsToRecords(currentLoanRecords);

        return currentLoanRecords;
    }

    /**
     * @see LoanService#assignLoanById(Long, Long)
     */
    @Override
    public Boolean assignLoanById(Long loanId, Long accountId) {
        LoanDO loanDO = new LoanDO();
        LoanExample example = new LoanExample();
        loanDO.setAccountId(accountId);
        example.createCriteria().andIdEqualTo(loanId);
        return loanMapper.updateByExampleSelective(loanDO, example) > 0;
    }

    /**
     * @see LoanService#getRecentToPayRecord(Long)
     */
    @Override
    public LoanRecordDomain getRecentToPayRecord(Long loanId) {
        if (loanId == null) {
            return null;
        }

        LoanRecordExample loanRecordExample = new LoanRecordExample();
        loanRecordExample.createCriteria().
                andLoanIdEqualTo(loanId).
                andStateEqualTo(LoanStatusEnum.PAY_PROCESS.getCode());
        loanRecordExample.setOrderByClause("`order`, `dead_line`");
        List<LoanRecordDO> loanRecordDOs = loanRecordMapper.selectByExample(loanRecordExample);

        if (CollectionUtils.isNotEmpty(loanRecordDOs)) {
            return LoanRecordConvert.convert2Domain(loanRecordDOs.get(SystemConstants.FIRST_INDEX));
        }

        return null;
    }

    /**
     * @see LoanService#getLoanIdsByWorkspaceId(Long)
     */
    @Override
    public List<Long> getLoanIdsByWorkspaceId(Long workspaceId) {
        if (workspaceId == null) {
            return new ArrayList<>();
        }

        LoanExample example = new LoanExample();
        example.createCriteria()
                .andWorkspaceIdEqualTo(workspaceId);

        List<LoanDO> loanDOs = loanMapper.selectByExample(example);
        List<Long> loanIds = new ArrayList<>(loanDOs.size());

        loanDOs.forEach(loan -> loanIds.add(loan.getId()));

        return loanIds;
    }

    /**
     * @see LoanService#delete(List)
     */
    @Override
    public boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }

        LoanExample example = new LoanExample();

        example.createCriteria()
                .andIdIn(ids);

        LoanDO loanDO = new LoanDO();
        loanDO.setIsDelete(true);

        return loanMapper.updateByExampleSelective(loanDO, example) == ids.size();
    }

    /**
     * @see LoanService#countWorkspaceLoans(Long)
     */
    @Override
    public int countWorkspaceLoans(Long workspaceId) {
        LoanExample example = new LoanExample();
        example.createCriteria()
                .andWorkspaceIdEqualTo(workspaceId)
                .andKeyIdIsNotNull()
                .andBusinessTypeEqualTo(BusinessTypeEnum.LOAN.getCode());

        return loanMapper.countByExample(example);
    }

    /**
     * @see LoanService#countLoan(LoanQuery)
     */
    @Override
    public int countLoan(LoanQuery loanQuery) {
        if (loanQuery == null) {
            return 0;
        }

        loanQuery = setTrashStatus(loanQuery);

        return loanMapper.countLoans(loanQuery);
    }

    /**
     * @see LoanService#getWorkSpaceLoanIds(List)
     */
    @Override
    public List<AccountWorkspaceListDTO> getWorkSpaceLoanIds(List<Long> workspaceIds) {
        if (CollectionUtils.isEmpty(workspaceIds)) {
            return new ArrayList<>();
        }

        return loanMapper.getWorkspaceLoanIds(workspaceIds);
    }

    /**
     * @see LoanService#lightBatchReadAll(List)
     */
    @Override
    public List<LoanLightDomain> lightBatchReadAll(List<Long> loanIds) {
        if (CollectionUtils.isEmpty(loanIds)) {
            return new ArrayList<>();
        }
        List<LoanLightDTO> loanLightDTOs = loanMapper.lightSelectByPrimaryKeys(loanIds, false);
        return LoanLightConvert.convert2Domains(loanLightDTOs);
    }

    // ~ private methods
    private List<String> statusToStrArray(List<LoanStatusEnum> states) {
        if (CollectionUtils.isEmpty(states)) {
            return new ArrayList<>();
        }

        List<String> list = new ArrayList<>(states.size());
        for (LoanStatusEnum state : states) {
            list.add(state.getCode());
        }

        return list;
    }

    /**
     * 如果查询请求里限定贷款状态不为垃圾箱，则去除垃圾箱内项目
     *
     * @param query
     */
    private LoanQuery setTrashStatus(LoanQuery query) {
        // 如果查询请求里限定贷款状态不为垃圾箱，则去除垃圾箱内项目
        if (query.getLoanStatus() == null || !query.getLoanStatus().equals(LoanStatusEnum.TRASH.getCode())) {
            query.getExcludeStatus().add(LoanStatusEnum.TRASH.getCode());
        }

        return query;
    }
}
