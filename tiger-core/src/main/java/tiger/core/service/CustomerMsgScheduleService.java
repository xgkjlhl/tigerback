/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.enums.SmsStatusEnum;
import tiger.core.domain.CustomerMsgScheduleDomain;

/**
 * @author zhangbin
 * @version v0.1 2015/10/16 20:32
 */
public interface CustomerMsgScheduleService {

    /**
     * 更新编号为scheduleId的̬定时消息状态为state
     *
     * @param scheduleId
     * @param statusEnum
     * @return
     */
    boolean updateScheduleState(Long scheduleId, SmsStatusEnum statusEnum);

    /**
     * 获取编号为id的定时消息
     *
     * @param id
     * @return
     */
    CustomerMsgScheduleDomain getById(Long id);
}
