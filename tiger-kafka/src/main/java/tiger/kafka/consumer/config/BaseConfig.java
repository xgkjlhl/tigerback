/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.kafka.consumer.config;

import org.apache.commons.collections.CollectionUtils;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.CustomerDomain;
import tiger.core.domain.LoanLightDomain;
import tiger.core.domain.workspace.WorkspaceDomain;

import java.util.Map;
import java.util.Set;

/**
 * @author alfred_yuan
 * @version v 0.1 16:19 alfred_yuan Exp $
 */
public abstract class BaseConfig {

    protected Set<MessageTypeEnum> supportSet;

    /**
     * 是否支持typeEnum
     *
     * @param typeEnum
     * @return
     */
    public boolean isSupport(MessageTypeEnum typeEnum) {
        if (CollectionUtils.isEmpty(supportSet)) {
            return false;
        }

        return supportSet.contains(typeEnum);
    }
}
