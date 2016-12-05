/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.CustomerMsgScheduleDO;
import tiger.common.dal.enums.SmsStatusEnum;
import tiger.common.dal.persistence.CustomerMsgScheduleMapper;
import tiger.common.util.BeanUtil;
import tiger.core.domain.CustomerMsgScheduleDomain;
import tiger.core.service.CustomerMsgScheduleService;

/**
 * @author zhangbin
 * @version v0.1 2015/10/16 20:32
 */
@Service
public class CustomerMsgScheduleServiceImpl implements CustomerMsgScheduleService {

    @Autowired
    CustomerMsgScheduleMapper mapper;

    @Override
    public boolean updateScheduleState(Long scheduleId, SmsStatusEnum statusEnum) {

        CustomerMsgScheduleDO scheduleDO = new CustomerMsgScheduleDO();
        scheduleDO.setId(scheduleId);
        scheduleDO.setStatus(statusEnum.getCode());

        int count = mapper.updateByPrimaryKeySelective(scheduleDO);

        return count == 1;
    }

    @Override
    public CustomerMsgScheduleDomain getById(Long id) {
        CustomerMsgScheduleDO msgScheduleDO = mapper.selectByPrimaryKey(id);
        CustomerMsgScheduleDomain domain = new CustomerMsgScheduleDomain();

        BeanUtil.copyPropertiesWithIgnores(msgScheduleDO, domain);
        return domain;
    }
}
