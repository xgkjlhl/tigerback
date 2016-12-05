/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import org.springframework.util.CollectionUtils;
import tiger.common.dal.dataobject.LoanPawnDO;
import tiger.common.dal.dataobject.LoanPawnParamDO;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.LoanPawnDomain;
import tiger.core.domain.LoanPawnParamDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * 贷款抵押物模型转换器
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 4:28:14 PM yiliang.gyl Exp $
 */
public class LoanPawnConvert {

    private LoanPawnConvert() {
    }

    /**
     * Domain 转 DO
     *
     * @param domain the domain
     * @return the loan pawn do
     */
    public static LoanPawnDO convertToLoanPawnDO(LoanPawnDomain domain) {
        if (null != domain) {
            LoanPawnDO loanPawnDO = new LoanPawnDO();
            BeanUtil.copyPropertiesWithIgnores(domain, loanPawnDO);
            loanPawnDO.setType(domain.getLoanPawnTypeEnum().getCode());
            return loanPawnDO;
        } else {
            return null;
        }
    }

    /**
     * DO 转 Domain
     *
     * @param loanPawnDO the loan pawn do
     * @return the loan pawn domain
     */
    public static LoanPawnDomain convertToLoanPawnDomain(LoanPawnDO loanPawnDO) {
        if (null != loanPawnDO) {
            LoanPawnDomain loanPawnDomain = new LoanPawnDomain();
            BeanUtil.copyPropertiesWithIgnores(loanPawnDO, loanPawnDomain);
            loanPawnDomain.setLoanPawnTypeEnum(LoanPawnTypeEnum.getEnumByCode(loanPawnDO.getType()));
            return loanPawnDomain;
        } else {
            return null;
        }
    }

    /**
     * Convert to loan pawn param do.
     *
     * @param domain the domain
     * @return the loan pawn param do
     */
    public static LoanPawnParamDO convertToLoanPawnParamDO(LoanPawnParamDomain domain) {
        if (null != domain) {
            LoanPawnParamDO pawnParamDO = new LoanPawnParamDO();
            BeanUtil.copyPropertiesWithIgnores(domain, pawnParamDO);
            return pawnParamDO;
        } else {
            return null;
        }
    }

    /**
     * Conver to loan pawn param d os.
     *
     * @param domains the domains
     * @return the list
     */
    public static List<LoanPawnParamDO> convertToLoanPawnParamDOs(List<LoanPawnParamDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return null;
        }
        List<LoanPawnParamDO> list = new ArrayList<>(domains.size());
        domains.forEach(loanPawnParamDomain -> list.add(LoanPawnConvert.convertToLoanPawnParamDO(loanPawnParamDomain)));
        return list;
    }

    /**
     * Convert to loan pawn param domain.
     *
     * @param paramDo the param do
     * @return the loan pawn param domain
     */
    public static LoanPawnParamDomain convertToLoanPawnParamDomain(LoanPawnParamDO paramDo) {
        if (null != paramDo) {
            LoanPawnParamDomain pawnParamDomain = new LoanPawnParamDomain();
            BeanUtil.copyPropertiesWithIgnores(paramDo, pawnParamDomain);
            return pawnParamDomain;
        } else {
            return null;
        }
    }

    /**
     * Convert to loan pawn param domains.
     *
     * @param loanPawnParamDOs the loan pawn param d os
     * @return the list
     */
    public static List<LoanPawnParamDomain> convertToLoanPawnParamDomains(List<LoanPawnParamDO> loanPawnParamDOs) {
        if (CollectionUtils.isEmpty(loanPawnParamDOs)) {
            return null;
        }
        List<LoanPawnParamDomain> list = new ArrayList<>(loanPawnParamDOs.size());
        loanPawnParamDOs.forEach(loanPawnParamDO -> list.add(LoanPawnConvert.convertToLoanPawnParamDomain(loanPawnParamDO)));
        return list;
    }

}
