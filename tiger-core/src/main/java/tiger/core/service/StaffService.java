/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.query.StaffQuery;
import tiger.core.domain.StaffDomain;

import java.util.List;

/**
 * @author HuPeng
 * @version v 0.1 2015年10月19日 下午4:03:17 HuPeng Exp $
 */
public interface StaffService {

    /**
     * 列出符合query条件的管理员列表
     *
     * @param query
     * @return
     */
    List<StaffDomain> query(StaffQuery query);

    /**
     * 获取staff对象模型
     *
     * @param id
     * @return
     */
    StaffDomain read(long id);

    /**
     * 根据用户名获取staff对象模型
     *
     * @param username
     * @return
     */
    StaffDomain readByUsername(String username);

    /**
     * 更新staff信息
     *
     * @param staffDomain
     * @return
     */
    boolean update(StaffDomain staffDomain);

    /**
     * 创建一个管理员密码
     *
     * @param staffDomain
     * @return
     */
    boolean create(StaffDomain staffDomain);
}
