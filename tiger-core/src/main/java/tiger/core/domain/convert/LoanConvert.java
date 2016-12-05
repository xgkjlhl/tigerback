/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.domain.convert;

import org.apache.commons.lang3.math.NumberUtils;
import tiger.common.dal.dataobject.LoanDO;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.JsonConverterUtil;
import tiger.common.util.JsonUtil;
import tiger.common.util.MapUtil;
import tiger.common.util.StringUtil;
import tiger.core.constants.LoanConstants;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.extraDomain.ExtraField;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贷款 DO 转换器
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 23, 2015 10:39:49 AM yiliang.gyl Exp $
 */
public class LoanConvert {

    private LoanConvert() {
    }

    /**
     * Convert domain to do.
     *
     * @param domain the domain
     *
     * @return the loan tpl do
     */
    public static LoanDO convertDomainToDO(LoanDomain domain) {
        LoanDO loanDO = new LoanDO();
        if (domain == null) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND.getCode(),
                ErrorCodeEnum.NOT_FOUND.getDefaultMessage());
        }
        BeanUtil.copyPropertiesWithIgnores(domain, loanDO);
        if (domain.getId() != null) {
            loanDO.setId(domain.getId());
        }
        if (null != domain.getBusinessType()) {
            loanDO.setBusinessType(domain.getBusinessType().getCode());
        }
        if (null != domain.getType()) {
            loanDO.setType(domain.getType().getCode());
        }
        if (null != domain.getPayWay()) {
            loanDO.setPayWay(domain.getPayWay().getCode());
        }
        if (null != domain.getLoanStatus()) {
            loanDO.setState(domain.getLoanStatus().getCode());
        }

        Map<String, String> extraFieldsMap = convertExtraFieldsMap(domain);
        if (null != domain.getExtParams()) {
            extraFieldsMap.putAll(domain.getExtParams());
        }
        if (MapUtil.isNotEmpty(extraFieldsMap)) {
            loanDO.setExtParams(JsonConverterUtil.serialize(extraFieldsMap));
        }

        return loanDO;
    }

    /**
     * Convert do to domain.
     *
     * @param LoanDO the loan tpl do
     *
     * @return the loan template domain
     */
    @SuppressWarnings("unchecked")
    public static LoanDomain convertDoToDomain(LoanDO LoanDO) {
        LoanDomain domain = new LoanDomain();
        if (LoanDO == null) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND.getCode(),
                ErrorCodeEnum.NOT_FOUND.getDefaultMessage());
        }
        BeanUtil.copyPropertiesWithIgnores(LoanDO, domain);

        domain.setType(LoanPawnTypeEnum.getEnumByCode(LoanDO.getType()));
        domain.setPayWay(LoanPayWayEnum.getEnumByCode(LoanDO.getPayWay()));
        domain.setLoanStatus(LoanStatusEnum.getEnumByCode(LoanDO.getState()));
        domain.setBusinessType(BusinessTypeEnum.getEnumByCode(LoanDO.getBusinessType()));
        if (!StringUtil.isBlank(LoanDO.getExtParams())) {
            HashMap<String, String> extParams = JsonUtil.fromJson(LoanDO.getExtParams(), HashMap.class);
            //保留原domain中的extParams，排除前端对extParams的依赖后可删除
            domain.setExtParams(extParams);

            //将extParams中的字段拆分出来
            domain.setCostOtherDesc(extParams.get("costOtherDesc"));
            domain.setCostTempDesc(extParams.get("costTempDesc"));
            domain.setCustomerDesc(extParams.get("customerDesc"));

            // 按期费用
            String costScheduleName = extParams.get(LoanConstants.COST_SCHEDULE_NAME);
            String costScheduleValue = extParams.get(LoanConstants.COST_SCHEDULE_VALUE);
            if (StringUtil.isNotBlank(costScheduleName) && NumberUtils.isNumber(costScheduleValue)) {
                ExtraField costSchedule = new ExtraField(costScheduleName, Double.valueOf(costScheduleValue));
                domain.setCostSchedule(costSchedule);
            }

            // 其他费用1
            String costOtherFirstName = extParams.get(LoanConstants.COST_OTHER_FIRST_NAME);
            String costOtherFirstValue = extParams.get(LoanConstants.COST_OTHER_FIRST_VALUE);
            String costOtherFirstDesc = extParams.get(LoanConstants.COST_OTHER_FIRST_DESC);
            if (StringUtil.isNotBlank(costOtherFirstName) && NumberUtils.isNumber(costOtherFirstValue)) {
                ExtraField costOtherFirst = new ExtraField(costOtherFirstName, Double.valueOf(costOtherFirstValue), costOtherFirstDesc);
                domain.setCostOtherFirst(costOtherFirst);
            }

            // 其他费用2
            String costOtherSecondName = extParams.get(LoanConstants.COST_OTHER_SECOND_NAME);
            String costOtherSecondValue = extParams.get(LoanConstants.COST_OTHER_SECOND_VALUE);
            String costOtherSecondDesc = extParams.get(LoanConstants.COST_OTHER_SECOND_DESC);
            if (StringUtil.isNotBlank(costOtherSecondName) && NumberUtils.isNumber(costOtherSecondValue)) {
                ExtraField costOtherSecond = new ExtraField(costOtherSecondName, Double.valueOf(costOtherSecondValue), costOtherSecondDesc);
                domain.setCostOtherSecond(costOtherSecond);
            }
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
    public static List<LoanDomain> convertDOsToDomains(List<LoanDO> dos) {
        List<LoanDomain> results = new ArrayList<LoanDomain>();
        if (!dos.isEmpty()) {
            for (LoanDO lTplDO : dos) {
                results.add(convertDoToDomain(lTplDO));
            }
        }
        return results;
    }

    /**
     * 将 按期费用, 其他费用1, 其他费用2 抽取成 Map对象
     *
     * @param domain
     *
     * @return
     */
    private static Map<String, String> convertExtraFieldsMap(LoanDomain domain) {
        Map<String, String> extraFieldsMap = new HashMap<>();

        // 按期费用
        ExtraField costSchedule = domain.getCostSchedule();
        if (costSchedule != null) {
            extraFieldsMap.put(
                LoanConstants.COST_SCHEDULE_NAME,
                StringUtil.defaultIfBlank(costSchedule.getName(), LoanPayItemEnum.COST_SCHEDULE.getValue())
            );
            String costScheduleValue = "0.0";
            if (costSchedule.getValue() != null) {
                costScheduleValue = costSchedule.getValue().toString();
            }
            extraFieldsMap.put(LoanConstants.COST_SCHEDULE_VALUE, costScheduleValue);
        }

        ExtraField costOtherFirst = domain.getCostOtherFirst();
        if (costOtherFirst != null) {
            extraFieldsMap.put(
                LoanConstants.COST_OTHER_FIRST_NAME,
                StringUtil.defaultIfBlank(costOtherFirst.getName(), LoanPayItemEnum.COST_CUSTOMIZE_OTHER_FIRST.getValue())
            );
            String costOtherFirstValue = "0.0";
            if (costOtherFirst.getValue() != null) {
                costOtherFirstValue = costOtherFirst.getValue().toString();
            }
            extraFieldsMap.put(LoanConstants.COST_OTHER_FIRST_VALUE, costOtherFirstValue);
            extraFieldsMap.put(LoanConstants.COST_OTHER_FIRST_DESC, StringUtil.defaultIfBlank(costOtherFirst.getDescription(), ""));
        }

        ExtraField costOtherSecond = domain.getCostOtherSecond();
        if (costOtherSecond != null) {
            extraFieldsMap.put(
                LoanConstants.COST_OTHER_SECOND_NAME,
                StringUtil.defaultIfBlank(costOtherSecond.getName(), LoanPayItemEnum.COST_CUSTOMIZE_OTHER_SECOND.getValue())
            );
            String costOtherSecondValue = "0.0";
            if (costOtherSecond.getValue() != null) {
                costOtherSecondValue = costOtherSecond.getValue().toString();
            }
            extraFieldsMap.put(LoanConstants.COST_OTHER_SECOND_VALUE, costOtherSecondValue);
            extraFieldsMap.put(LoanConstants.COST_OTHER_SECOND_DESC, StringUtil.defaultIfBlank(costOtherSecond.getDescription(), ""));
        }

        return extraFieldsMap;
    }
}
