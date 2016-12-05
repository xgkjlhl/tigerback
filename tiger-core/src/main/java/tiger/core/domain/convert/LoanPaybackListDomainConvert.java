/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dto.LoanPaybackDTO;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.LoanPaybackListDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * 款项列表对象转换器.
 *
 * @author yiliang.gyl
 * @version v 0.1 Oct 4, 2015 8:44:43 PM yiliang.gyl Exp $
 */
public class LoanPaybackListDomainConvert {

    private LoanPaybackListDomainConvert() {
    }

    /**
     * Convert to domain.
     *
     * @param loanPaybackDTO the loan payback dto
     * @return the loan payback list domain
     */
    public static LoanPaybackListDomain convertToDomain(LoanPaybackDTO loanPaybackDTO) {
        if (loanPaybackDTO == null) {
            return null;
        }
        LoanPaybackListDomain listDomain = new LoanPaybackListDomain();
        BeanUtil.copyPropertiesWithIgnores(loanPaybackDTO, listDomain);
        listDomain.setActionType(LoanActionEnum.getEnumByCode(loanPaybackDTO.getType()));
        listDomain.setStatus(LoanStatusEnum.getEnumByCode(loanPaybackDTO.getState()));
        return listDomain;
    }

    /**
     * Convert to domains.
     *
     * @param loanPaybackDTOs the loan payback dt os
     * @return the list
     */
    public static List<LoanPaybackListDomain> convertToDomains(List<LoanPaybackDTO> loanPaybackDTOs) {
        if (null == loanPaybackDTOs) {
            return null;
        }
        List<LoanPaybackListDomain> list = new ArrayList<>();
        for (LoanPaybackDTO dto : loanPaybackDTOs) {
            list.add(convertToDomain(dto));
        }
        return list;
    }

}
