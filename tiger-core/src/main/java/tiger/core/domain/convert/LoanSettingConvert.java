/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.LoanSettingDO;
import tiger.common.dal.enums.LoanSettingTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.ByteUtil;
import tiger.core.domain.LoanSettingDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 9:22 PM yiliang.gyl Exp $
 */
public class LoanSettingConvert {

    private LoanSettingConvert() {
    }

    /**
     * Convert to Do
     *
     * @param source
     * @return
     */
    public static LoanSettingDO convert2Do(LoanSettingDomain source) {
        if (source == null) {
            return null;
        }
        LoanSettingDO target = new LoanSettingDO();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        target.setSettingName(source.getName());
        target.setSettingValue(source.getValue());
        target.setSettingType(source.getLoanSettingTypeEnum().getCode());
        target.setIsActive(ByteUtil.getAsBytes(source.getIsActive())[0]);
        return target;
    }

    /**
     * Convert to Do
     *
     * @param source
     * @return
     */
    public static List<LoanSettingDO> convert2Dos(List<LoanSettingDomain> source) {
        List<LoanSettingDO> targets = new ArrayList<>();
        for (LoanSettingDomain settingDomain : source) {
            targets.add(convert2Do(settingDomain));
        }
        return targets;
    }


    /**
     * Convert to Domain
     *
     * @param source
     * @return
     */
    public static LoanSettingDomain convert2Domain(LoanSettingDO source) {
        if (source == null) {
            return null;
        }
        LoanSettingDomain target = new LoanSettingDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        target.setName(source.getSettingName());
        target.setValue(source.getSettingValue());
        target.setLoanSettingTypeEnum(LoanSettingTypeEnum.getEnumByCode(source.getSettingType()));
        target.setIsActive(ByteUtil.toBoolean(source.getIsActive()));
        return target;
    }

    /**
     * Convert to Domains
     *
     * @param source
     * @return
     */
    public static List<LoanSettingDomain> convert2Domains(List<LoanSettingDO> source) {
        List<LoanSettingDomain> targets = new ArrayList<>();
        for (LoanSettingDO settingDO : source) {
            targets.add(convert2Domain(settingDO));
        }
        return targets;
    }

}
