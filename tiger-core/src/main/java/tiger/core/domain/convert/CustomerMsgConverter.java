package tiger.core.domain.convert;

import tiger.common.dal.dataobject.CustomerMsgDO;
import tiger.common.dal.enums.CustomerMsgBizTypeEnum;
import tiger.common.dal.enums.SmsStatusEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.CustomerMsgDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户消息转换器
 *
 * @author HuPeng
 * @version v 0.1 2015年9月30日 下午8:03:12 HuPeng Exp $
 */
public class CustomerMsgConverter {

    /**
     * Convert2 domain.
     *
     * @param source the source
     * @return the customer msg domain
     */
    public static CustomerMsgDomain convert2Domain(CustomerMsgDO source) {
        CustomerMsgDomain target = new CustomerMsgDomain();
        if (null != source) {
            BeanUtil.copyPropertiesWithIgnores(source, target);
            if (StringUtil.isNotBlank(source.getStatus())) {
                target.setStatus(SmsStatusEnum.getEnumByCode(source.getStatus()));
            }
            if (StringUtil.isNotBlank(source.getBizType())) {
                target.setBizType(CustomerMsgBizTypeEnum.getEnumByCode(source.getBizType()));
            }
        }
        return target;
    }

    /**
     * Convert2 domain.
     *
     * @param sourceList the source list
     * @return the list
     */
    public static List<CustomerMsgDomain> convert2Domain(List<CustomerMsgDO> sourceList) {
        List<CustomerMsgDomain> targetList = new ArrayList<>();
        for (CustomerMsgDO source : sourceList) {
            CustomerMsgDomain target = convert2Domain(source);
            targetList.add(target);
        }
        return targetList;
    }

    /**
     * Convert2 do.
     */
    public static CustomerMsgDO convert2Do(CustomerMsgDomain source) {
        CustomerMsgDO target = new CustomerMsgDO();
        if (null != source) {
            BeanUtil.copyPropertiesWithIgnores(source, target);
            if (source.getStatus() != null) {
                target.setStatus(source.getStatus().getCode());
            }
            if (source.getBizType() != null) {
                target.setBizType(source.getBizType().getCode());
            }
        }
        return target;
    }

    /**
     * Convert2 do list
     *
     * @param sourceList
     * @return
     */
    public static List<CustomerMsgDO> conver2Do(List<CustomerMsgDomain> sourceList) {
        List<CustomerMsgDO> targetList = new ArrayList<>();
        for (CustomerMsgDomain source : sourceList) {
            CustomerMsgDO target = convert2Do(source);
            targetList.add(target);
        }
        return targetList;
    }


}
