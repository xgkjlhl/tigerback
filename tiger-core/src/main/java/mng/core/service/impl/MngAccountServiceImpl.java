/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package mng.core.service.impl;

import mng.core.service.MngAccountService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dto.CountDTO;
import tiger.common.dal.persistence.LoanMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alfred_yuan
 * @version v 0.1 12:37 alfred_yuan Exp $
 */
@Service
public class MngAccountServiceImpl implements MngAccountService {
    @Autowired
    private LoanMapper loanMapper;

    @Override
    public Map<Long, Integer> accountLoanCounts(List<Long> accountIds) {
        if (CollectionUtils.isEmpty(accountIds)) {
            return new HashMap<>();
        }

        Map<Long, Integer> countMap = new HashMap<>(accountIds.size());
        List<CountDTO> countDTOs = loanMapper.countAccountLoans(accountIds);
        countDTOs.forEach(count -> countMap.put(count.getId(), count.getCount()));

        accountIds.forEach(accountId -> countMap.putIfAbsent(accountId, 0));

        return countMap;
    }
}
