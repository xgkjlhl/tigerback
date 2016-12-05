/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.query.AccountSettingQuery;
import tiger.core.domain.AccountSettingDomain;
import tiger.core.domain.listDomain.AccountSettingListDomain;

import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 19:38 alfred_yuan Exp $
 */
public interface AccountSettingService {

    /**
     * 增加一个账户设置
     *
     * @param accountSettingDomain
     * @return
     */
    boolean add(AccountSettingDomain accountSettingDomain);

    /**
     * 删除一个账户设置
     *
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 根据id更新一个账户设置
     *
     * @param accountSettingDomain
     * @return
     */
    boolean update(AccountSettingDomain accountSettingDomain);

    /**
     * 查询账户设置
     *
     * @param settingQuery
     * @return
     */
    List<AccountSettingListDomain> query(AccountSettingQuery settingQuery);

}
