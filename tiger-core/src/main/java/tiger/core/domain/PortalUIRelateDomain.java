package tiger.core.domain;

import tiger.common.dal.enums.PortalPositionEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaric Liao on 2016/1/21.
 */
public class PortalUIRelateDomain extends BaseDomain {

    private Long attachId;

    @CopyIgnore
    private PortalPositionEnum location;

    private String title;

    private String url;

    private Integer rank;

    private Map<String, String> extParams = new HashMap<String, String>();

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public PortalPositionEnum getLocation() {
        return location;
    }

    public void setLocation(PortalPositionEnum location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Map<String, String> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, String> extParams) {
        this.extParams = extParams;
    }
}
