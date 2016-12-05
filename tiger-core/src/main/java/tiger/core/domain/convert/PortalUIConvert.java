package tiger.core.domain.convert;

import tiger.common.dal.dataobject.PortalUIDO;
import tiger.common.dal.enums.PortalPositionEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.JsonConverterUtil;
import tiger.common.util.JsonUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.PortalUIRelateDomain;

import java.util.HashMap;

/**
 * Created by Jaric Liao on 2016/1/23.
 */
public class PortalUIConvert {

    public static PortalUIDO convert2DO(PortalUIRelateDomain domain){
        if (domain == null) {
            return null;
        }
        PortalUIDO portalUIDO = new PortalUIDO();
        BeanUtil.copyPropertiesWithIgnores(domain, portalUIDO);
        if (null != domain.getLocation()){
            portalUIDO.setLocation(domain.getLocation().getCode());
        }
        if (null != domain.getExtParams()) {
            portalUIDO.setExtParams(JsonConverterUtil.serialize(domain.getExtParams()));
        }
        return portalUIDO;
    }

    public static PortalUIRelateDomain convert2Domain(PortalUIDO portalUIDO){
        if (portalUIDO == null) {
            return null;
        }
        PortalUIRelateDomain domain = new PortalUIRelateDomain();
        BeanUtil.copyPropertiesWithIgnores(portalUIDO, domain);
        if (null != portalUIDO.getLocation()){
            domain.setLocation(PortalPositionEnum.getEnumByCode(portalUIDO.getLocation()));
        }
        if (StringUtil.isNotBlank(portalUIDO.getExtParams())) {
            domain.setExtParams(JsonUtil.fromJson(portalUIDO.getExtParams(), HashMap.class));
        }
        return domain;
    }
}
