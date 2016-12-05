package tiger.web.mng.form.portal;

import org.springframework.web.multipart.MultipartFile;
import tiger.common.dal.enums.PortalPositionEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.PortalUIRelateDomain;
import tiger.web.mng.form.BaseForm;
import tiger.web.mng.form.FormInterface;

import javax.validation.constraints.NotNull;

/**
 * Created by Jaric Liao on 2016/1/21.
 */
public class UIRelateForm extends BaseForm implements FormInterface{

    @NotNull(message = "位置不能为空")
    private PortalPositionEnum location;

    private String title;

    @NotNull
    private Long attachId;

    @NotNull
    private String url;

    @NotNull
    private int rank = 1;

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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
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
        return domain;
    }
}
