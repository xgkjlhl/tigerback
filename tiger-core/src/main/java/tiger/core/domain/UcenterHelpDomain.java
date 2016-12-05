/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.sql.Timestamp;

/**
 * @author mi.li
 * @version v 0.1 2015年9月28日 下午8:59:49 mi.li Exp $
 */
public class UcenterHelpDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -2051357785748942036L;

    private Long catalogId;

    private String catalogName;

    private String title;

    private String content;

    private Boolean isCommon;

    @CopyIgnore
    private Timestamp createTime;

    @CopyIgnore
    private Timestamp updateTime;

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Boolean isCommon) {
        this.isCommon = isCommon;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

}
