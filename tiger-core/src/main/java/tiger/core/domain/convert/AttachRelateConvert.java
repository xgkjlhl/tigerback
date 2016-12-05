/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import org.springframework.util.CollectionUtils;
import tiger.common.dal.dataobject.AttachRelateDO;
import tiger.common.dal.enums.AttachBizTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.AttachRelateDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 11:48 AM yiliang.gyl Exp $
 */
public class AttachRelateConvert {

    public static AttachRelateDomain convertToAttachRelateDomain(AttachRelateDO source) {
        if (null != source) {
            AttachRelateDomain target = new AttachRelateDomain();
            BeanUtil.copyPropertiesWithIgnores(source, target);
            target.setBizType(AttachBizTypeEnum.getEnumByCode(source.getBizType()));
            return target;
        } else {
            return null;
        }
    }

    public static AttachRelateDO convertToAttachRelateDO(AttachRelateDomain source) {
        if (null != source) {
            AttachRelateDO target = new AttachRelateDO();
            BeanUtil.copyPropertiesWithIgnores(source, target);
            target.setBizType(source.getBizType().getCode());
            return target;
        } else {
            return null;
        }
    }

    public static List<AttachRelateDomain> convertTOAttachRelateDomains(List<AttachRelateDO> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<AttachRelateDomain> list = new ArrayList<>(sources.size());
        sources.forEach(attachRelateDO -> list.add(AttachRelateConvert.convertToAttachRelateDomain(attachRelateDO)));
        return list;
    }

    public static List<AttachRelateDO> convertToLoanPawnAttachDOs(List<AttachRelateDomain> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<AttachRelateDO> list = new ArrayList<>(sources.size());
        sources.forEach(attachRelateDomain -> list.add(AttachRelateConvert.convertToAttachRelateDO(attachRelateDomain)));
        return list;
    }
}
