/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package mng.core.service;

import java.util.List;
import java.util.Map;

/**
 * @author alfred_yuan
 * @version v 0.1 12:36 alfred_yuan Exp $
 */
public interface MngAccountService {

    /**
     * 获取account的贷款合同数量统计,返回格式为 accountId -> loanCount
     *
     * @param accountIds
     * @return
     */
    Map<Long, Integer> accountLoanCounts(List<Long> accountIds);

}
