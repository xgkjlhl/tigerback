/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.core.base.BaseDomain;

/**
 * @author mi.li
 * @version v 0.1 16/3/20 下午4:24 mi.li Exp $
 */
public class LoanLightDomain extends BaseDomain {

    private static final long serialVersionUID = -1277074518435658346L;

    private String keyId;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
