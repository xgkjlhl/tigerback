/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.domain.convert;

import org.apache.commons.collections.CollectionUtils;
import tiger.common.dal.dataobject.AccountSettingDO;
import tiger.common.dal.dto.list.AccountSettingListDTO;
import tiger.common.dal.enums.AccountSettingBizTypeEnum;
import tiger.common.dal.enums.AccountSettingTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.AccountSettingDomain;
import tiger.core.domain.listDomain.AccountSettingListDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 19:18 alfred_yuan Exp $
 */
public class AccountSettingConvert {

    private AccountSettingConvert() {
    }

    public static AccountSettingDomain convert2Domain(AccountSettingDO source) {
        if (source == null) {
            return null;
        }

        AccountSettingDomain target = new AccountSettingDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);

        target.setBizType(AccountSettingBizTypeEnum.getEnumByCode(source.getBizType()));
        target.setSettingType(AccountSettingTypeEnum.getEnumByCode(source.getSettingType()));

        return target;
    }

    public static List<AccountSettingDomain> convert2Domains(List<AccountSettingDO> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }

        List<AccountSettingDomain> targets = new ArrayList<>(sources.size());

        sources.forEach(source -> targets.add(convert2Domain(source)));

        return targets;
    }

    public static AccountSettingDO convert2DO(AccountSettingDomain source) {
        if (source == null) {
            return null;
        }

        AccountSettingDO target = new AccountSettingDO();
        BeanUtil.copyPropertiesWithIgnores(source, target);

        if (source.getBizType() != null) {
            target.setBizType(source.getBizType().getCode());
        }
        if (source.getSettingType() != null) {
            target.setSettingType(source.getSettingType().getCode());
        }

        return target;
    }

    public static List<AccountSettingListDomain> convert2ListDomains(List<AccountSettingListDTO> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }

        final List<AccountSettingListDomain> targets = new ArrayList<>(sources.size());

        sources.forEach(source -> {
            AccountSettingListDomain accountSettingListDomain = new AccountSettingListDomain();
            accountSettingListDomain.setId(source.getId());
            accountSettingListDomain.setBizType(AccountSettingBizTypeEnum.getEnumByCode(source.getBizType()));
            accountSettingListDomain.setSettingType(AccountSettingTypeEnum.getEnumByCode(source.getSettingType()));
            accountSettingListDomain.setSettingValue(source.getSettingValue());
            targets.add(accountSettingListDomain);
        });

        return targets;
    }
}
