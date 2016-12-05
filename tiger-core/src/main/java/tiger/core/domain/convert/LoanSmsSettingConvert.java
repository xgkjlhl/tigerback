/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.enums.LoanSettingTypeEnum;
import tiger.core.constants.LoanConstants;
import tiger.core.domain.LoanSettingDomain;
import tiger.core.domain.LoanSmsSettingDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 10:32 PM yiliang.gyl Exp $
 */
public class LoanSmsSettingConvert {

    private LoanSmsSettingConvert() {
    }

    public static LoanSettingDomain convertToSettingDomain(LoanSmsSettingDomain smsSettingDomain) {
        LoanSettingDomain settingDomain = new LoanSettingDomain();
        settingDomain.setName(LoanConstants.LOAN_SMS_DUE_DAY);
        settingDomain.setValue(smsSettingDomain.getDay().toString());
        settingDomain.setLoanSettingTypeEnum(LoanSettingTypeEnum.SMS_SETTING);
        settingDomain.setIsActive(smsSettingDomain.getOpen());
        settingDomain.setLoanId(smsSettingDomain.getLoanId());
        return settingDomain;
    }

    public static List<LoanSettingDomain> convertToSettingDomains(List<LoanSmsSettingDomain> source) {
        List<LoanSettingDomain> targets = new ArrayList<>();
        for (LoanSmsSettingDomain smsSettingDomain : source) {
            targets.add(convertToSettingDomain(smsSettingDomain));
        }
        return targets;
    }


    public static LoanSmsSettingDomain convertToSmsSettingDomain(LoanSettingDomain loanSettingDomain) {
        LoanSmsSettingDomain smsSettingDomain = new LoanSmsSettingDomain();
        smsSettingDomain.setLoanId(loanSettingDomain.getLoanId());
        smsSettingDomain.setId(loanSettingDomain.getId());
        smsSettingDomain.setLoanId(loanSettingDomain.getLoanId());
        smsSettingDomain.setOpen(loanSettingDomain.getIsActive());
        smsSettingDomain.setDay(Integer.parseInt(loanSettingDomain.getValue()));
        return smsSettingDomain;
    }

    public static List<LoanSmsSettingDomain> convertToSmsSettingDomains(List<LoanSettingDomain> source) {
        List<LoanSmsSettingDomain> targets = new ArrayList<>();
        for (LoanSettingDomain settingDomain : source) {
            targets.add(convertToSmsSettingDomain(settingDomain));
        }
        return targets;
    }
}
