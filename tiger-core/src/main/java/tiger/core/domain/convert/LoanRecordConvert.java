/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.LoanRecordDO;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.LoanRecordDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * 贷款履约记录转换器
 *
 * @author alfred.yx
 * @version v 0.1 Sep 30, 2015 2:03:36 PM alfred Exp $
 */
public class LoanRecordConvert {

    private LoanRecordConvert() {
    }

    /**
     * Convert2 domain.
     *
     * @param source the source
     * @return the loan record domain
     */
    public static LoanRecordDomain convert2Domain(LoanRecordDO source) {
        if (source == null) {
            return null;
        }
        LoanRecordDomain target = new LoanRecordDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        // 设置枚举和类型
        target.setState(LoanStatusEnum.getEnumByCode(source.getState()));
        target.setType(LoanActionEnum.getEnumByCode(source.getType()));
        target.setLoanPayItemModeEnum(LoanPayItemModeEnum.getEnumByCode(source.getPayMode()));
        if (source.getAmount() >= 0) {
            target.setLoanPayItemModeEnum(LoanPayItemModeEnum.INCOME);
        } else {
            target.setLoanPayItemModeEnum(LoanPayItemModeEnum.OUTCOME);
        }
        return target;
    }

    /**
     * Convert2 do.
     *
     * @param source the source
     * @return the loan record do
     */
    public static LoanRecordDO convert2DO(LoanRecordDomain source) {
        if (source == null) {
            return null;
        }
        LoanRecordDO target = new LoanRecordDO();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        // 设置枚举类型
        if (null != source.getState()) {
            target.setState(source.getState().getCode());
        }
        if (null != source.getType()) {
            target.setType(source.getType().getCode());
        }
        if (null != source.getLoanPayItemModeEnum()) {
            target.setPayMode(source.getLoanPayItemModeEnum().getCode());
        }
        return target;
    }

    /**
     * Convert2 domains.
     *
     * @param source the source
     * @return the list
     */
    public static List<LoanRecordDomain> convert2Domains(List<LoanRecordDO> source) {
        if (null == source) {
            return null;
        }
        List<LoanRecordDomain> target = new ArrayList<>();
        for (LoanRecordDO loanRecordDo : source) {
            target.add(convert2Domain(loanRecordDo));
        }
        return target;
    }


    /**
     * 还款所需要更新的字段
     *
     * @param source
     * @return
     */
    public static LoanRecordDO convert2PayDO(LoanRecordDomain source) {
        if (source == null || source.getId() == null) {
            return null;
        }
        LoanRecordDO target = new LoanRecordDO();
        target.setId(source.getId());
        target.setAmount(source.getAmount());
        target.setActualAmount(source.getAmount());
        target.setActualInterest(source.getActualInterest());
        target.setActualDate(source.getActualDate());
        return target;
    }

    /**
     * @param loanRecordDomains
     * @return
     */
    public static List<LoanRecordDO> convert2PayDOs(List<LoanRecordDomain> loanRecordDomains) {
        if (null == loanRecordDomains) {
            return null;
        }
        List<LoanRecordDO> recordDOs = new ArrayList<>();
        for (LoanRecordDomain loanRecordDomain : loanRecordDomains) {
            recordDOs.add(convert2PayDO(loanRecordDomain));
        }
        return recordDOs;
    }

}
