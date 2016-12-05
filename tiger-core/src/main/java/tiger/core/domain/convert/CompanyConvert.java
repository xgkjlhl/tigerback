/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import org.apache.commons.collections.CollectionUtils;
import tiger.common.dal.dataobject.CompanyDO;
import tiger.common.util.BeanUtil;
import tiger.core.domain.CompanyDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * 公司类型转换器
 *
 * @author alfred.yx
 * @version $ID: v 0.1 下午12:08 alfred.yx Exp $
 */
public class CompanyConvert {
    private CompanyConvert() {
    }

    public static CompanyDomain convertToDomain(CompanyDO source) {
        if (null == source) {
            return null;
        }
        CompanyDomain target = new CompanyDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        return target;
    }

    public static List<CompanyDomain> convertToDomains(List<CompanyDO> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }

        List<CompanyDomain> targets = new ArrayList<>(sources.size());
        sources.forEach(source -> targets.add(convertToDomain(source)));

        return targets;
    }
}
