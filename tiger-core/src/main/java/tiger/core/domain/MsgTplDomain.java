/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.core.base.BaseDomain;

/**
 * @author zhangbin
 * @version v0.1 2015/10/5 17:30
 */
public class MsgTplDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -6157097352710566804L;

    private String content;

    private Long tplId;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTplId() {
        return tplId;
    }

    public void setTplId(Long tplId) {
        this.tplId = tplId;
    }

}
