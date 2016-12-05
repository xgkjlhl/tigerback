package tiger.core.domain.convert;

import tiger.common.dal.dataobject.CustomerDO;
import tiger.common.util.BeanUtil;
import tiger.core.domain.CustomerDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class CustomerConverter.
 *
 * @author Domi.HuPeng
 * @version v 0.1 2015年9月17日 下午7:42:50 Domi.HuPeng Exp $
 */
public class CustomerConverter {

    /**
     * Convert2 domain.
     *
     * @param source the source
     * @return the customer domain
     */
    public static CustomerDomain convert2Domain(CustomerDO source) {
        CustomerDomain target = new CustomerDomain();
        if (null != source) {
            BeanUtil.copyPropertiesWithIgnores(source, target);
            if (source.getBirthday() == null) {
                target.setBirthday(null);
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
    public static List<CustomerDomain> convert2Domain(List<CustomerDO> sourceList) {
        List<CustomerDomain> targetList = new ArrayList<>();
        sourceList.forEach(source ->
                        targetList.add(CustomerConverter.convert2Domain(source))
        );
        return targetList;
    }

    public static CustomerDO convert2DO(CustomerDomain source) {
        CustomerDO target = new CustomerDO();
        if (null != source) {
            BeanUtil.copyPropertiesWithIgnores(source, target);
            if (null != source.getIcon()) {
                target.setIcon(source.getIcon().getId());
            }
        }
        return target;
    }

}
