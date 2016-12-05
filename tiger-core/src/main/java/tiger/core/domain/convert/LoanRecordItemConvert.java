/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.domain.convert;

import tiger.common.dal.dataobject.LoanRecordItemsDO;
import tiger.common.dal.dto.LoanRecordKeyIdDTO;
import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.JsonConverterUtil;
import tiger.common.util.JsonUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.LoanRecordItemDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alfred.yx
 * @version v 0.1 Sep 30, 2015 2:58:30 PM alfred Exp $
 */
public class LoanRecordItemConvert {

    private LoanRecordItemConvert() {
    }

    /**
     * Convert2 domain.
     *
     * @param source the source
     *
     * @return the loan record item domain
     */
    public static LoanRecordItemDomain convert2Domain(LoanRecordItemsDO source) {
        if (null == source) {
            return null;
        }
        LoanRecordItemDomain target = new LoanRecordItemDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        target.setItemType(LoanPayItemEnum.getEnumByCode(source.getType()));
        target.setItemModel(LoanPayItemModeEnum.getEnumByCode(source.getPayMode()));

        if (!StringUtil.isBlank(source.getExtParams())) {
            HashMap<String, String> extParams = JsonUtil.fromJson(source.getExtParams(), HashMap.class);
            //保留原domain中的extParams，排除前端对extParams的依赖后可删除
            target.setExtParams(extParams);
        }

        return target;
    }

    /**
     * Convert2 domains.
     *
     * @param sources the sources
     *
     * @return the list
     */
    public static List<LoanRecordItemDomain> convert2Domains(List<LoanRecordItemsDO> sources) {
        if (null == sources) {
            return new ArrayList<>();
        }
        List<LoanRecordItemDomain> target = new ArrayList<>();
        for (LoanRecordItemsDO source : sources) {
            target.add(convert2Domain(source));
        }
        return target;
    }

    /**
     * 转换财务对象为财务领域模型
     *
     * @param sources
     *
     * @return
     */
    public static List<LoanRecordItemDomain> convertToFiscalDomains(List<LoanRecordItemsDO> sources) {
        if (null == sources) {
            return new ArrayList<>();
        }
        List<LoanRecordItemDomain> target = new ArrayList<>();
        double balance = 0.0;
        for (LoanRecordItemsDO source : sources) {
            if (source.getAmount() == 0) {
                continue;
            }
            if (source.getPayMode().equals(LoanPayItemModeEnum.INCOME.getCode())) {
                balance += source.getAmount();

            } else if (source.getPayMode().equals(LoanPayItemModeEnum.OUTCOME.getCode())) {
                balance -= source.getAmount();
            }
            LoanRecordItemDomain domain = convert2Domain(source);
            domain.setBalance(balance);
            target.add(domain);
        }
        return target;
    }

    public static List<LoanRecordItemDomain> convertToFiscalDomains(List<LoanRecordItemsDO> sources, Map<Long, LoanRecordKeyIdDTO> keyIdMap) {
        if (null == sources) {
            return new ArrayList<>();
        }
        List<LoanRecordItemDomain> target = new ArrayList<>();
        for (LoanRecordItemsDO source : sources) {
            LoanRecordItemDomain domain = convert2Domain(source);
            if (keyIdMap.get(domain.getLoanRecordId()) != null) {
                domain.setKeyId(keyIdMap.get(domain.getLoanRecordId()).getKeyId());
                domain.setLoanId(keyIdMap.get(domain.getLoanRecordId()).getLoanId());
            }
            target.add(domain);
        }
        return target;
    }


    /**
     * Convert2 do.
     *
     * @param domain the domain
     *
     * @return the loan record items do
     */
    public static LoanRecordItemsDO convert2Do(LoanRecordItemDomain domain) {
        if (null == domain) {
            return null;
        }
        LoanRecordItemsDO loanRecordItemsDO = new LoanRecordItemsDO();
        BeanUtil.copyPropertiesWithIgnores(domain, loanRecordItemsDO);
        if (domain.getItemType() != null) {
            loanRecordItemsDO.setType(domain.getItemType().getCode());
        }
        if (domain.getItemModel() != null) {
            loanRecordItemsDO.setPayMode(domain.getItemModel().getCode());
        }
        if (domain.getExtParams() != null) {
            loanRecordItemsDO.setExtParams(JsonConverterUtil.serialize(domain.getExtParams()));
        }
        return loanRecordItemsDO;
    }

    /**
     * Convert2 do.
     *
     * @param domain the domain
     *
     * @return the loan record items do
     */
    public static LoanRecordItemsDO convert2Do(LoanRecordItemDomain domain, Long loanRecordId) {
        if (null == domain) {
            return null;
        }
        LoanRecordItemsDO loanRecordItemsDO = convert2Do(domain);
        if (loanRecordId != null) {
            loanRecordItemsDO.setLoanRecordId(loanRecordId);
        }
        if (domain.getExtParams() != null) {
            loanRecordItemsDO.setExtParams(JsonConverterUtil.serialize(domain.getExtParams()));
        }
        return loanRecordItemsDO;
    }

    /**
     * Convert2 dos.
     *
     * @param sources the sources
     *
     * @return the list
     */
    public static List<LoanRecordItemsDO> convert2Dos(List<LoanRecordItemDomain> sources) {
        if (null == sources) {
            return new ArrayList<>();
        }
        List<LoanRecordItemsDO> targets = new ArrayList<>();
        for (LoanRecordItemDomain domain : sources) {
            targets.add(convert2Do(domain));
        }
        return targets;
    }

    /**
     * Convert2 dos.
     *
     * @param sources      the sources
     * @param loanRecordId the loan record id
     *
     * @return the list
     */
    public static List<LoanRecordItemsDO> convert2Dos(List<LoanRecordItemDomain> sources,
                                                      Long loanRecordId) {
        if (null == sources) {
            return new ArrayList<>();
        }
        List<LoanRecordItemsDO> targets = new ArrayList<>();
        for (LoanRecordItemDomain domain : sources) {
            targets.add(convert2Do(domain, loanRecordId));
        }
        return targets;
    }
}
