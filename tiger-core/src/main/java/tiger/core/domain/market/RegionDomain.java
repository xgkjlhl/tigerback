/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.domain.market;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;
import tiger.core.domain.AttachDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 22:06 alfred_yuan Exp $
 */
public class RegionDomain extends BaseDomain {

    private static final long serialVersionUID = -1979625853775904132L;

    private String code;

    private String name;

    @CopyIgnore
    private StringBuilder fullName = new StringBuilder();

    @JsonIgnore
    private Long parentId;

    // 孩子节点
    @CopyIgnore
    private List<RegionDomain> children = new ArrayList<>();

    // 二维码列表
    @CopyIgnore
    private List<AttachDomain> qrCodes = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public StringBuilder getFullName() {
        return fullName;
    }

    public void setFullName(StringBuilder fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<RegionDomain> getChildren() {
        return children;
    }

    public void setChildren(List<RegionDomain> children) {
        this.children = children;
    }

    public List<AttachDomain> getQrCodes() {
        return qrCodes;
    }

    public void setQrCodes(List<AttachDomain> qrCodes) {
        this.qrCodes = qrCodes;
    }
}
