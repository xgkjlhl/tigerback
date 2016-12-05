package tiger.web.mng.form.portal;

import tiger.common.dal.enums.PortalPositionEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.PortalUIRelateDomain;
import tiger.web.mng.form.BaseForm;
import tiger.web.mng.form.FormInterface;

import javax.validation.constraints.NotNull;

/**
 * Created by Jaric Liao on 2016/5/18.
 */
public class UIEidtForm extends BaseForm implements FormInterface {
    @CopyIgnore
    private String location;

    private String title;

    private Long attachId;

    private String url;

    private Integer rank;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public PortalUIRelateDomain convert2Domain() {
        PortalUIRelateDomain domain = new PortalUIRelateDomain();
        BeanUtil.copyPropertiesWithIgnores(this, domain);
        if ( location!=null ) domain.setLocation(PortalPositionEnum.getEnumByCode(location));
        return domain;
    }
}
