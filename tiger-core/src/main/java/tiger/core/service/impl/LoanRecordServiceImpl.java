/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tiger.common.dal.dataobject.LoanRecordDO;
import tiger.common.dal.dataobject.LoanRecordItemsDO;
import tiger.common.dal.dataobject.example.LoanRecordExample;
import tiger.common.dal.dataobject.example.LoanRecordItemsExample;
import tiger.common.dal.dto.LoanPaybackDTO;
import tiger.common.dal.dto.LoanRecordKeyIdDTO;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.dal.persistence.LoanRecordItemsMapper;
import tiger.common.dal.persistence.LoanRecordMapper;
import tiger.common.dal.query.LoanFiscalQuery;
import tiger.common.dal.query.LoanPaybackQuery;
import tiger.common.util.BeanUtil;
import tiger.common.util.DateUtil;
import tiger.common.util.Paginator;
import tiger.core.base.PageResult;
import tiger.core.domain.LoanFiscalListDomain;
import tiger.core.domain.LoanPaybackListDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRecordItemDomain;
import tiger.core.domain.convert.LoanPaybackListDomainConvert;
import tiger.core.domain.convert.LoanRecordConvert;
import tiger.core.domain.convert.LoanRecordItemConvert;
import tiger.core.service.LoanRecordService;
import tiger.core.service.LoanService;

import javax.transaction.Transactional;
import java.util.*;

/**
 * 款项记录记录服务类是吸纳
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 30, 2015 9:37:28 PM yiliang.gyl Exp $
 */
@Service
public class LoanRecordServiceImpl implements LoanRecordService {

    private final Logger logger = Logger.getLogger(LoanRecordServiceImpl.class);

    @Autowired
    private LoanRecordMapper loanRecordMapper;

    @Autowired
    private LoanRecordItemsMapper loanRecordItemsMapper;

    @Autowired
    private LoanService loanService;

    /**
     * @see LoanRecordService#update(LoanRecordDomain)
     */
    @Transactional
    @Override
    public LoanRecordDomain update(LoanRecordDomain loanRecordDomain) {
        if (loanRecordDomain == null || loanRecordDomain.getId() == null) {
            return null;
        }
        LoanRecordDO loanRecordDO = LoanRecordConvert.convert2DO(loanRecordDomain);
        loanRecordMapper.updateByPrimaryKeySelective(loanRecordDO);

        return updateRecordItem(loanRecordDO.getId(), loanRecordDomain.getRecordItemDomains());
    }

    /**
     * 更新贷款记录的熟知详情列表
     * 如果recordItemDomains为空,表示删除详情列表
     *
     * @see LoanRecordService#updateRecordItem(Long, List)
     */
    @Override
    public LoanRecordDomain updateRecordItem(Long recordId, List<LoanRecordItemDomain> recordItemDomains) {
        if (recordId == null || recordItemDomains == null) {
            return null;
        }

        //删除原有的详情列表
        LoanRecordItemsExample itemsExample = new LoanRecordItemsExample();
        itemsExample.createCriteria().andLoanRecordIdEqualTo(recordId);
        loanRecordItemsMapper.deleteByExample(itemsExample);

        //插入现有的item
        if (!CollectionUtils.isEmpty(recordItemDomains)) {
            //存储详情
            loanRecordItemsMapper.batchInsertItems(LoanRecordItemConvert
                    .convert2Dos(recordItemDomains, recordId));
        }
        return getLoanRecordById(recordId);
    }


    /**
     * @see tiger.core.service.LoanRecordService#getRecordsByPage(tiger.common.dal.query.LoanPaybackQuery)
     */
    @Override
    public PageResult<List<LoanPaybackListDomain>> getRecordsByPage(LoanPaybackQuery query) {

        PageResult<List<LoanPaybackListDomain>> result = new PageResult<>();

        query = setOverdueDate(query);

        int count = loanRecordMapper.countPaybackItems(query);

        if (logger.isInfoEnabled()) {
            logger.info("查询条件[" + query + "], 查询到总共 [" + count + "]个对象");
        }

        //如果是智能排序，重排
        if (query.getSmartSort() && query.getOverdue() == null) {
            //获取逾期日期所在的页码
            Date overdueDate = DateUtil.addDays(new Date(), -query.getOverdueDay());
            int smartCount = loanRecordMapper.countPaybackItemsOverDate(query, overdueDate);
            logger.info("获取到智能重排count :[" + smartCount + "]");
            //计算智能重排过后的页数
            if (smartCount > query.getPageSize()) {
                int page = smartCount / query.getPageSize() + 1;
                query.setPageNum(page);
            }
        }

        Paginator paginator = new Paginator();
        paginator.setItems(count);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum());

        result.setPaginator(paginator);

