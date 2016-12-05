/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dto.LoanLightDTO;
import tiger.common.util.BeanUtil;
import tiger.core.domain.LoanLightDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 16/3/20 下午4:29 mi.li Exp $
 */
public class LoanLightConvert {
    private LoanLightConvert() {
    }

    public static LoanLightDomain convert2Domain(LoanLightDTO source){
        if(source == null)
            return null;
        LoanLightDomain target = new LoanLightDomain();
        BeanUtil.copyPropertiesWithIgnores(source,target);
        return target;
    }

    public static List<LoanLightDomain> convert2Domains(List<LoanLightDTO> source){
        if(source == null)
            return null;
        List<LoanLightDomain> target = new ArrayList<>();
        for(LoanLightDTO loanLightDo : source){
            target.add(convert2Domain(loanLightDo));

        }
        return target;
    }
}
