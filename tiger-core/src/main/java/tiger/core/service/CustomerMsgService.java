/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.enums.CustomerMsgBizTypeEnum;
import tiger.common.dal.enums.SmsStatusEnum;
import tiger.core.domain.CustomerMsgDomain;

import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/10/16 18:10
 */
public interface CustomerMsgService {

    /**
     * 通过id获取消息
     *
     * @param id
     * @return
     */
    CustomerMsgDomain getCustomerMsgById(Long id);

    /**
     * 修改消息状态
     *
     * @param id
     * @param state
     * @return boolean
     */
    boolean updateMsgState(Long id, String state);

    /**
     * 插入一条数据
     *
     * @param customerMsgDomain
     * @return
     */
    boolean insert(CustomerMsgDomain customerMsgDomain);

    /**
     * 根据短信的外部id和业务类型查询短信列表
     *
     * @param outBizId
     * @param bizTypeEnum
     * @return
     */
    List<CustomerMsgDomain> findByOutBizIdAndType(String outBizId, CustomerMsgBizTypeEnum bizTypeEnum);

}