        List<LoanPaybackDTO> dtos = loanRecordMapper.getPaybackItemsByPage(query,
                paginator.getOffset(), paginator.getItemsPerPage());
        //转换成为列表需要的模型
        result.setData(LoanPaybackListDomainConvert.convertToDomains(dtos));
        return result;
    }

    /**
     * @see LoanRecordService#getAllRecords(LoanPaybackQuery)
     */
    @Override
    public List<LoanPaybackListDomain> getAllRecords(LoanPaybackQuery query) {
        query = setOverdueDate(query);

        if (logger.isInfoEnabled()) {
            logger.info("查询所有项目的近期还款列表, 参数为 [" + query + "]");
        }

        return LoanPaybackListDomainConvert.convertToDomains(
                loanRecordMapper.getAllPaybackItems(query)
        );
    }

    /**
     * @see LoanRecordService#countRecords(LoanPaybackQuery)
     */
    @Override
    public Integer countRecords(LoanPaybackQuery query) {
        return loanRecordMapper.countPaybackItems(query);
    }

    /**
     * @see tiger.core.service.LoanRecordService#getLoanRecordById(long)
     */
    @Override
    public LoanRecordDomain getLoanRecordById(long recordId) {
        LoanRecordDomain recordDomain = LoanRecordConvert
                .convert2Domain(loanRecordMapper.selectByPrimaryKey(recordId));
        if (recordDomain != null) {
            ArrayList<Long> ids = new ArrayList<>();
            ids.add(recordDomain.getId());
            recordDomain.setRecordItemDomains(LoanRecordItemConvert
                    .convert2Domains(loanRecordItemsMapper.getRecordItemsByRecordIds(ids, null)));
        }
        return recordDomain;
    }
    public List<LoanRecordDO> getLoanRecordByLoanIdAndStatus(long loanId){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id",loanId);
        map.put("state",LoanStatusEnum.WAIT_LAUNCH.getCode());
        return loanRecordMapper.getLoanRecordsByLoanIdAndStatus(map);
    }

    /**
     * @see LoanRecordService#getLoanRecordsByIds(List)
     */
    @Override
    public List<LoanRecordDomain> getLoanRecordsByIds(List<Long> recordIds) {
        if (recordIds.size() <= 0) {
            return new ArrayList<>();
        }
        List<LoanRecordDomain> domains = LoanRecordConvert.convert2Domains(loanRecordMapper.getLoanRecordsByIds(recordIds));
        //添加详情
        loanService.addRecordItemsToRecords(domains);
        return domains;
    }

    /**
     * @see LoanRecordService#getFiscalDetailsList(LoanFiscalQuery)
     */
    @Override
    public LoanFiscalListDomain getFiscalDetailsList(LoanFiscalQuery loanFiscalQuery) {
        LoanFiscalListDomain loanFiscalListDomain = new LoanFiscalListDomain();
        if (CollectionUtils.isEmpty(loanFiscalQuery.getInComeTypes()) && CollectionUtils.isEmpty(loanFiscalQuery.getOutComeTypes())) {
            loanFiscalListDomain.setAmount(0.0);
            return loanFiscalListDomain;
        }

        //由于财务分析页面显示的时间不一定是deadline，所以去除query中的时间，后续再筛选
        Date beginDate = loanFiscalQuery.getBeginDate();
        Date endDate = loanFiscalQuery.getEndDate();
        loanFiscalQuery.setBeginDate(null);
        loanFiscalQuery.setEndDate(null);

        //1. 按照条件查询所有符合条件的 records
        LoanPaybackQuery loanPaybackQuery = new LoanPaybackQuery();
        loanPaybackQuery.getExcludeStates().add(LoanStatusEnum.TRASH.getCode());
        BeanUtil.copyPropertiesWithIgnores(loanFiscalQuery, loanPaybackQuery);
        List<LoanRecordDO> records = loanRecordMapper.getRecordsByQuery(loanPaybackQuery);


        List<Long> recordIds = new ArrayList<>(); // 需要列出财务数据的records
        Map<Long, Boolean> recordHappenedMap = new HashMap<>(); //已发生的数据
        Map<Long, Date> recordTimeMap = new HashMap<>(); //记录发生时间

        for (LoanRecordDO record : records) {
            try {
                if (LoanStatusEnum.getEnumByCode(record.getState()).equals(LoanStatusEnum.FINISH)) {
                    recordHappenedMap.put(record.getId(), true);
                    recordTimeMap.put(record.getId(), record.getActualDate());
                } else {
                    recordHappenedMap.put(record.getId(), false);
                    recordTimeMap.put(record.getId(), record.getDeadLine());
                }
                //只比较年月日
                String updateTimeStr = DateUtil.getDateString(recordTimeMap.get(record.getId()));
                //如果时间不在指定范围内，则结束这次循环
                if (beginDate != null) {
                    if (updateTimeStr.compareTo(DateUtil.getDateString(beginDate)) < 0)
                        continue;
                }
                if (endDate != null) {
                    if (updateTimeStr.compareTo(DateUtil.getDateString(endDate)) > 0)
                        continue;
                }
                recordIds.add(record.getId());
            } catch (Exception e) {
                logger.error("无法获取财务数据:[" + record + "]");
            }
        }

        //如果没有相应的record_id，则返回
        if (CollectionUtils.isEmpty(recordIds)) {
            loanFiscalListDomain.setAmount(0.0);
            return loanFiscalListDomain;
        }

        //2. 获取所有的财务条目
        List<LoanRecordItemsDO> itemsDOs = loanRecordItemsMapper.getRecordItemsByRecordIds(recordIds, loanFiscalQuery);

        //获取包含keyId 和 loanRecordId 的一组Map
        List<LoanRecordKeyIdDTO> keyIdMaps = loanRecordMapper.getLoanKeyIdsByRecordIds(recordIds);
        Map<Long, LoanRecordKeyIdDTO> keyIdMap = new HashMap<>();
        for (LoanRecordKeyIdDTO keyIdDTO : keyIdMaps) {
            keyIdMap.put(keyIdDTO.getLoanRecordId(), keyIdDTO);
        }

        //3. 计算财务条目的金额
        Double outcomeAmount = loanRecordItemsMapper.sumRecordItemsByRecordIdsAndMode(recordIds, loanFiscalQuery,
                LoanPayItemModeEnum.OUTCOME.getCode());
        Double incomeAmount = loanRecordItemsMapper.sumRecordItemsByRecordIdsAndMode(recordIds, loanFiscalQuery,
                LoanPayItemModeEnum.INCOME.getCode());

        List<LoanRecordItemDomain> list = LoanRecordItemConvert.convertToFiscalDomains(itemsDOs, keyIdMap);

        //去除account是0的统计项 & 设置兑付状态和时间
        Iterator<LoanRecordItemDomain> iter = list.iterator();
        while (iter.hasNext()) {
            LoanRecordItemDomain domain = iter.next();
            if (domain.getAmount() == 0) {
                iter.remove();
                continue;
            }
            domain.setHappened(recordHappenedMap.get(domain.getLoanRecordId()));
            domain.setUpdateTime(recordTimeMap.get(domain.getLoanRecordId()));
        }

        //排序
        sortRecordItems(list);
        //计算Balance
        calculateBalance(list);
        //修改额外参数的显示的值
        //replaceDisplayItemTypeValue(list);

        loanFiscalListDomain.setList(list);
        loanFiscalListDomain.sumAmount(incomeAmount, outcomeAmount);

        return loanFiscalListDomain;
    }

    private void replaceDisplayItemTypeValue(List<LoanRecordItemDomain> list){
        if(list!=null){
            for (int i=0;i<list.size();i++) {
                LoanRecordItemDomain itemDomain=list.get(i);
                Map<String,String> map=itemDomain.getExtParams();
                if(map!=null&&!map.isEmpty()){
                  String key= map.get("name");
                    itemDomain.getItemType().setValue(key);
                }
            }
        }

    }

    /**
     * @see LoanRecordService#getLoanRecordsByIds(List)
     */
    @Override
    public List<LoanRecordDomain> getLoanRecordsByLoanIds(List<Long> loanIds) {
        if (CollectionUtils.isEmpty(loanIds)) {
            return new ArrayList<>();
        }
        return LoanRecordConvert.convert2Domains(loanRecordMapper.getLoanRecordsByLoanIdsAndExcludeTypes(loanIds,
                new ArrayList<>()));
    }

    /**
     * @see LoanRecordService#getLoanRecordsByLoanIdsAndExcludeTypes(List, List)
     */
    @Override
    public List<LoanRecordDomain> getLoanRecordsByLoanIdsAndExcludeTypes(List<Long> loanIds,
                                                                         List<LoanActionEnum> actionEnums) {
        if (CollectionUtils.isEmpty(loanIds)) {
            return null;
        }
        List<String> excludeTypes = new ArrayList<>();
        for (LoanActionEnum actionEnum : actionEnums) {
            excludeTypes.add(actionEnum.getCode());
        }
        return LoanRecordConvert.convert2Domains(loanRecordMapper.getLoanRecordsByLoanIdsAndExcludeTypes(loanIds,
                excludeTypes));

    }

    /**
     * @see LoanRecordService#trashLoanRecordsByLoanIds(List)
     */
    @Override
    public Boolean trashLoanRecordsByLoanIds(List<Long> loanIds) {
        if (CollectionUtils.isEmpty(loanIds)) {
            return true;
        }
        // 如果没有对应的还款记录也返回成功，因为不需要修改
        if (CollectionUtils.isEmpty(getLoanRecordsByLoanIds(loanIds))) return true;
        return loanRecordMapper.trashLoanRecordsByLoanIds(loanIds) > 0;
    }

    /**
     * @see LoanRecordService#recoverLoanRecordsByLoanIds(List)
     */
    @Override
    public Boolean recoverLoanRecordsByLoanIds(List<Long> loanIds) {
        if (CollectionUtils.isEmpty(loanIds)) {
            return true;
        }
        // 如果没有对应的还款记录也返回成功，因为不需要修改
        if (CollectionUtils.isEmpty(getLoanRecordsByLoanIds(loanIds))) return true;
        return loanRecordMapper.recoverLoanRecordsByLoanIds(loanIds) > 0;
    }

    /**
     * @see LoanRecordService#deleteLoanRecordsByIds(List)
     */
    @Override
    public Boolean deleteLoanRecordsByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        LoanRecordExample deleteExample = new LoanRecordExample();
        deleteExample.createCriteria()
                .andIdIn(ids);

        return loanRecordMapper.deleteByExample(deleteExample) > 0;
    }

    /**
     * @see LoanRecordService#deleteLoanRecordItemsByLoanRecordIds(List)
     */
    @Override
    public Boolean deleteLoanRecordItemsByLoanRecordIds(List<Long> loanRecordIds) {
        if (CollectionUtils.isEmpty(loanRecordIds)) {
            return true;
        }
        LoanRecordItemsExample deleteExample = new LoanRecordItemsExample();
        deleteExample.createCriteria()
                .andLoanRecordIdIn(loanRecordIds);

        return loanRecordItemsMapper.deleteByExample(deleteExample) > 0;
    }

    /**
     * @see LoanRecordService#updateLoanRecordsByIds(LoanRecordDomain, List)
     */
    @Override
    public boolean updateLoanRecordsByIds(LoanRecordDomain target, List<Long> ids) {
        if (target == null) {
            return false;
        }
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }

        LoanRecordDO recordDO = LoanRecordConvert.convert2DO(target);
        LoanRecordExample updateExample = new LoanRecordExample();
        updateExample.createCriteria().andIdIn(ids);

        return loanRecordMapper.updateByExampleSelective(recordDO, updateExample) > 0;
    }

    /**
     * @see LoanRecordService#revertLoanRecord(LoanRecordDomain)
     */
    @Override
    public LoanRecordDomain revertLoanRecord(LoanRecordDomain loanRecordDomain) {
        if (loanRecordDomain == null || loanRecordDomain.getId() == null) {
            return null;
        }

        LoanRecordDO loanRecordDO = LoanRecordConvert.convert2DO(loanRecordDomain);
        loanRecordMapper.revertLoanRecord(loanRecordDO);

        return updateRecordItem(loanRecordDO.getId(), loanRecordDomain.getRecordItemDomains());
    }

    //~ private method

    /**
     * 排序
     * ~ 1. updateTime
     * ~ 2. recordId
     *
     * @param list
     */
    private void sortRecordItems(List<LoanRecordItemDomain> list) {
        Comparator mycmp = ComparableComparator.getInstance();
        mycmp = ComparatorUtils.nullLowComparator(mycmp);//允许null
//        mycmp = ComparatorUtils.reversedComparator(mycmp);//逆序
        ArrayList<Object> sortFields = new ArrayList<Object>();
        sortFields.add(new BeanComparator("updateTime", mycmp));
        sortFields.add(new BeanComparator("loanRecordId", mycmp));
        ComparatorChain multiSort = new ComparatorChain(sortFields);
        Collections.sort(list, multiSort);
    }

    /**
     * 计算结存
     *
     * @param list
     */
    private void calculateBalance(List<LoanRecordItemDomain> list) {
        double balance = 0.0;
        for (LoanRecordItemDomain domain : list) {
            if (domain.getAmount() == 0)
                continue;
            if (domain.getItemModel().equals(LoanPayItemModeEnum.INCOME))
                balance += domain.getAmount();
            else if (domain.getItemModel().equals(LoanPayItemModeEnum.OUTCOME))
                balance -= domain.getAmount();
            domain.setBalance(balance);
        }
    }

    /**
     * 设置query的overDueDate
     *
     * @param query
     * @return
     */
    private LoanPaybackQuery setOverdueDate(LoanPaybackQuery query) {
        if (query.getOverdue() != null) {
            if (query.getOverdue()) {
                query.setEndDate(DateUtil.getDayBegin(new Date()));
            } else {
                query.setBeginDate(DateUtil.getDayBegin(new Date()));
            }
        }
        return query;
    }
}
