/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.LoanRefineDO;
import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.JsonConverterUtil;
import tiger.common.util.JsonUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanRefineDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author alfred_yuan
 * @version ${ID}: v 0.1 20:33 alfred_yuan Exp $
 */
public class LoanRefineConvert {

    private LoanRefineConvert() {
    }

    public static LoanRefineDO convertToDO(LoanRefineDomain source) {
        if (source == null) {
            return null;
        }
        LoanRefineDO target = new LoanRefineDO();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        if (source.getPayWay() != null) {
            target.setPayWay(source.getPayWay().getCode());
        }
        if (null != source.getExtParams()) {
            target.setExtParams(JsonConverterUtil.serialize(source.getExtParams()));
        }
        return target;
    }

    public static LoanRefineDomain convertToDomain(LoanRefineDO source) {
        if (source == null) {
            return null;
        }
        LoanRefineDomain target = new LoanRefineDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        target.setPayWay(LoanPayWayEnum.getEnumByCode(source.getPayWay()));
        if (!StringUtil.isBlank(source.getExtParams())) {
            target.setExtParams(JsonUtil.fromJson(source.getExtParams(), HashMap.class));
        }
        return target;
    }

    public static List<LoanRefineDomain> convertToDomains(List<LoanRefineDO> refineDOs) {
        List<LoanRefineDomain> refineDomains = new ArrayList<>();

        for (LoanRefineDO source : refineDOs) {
            refineDomains.add(convertToDomain(source));
        }

        return refineDomains;
    }

    public static LoanRefineDomain convertLoanDomainToLoanRefineDomain(LoanDomain source) {
        LoanRefineDomain loanRefineDomain = new LoanRefineDomain();

        BeanUtil.copyPropertiesWithIgnores(source, loanRefineDomain);
        loanRefineDomain.setPayWay(source.getPayWay());
        loanRefineDomain.setExtParams(source.getExtParams());
        loanRefineDomain.setLoanId(source.getId());

        return loanRefineDomain;
    }
}
