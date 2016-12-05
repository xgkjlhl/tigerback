/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.UcenterPostReplyDO;
import tiger.common.util.BeanUtil;
import tiger.core.domain.UcenterPostReplyDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 2015年9月28日 下午6:48:29 mi.li Exp $
 */
public class UcenterPostReplyConvert {

    /**
     * @param domain
     * @return
     */
    public static UcenterPostReplyDO convertDomainToDO(UcenterPostReplyDomain domain) {
        if (null == domain) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        UcenterPostReplyDO ucenterPostReplyDO = new UcenterPostReplyDO();
        BeanUtil.copyPropertiesWithIgnores(domain, ucenterPostReplyDO);

        return ucenterPostReplyDO;
    }

    /**
     * @param ucenterPostReplyDO
     * @return
     */
    public static UcenterPostReplyDomain convertDOToDomain(UcenterPostReplyDO ucenterPostReplyDO) {
        if (null == ucenterPostReplyDO) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        UcenterPostReplyDomain domain = new UcenterPostReplyDomain();
        BeanUtil.copyPropertiesWithIgnores(ucenterPostReplyDO, domain);

        return domain;
    }

    /**
     * @param postDOs
     * @return
     */
    public static List<UcenterPostReplyDomain> convertDOsToDomains(List<UcenterPostReplyDO> postReplyDOs) {
        List<UcenterPostReplyDomain> domains = new ArrayList<>();
        if (!postReplyDOs.isEmpty()) {
            for (UcenterPostReplyDO postReplyDO : postReplyDOs) {
                domains.add(convertDOToDomain(postReplyDO));
            }
        }
        return domains;
    }
}
