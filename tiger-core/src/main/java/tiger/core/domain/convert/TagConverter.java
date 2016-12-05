package tiger.core.domain.convert;

import org.springframework.util.CollectionUtils;
import tiger.common.dal.dataobject.TagDO;
import tiger.common.util.BeanUtil;
import tiger.core.domain.TagDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Domi.Hu
 * @version v 0.1 2015年9月20日 下午3:22:13 Domi.Hu Exp $
 */
public class TagConverter {

    private TagConverter() {

    }

    public static TagDomain convert2Domain(TagDO source) {
        if (source == null) {
            return null;
        }
        TagDomain target = new TagDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        return target;
    }

    public static List<TagDomain> convert2Domain(List<TagDO> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        List<TagDomain> targetList = new ArrayList<>();
        for (TagDO source : sourceList) {
            targetList.add(TagConverter.convert2Domain(source));
        }
        return targetList;
    }

    public static TagDO convert2DO(TagDomain source) {
        if (source == null) {
            return null;
        }
        TagDO target = new TagDO();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        return target;
    }

}
