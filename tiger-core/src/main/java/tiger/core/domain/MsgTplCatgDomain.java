/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.core.base.BaseDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/10/5 17:30
 */
public class MsgTplCatgDomain extends BaseDomain {
    private String name;

    private String description;

    private List<MsgTplDomain> msgTpls;

    public void addMsgTpl(MsgTplDomain msgTpl) {
        if (msgTpl != null) {
            msgTpls.add(msgTpl);
        } else {
            msgTpls = new ArrayList<>();
            msgTpls.add(msgTpl);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MsgTplDomain> getMsgTpls() {
        return msgTpls;
    }

    public void setMsgTpls(List<MsgTplDomain> msgTpls) {
        this.msgTpls = msgTpls;
    }
}
