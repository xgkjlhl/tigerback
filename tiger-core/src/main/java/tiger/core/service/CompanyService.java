/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.query.CompanyQuery;
import tiger.core.domain.CompanyDomain;

import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/9/19 9:42
 */
public interface CompanyService {
    /**
     * 通过id获取公司
     * @param id
     * @return
     */
    CompanyDomain getCompanyById(Long id);

    /**
     * 添加公司信息
     *
     * @param company
     * @return
     */
    boolean addCompany(CompanyDomain company);

    /**
     * 更新公司信息
     *
     * @param domain
     * @return
     */
    boolean updateCompany(CompanyDomain domain);

    /**
     * 查询公司信息
     * @param query
     * @return
     */
    List<CompanyDomain> query(CompanyQuery query);
}
