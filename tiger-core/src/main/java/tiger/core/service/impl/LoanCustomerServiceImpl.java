/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.LoanCustomerDO;
import tiger.common.dal.dataobject.example.LoanCustomerExample;
import tiger.common.dal.dto.CountDTO;
import tiger.common.dal.enums.LoanCustomerTypeEnum;
import tiger.common.dal.persistence.LoanCustomerMapper;
import tiger.core.service.LoanCustomerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 4:40 PM yiliang.gyl Exp $
 */
@Service
public class LoanCustomerServiceImpl implements LoanCustomerService {

    @Autowired
    private LoanCustomerMapper loanCustomerMapper;

    /**
     * @see LoanCustomerService#getLoanCustomerIds(Long)
     */
    @Override
    public List<Long> getLoanCustomerIds(Long loanId) {
        if (loanId == null) {
            return new ArrayList<>();
        }

        LoanCustomerExample example = new LoanCustomerExample();
        example.createCriteria().andLoanIdEqualTo(loanId);
        List<LoanCustomerDO> loanCustomerDOs = loanCustomerMapper.selectByExample(example);

        List<Long> ids = new ArrayList<>();

        for (LoanCustomerDO customerDO : loanCustomerDOs) {
            ids.add(customerDO.getCustomerId());
        }

        return ids;
    }

    /**
     * @see LoanCustomerService#getLoansCustomerIds(List)
     */
    @Override
    public List<Long> getLoansCustomerIds(List<Long> loanIds) {
        if (CollectionUtils.isEmpty(loanIds))
            return null;
        LoanCustomerExample example = new LoanCustomerExample();
        example.createCriteria().andLoanIdIn(loanIds);
        List<LoanCustomerDO> loanCustomerDOs = loanCustomerMapper.selectByExample(example);

        List<Long> ids = new ArrayList<>();

        for (LoanCustomerDO customerDO : loanCustomerDOs) {
            if (!ids.contains(customerDO.getCustomerId()))
                ids.add(customerDO.getCustomerId());
        }

        return ids;
    }

    /**
     * @see LoanCustomerService#countCustomerByLoanType(Long, LoanCustomerTypeEnum)
     */
    @Override
    public int countCustomerByLoanType(Long loanId, LoanCustomerTypeEnum customerType) {
        if (loanId == null || customerType == null) {
            return 0;
        }

        LoanCustomerExample example = generateLoanCustomerExample(loanId, customerType);
        return loanCustomerMapper.countByExample(example);
    }

    /**
     * @see LoanCustomerService#deleteCustomerByLoanId(Long)
     */
    @Override
    public boolean deleteCustomerByLoanId(Long loanId) {
        if (loanId == null) {
            return true;
        }

        LoanCustomerExample example = new LoanCustomerExample();
        example.createCriteria().andLoanIdEqualTo(loanId);
        return loanCustomerMapper.deleteByExample(example) > 0;
    }

    /**
     * @see LoanCustomerService#deleteCustomerByLoanIds(List)
     */
    @Override
    public boolean deleteCustomerByLoanIds(List<Long> loanIds) {
        if (CollectionUtils.isEmpty(loanIds)) {
            return true;
        }

        LoanCustomerExample example = new LoanCustomerExample();
        example.createCriteria()
                .andLoanIdIn(loanIds);

        return loanCustomerMapper.deleteByExample(example) == loanIds.size();
    }

    /**
     * @see LoanCustomerService#deleteCustomerByLoanType(Long, LoanCustomerTypeEnum)
     */
    @Override
    public boolean deleteCustomerByLoanType(Long loanId, LoanCustomerTypeEnum customerType) {
        if (loanId == null || customerType == null) {
            return true;
        }

        LoanCustomerExample example = generateLoanCustomerExample(loanId, customerType);
        return loanCustomerMapper.deleteByExample(example) > 0;
    }

    /**
     * @see LoanCustomerService#countCustomersByType(List, LoanCustomerTypeEnum)
     */
    @Override
    public Map<Long, Integer> countCustomersByType(List<Long> customerIds, LoanCustomerTypeEnum type) {
        // 如果参数非法,提前结束
        if (CollectionUtils.isEmpty(customerIds) || type == null) {
            return new HashedMap();
        }

        List<CountDTO> customerCount = loanCustomerMapper.countCustomersByType(customerIds, type);
        HashMap<Long, Integer> customerCountMap = new HashMap<>(customerIds.size());
        // 设置默认值0
        customerIds.forEach(customerId -> customerCountMap.put(customerId, 0));
        // 设置查询结果
        customerCount.forEach(customerDTO -> customerCountMap.put(customerDTO.getId(), customerDTO.getCount()));

        return customerCountMap;
    }

    /**
     * 根据贷款合同编号 和 客户类型生成对应的 LoanCustomerExample
     *
     * @param loanId
     * @param customerType
     * @return
     */
    private LoanCustomerExample generateLoanCustomerExample(Long loanId, LoanCustomerTypeEnum customerType) {
        LoanCustomerExample example = new LoanCustomerExample();
        example.createCriteria().
                andLoanIdEqualTo(loanId).
                andTypeEqualTo(customerType.getCode());
        return example;
    }
}
