/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.kafka.consumer.config;

import tiger.common.dal.enums.MessageTypeEnum;

import java.util.HashSet;

/**
 * @author alfred_yuan
 * @version v 0.1 16:18 alfred_yuan Exp $
 */
public class ActivityConfig extends BaseConfig {

    public ActivityConfig() {
        supportSet = new HashSet<>();
        // 配置activity列表
        supportSet.add(MessageTypeEnum.LOAN_PAY_WITH_MONEY);
        supportSet.add(MessageTypeEnum.LOAN_CANCEL);
        supportSet.add(MessageTypeEnum.LOAN_CREATE);
        supportSet.add(MessageTypeEnum.LOAN_LAUNCH_WITH_MONEY);
        supportSet.add(MessageTypeEnum.LOAN_TEMPORARY_DELETE);
        supportSet.add(MessageTypeEnum.LOAN_PERMANENTLY_DELETE);
        supportSet.add(MessageTypeEnum.LOAN_RECOVERY);
        supportSet.add(MessageTypeEnum.LOAN_REFINE);
        supportSet.add(MessageTypeEnum.LOAN_TRANSFER);
        supportSet.add(MessageTypeEnum.ADD_MODIFICATION);
        supportSet.add(MessageTypeEnum.UPDATE_MODIFICATION);
        supportSet.add(MessageTypeEnum.DELETE_MODIFICATION);
        supportSet.add(MessageTypeEnum.LOAN_FINISH);
        supportSet.add(MessageTypeEnum.ADD_CUSTOMER);
        supportSet.add(MessageTypeEnum.DELETE_CUSTOMER);
        supportSet.add(MessageTypeEnum.UPDATE_CUSTOMER);
        supportSet.add(MessageTypeEnum.JOIN_WORKSPACE);
        supportSet.add(MessageTypeEnum.UPDATE_LOAN_SETTING);
        supportSet.add(MessageTypeEnum.UNFOLLOW_WORKSPACE);
    }

}
