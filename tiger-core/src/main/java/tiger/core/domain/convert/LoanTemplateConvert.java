/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.domain.convert;

import tiger.common.dal.dataobject.LoanTplDO;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.JsonConverterUtil;
import tiger.common.util.JsonUtil;
import tiger.common.util.StringUtil;
import tiger.core.constants.LoanConstants;
import tiger.core.domain.LoanTemplateDomain;
import tiger.core.domain.extraDomain.ExtraField;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贷款模板底层转换器
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 19, 2015 3:09:57 PM yiliang.gyl Exp $
 */
public class LoanTemplateConvert {

    private LoanTemplateConvert() {
    }

    /**
     * Convert domain to do.
     *
     * @param domain the domain
     *
     * @return the loan tpl do
     */
    public static LoanTplDO convertDomainToDO(LoanTemplateDomain domain) {
        LoanTplDO loanTplDO = new LoanTplDO();
        if (domain == null) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND.getCode(),
                ErrorCodeEnum.NOT_FOUND.getDefaultMessage());
        }
        BeanUtil.copyPropertiesWithIgnores(domain, loanTplDO);
        if (domain.getId() != null) {
            loanTplDO.setId(domain.getId());
        }
        loanTplDO.setType(domain.getType().getCode());
        loanTplDO.setPayWay(domain.getPayWay().getCode());
        loanTplDO.setBusinessType(domain.getBusinessType().getCode());
        loanTplDO.setExtParams(JsonConverterUtil.serialize(domain.getExtParams()));

        return loanTplDO;
    }

    /**
     * Convert do to domain.
     *
     * @param loanTplDO the loan tpl do
     *
     * @return the loan template domain
     */
    @SuppressWarnings("unchecked")
    public static LoanTemplateDomain convertDoToDomain(LoanTplDO loanTplDO) {
        LoanTemplateDomain domain = new LoanTemplateDomain();
        if (loanTplDO == null) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND.getCode(),
                ErrorCodeEnum.NOT_FOUND.getDefaultMessage());
        }
        BeanUtil.copyPropertiesWithIgnores(loanTplDO, domain);

        domain.setType(LoanPawnTypeEnum.getEnumByCode(loanTplDO.getType()));
        domain.setPayWay(LoanPayWayEnum.getEnumByCode(loanTplDO.getPayWay()));
        domain.setBusinessType(BusinessTypeEnum.getEnumByCode(loanTplDO.getBusinessType()));
        if (!StringUtil.isBlank(loanTplDO.getExtParams())) {
            Map<String, String> extParamsMap = JsonUtil.fromJson(loanTplDO.getExtParams(), HashMap.class);

            // 按期费用
            Double costScheduleValue = Double.valueOf(
                StringUtil.defaultIfBlank(extParamsMap.get(LoanConstants.COST_SCHEDULE_VALUE), "0.0")
            );
            String costScheduleName = StringUtil.defaultIfBlank(
                extParamsMap.get(LoanConstants.COST_SCHEDULE_NAME), ""
            );
            ExtraField costSchedule = new ExtraField(costScheduleName, costScheduleValue);
            domain.setCostSchedule(costSchedule);

            // 其他费用1
            Double costOtherFirstValue = Double.valueOf(
                StringUtil.defaultIfBlank(extParamsMap.get(LoanConstants.COST_OTHER_FIRST_VALUE), "0.0")
            );
            String costOtherFirstName = StringUtil.defaultIfBlank(
                extParamsMap.get(LoanConstants.COST_OTHER_FIRST_NAME), ""
            );
            String costOtherFirstDesc = StringUtil.defaultIfBlank(
                extParamsMap.get(LoanConstants.COST_OTHER_FIRST_DESC), ""
            );
            ExtraField costOtherFirst = new ExtraField(costOtherFirstName, costOtherFirstValue, costOtherFirstDesc);
            domain.setCostOtherFirst(costOtherFirst);

            // 其他费用2
            Double costOtherSecondValue = Double.valueOf(
                StringUtil.defaultIfBlank(extParamsMap.get(LoanConstants.COST_OTHER_SECOND_VALUE), "0.0")
            );
            String costOtherSecondName = StringUtil.defaultIfBlank(
                extParamsMap.get(LoanConstants.COST_OTHER_SECOND_NAME), ""
            );
            String costOtherSecondDesc = StringUtil.defaultIfBlank(
                extParamsMap.get(LoanConstants.COST_OTHER_SECOND_DESC), ""
            );
            ExtraField costOtherSecond = new ExtraField(costOtherSecondName, costOtherSecondValue, costOtherSecondDesc);
            domain.setCostOtherSecond(costOtherSecond);

            domain.setExtParams(extParamsMap);
        }
        return domain;
    }


    /**
     * Convert d os to domains.
     *
     * @param dos the dos
     *
     * @return the list
     */
    public static List<LoanTemplateDomain> convertDOsToDomains(List<LoanTplDO> dos) {
        List<LoanTemplateDomain> results = new ArrayList<LoanTemplateDomain>();
        if (!dos.isEmpty()) {
            for (LoanTplDO lTplDO : dos) {
                results.add(convertDoToDomain(lTplDO));
            }
        }
        return results;
    }
}
