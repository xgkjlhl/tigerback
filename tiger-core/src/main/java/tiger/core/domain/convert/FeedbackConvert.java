/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import org.apache.commons.collections.CollectionUtils;
import tiger.common.dal.dataobject.FeedbackDO;
import tiger.common.dal.dto.list.FeedbackListDTO;
import tiger.common.util.BeanUtil;
import tiger.core.domain.FeedbackDomain;
import tiger.core.domain.listDomain.FeedbackListDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 16:14 alfred_yuan Exp $
 */
public class FeedbackConvert {

    private FeedbackConvert() {}

    public static FeedbackDomain convert2Domain(FeedbackDO source) {
        if (source == null) {
            return null;
        }
        FeedbackDomain target = new FeedbackDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        return  target;
    }

    public static List<FeedbackDomain> convert2Domains(List<FeedbackDO> sources) {
        if (sources == null) {
            return null;
        }
        List<FeedbackDomain> targets = new ArrayList<>(sources.size());
        sources.forEach(source -> targets.add(convert2Domain(source)));
        return targets;
    }

    public static FeedbackDO convert2DO(FeedbackDomain source) {
        if (source == null) {
            return null;
        }
        FeedbackDO target = new FeedbackDO();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        return target;
    }

    public static List<FeedbackListDomain> convert2ListDomains(List<FeedbackListDTO> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }

        List<FeedbackListDomain> targets = new ArrayList<>(sources.size());
        sources.forEach(source -> targets.add(convet2ListDomain(source)));

        return targets;
    }

    public static FeedbackListDomain convet2ListDomain(FeedbackListDTO source) {
        if (source == null) {
            return null;
        }

        FeedbackListDomain target = new FeedbackListDomain();
        target.setAccountId(source.getAccountId());
        target.setContent(source.getContent());
        target.setCreateTime(source.getCreateTime());
        target.setId(source.getId());

        return target;
    }

}
