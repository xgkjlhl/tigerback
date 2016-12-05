/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.attach;

import tiger.common.dal.enums.AttachTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.AttachDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * The Class AttachForm.
 *
 * @author alfred.yx
 * @version v 0.1 Oct 5, 2015 4:20:48 PM alfred Exp $
 */
public class AttachForm extends BaseForm implements FormInterface {

    /**
     * The name.
     */
    @NotNull(message = "文件名不能为空")
    @Pattern(regexp = ".+\\..+", message = "文件名不合法，格式为：文件名.文件格式")
    @Size(max = 122, message = "文件名长度不合法，文件名最大长度为122")
    private String name;

    /**
     * The attach type.
     */
    @CopyIgnore
    private String attachType;

    /**
     * The meta data.
     */
    private Map<String, String> metaData;

    /**
     * @return the attach domain
     * @see FormInterface#convert2Domain()
     */
    @Override
    public AttachDomain convert2Domain() {
        AttachDomain target = new AttachDomain();
        BeanUtil.copyPropertiesWithIgnores(this, target);
        if (StringUtil.isNotEmpty(attachType)) {
            target.setAttachType(AttachTypeEnum.getEnumByCode(attachType));
        }
        return target;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the attach type.
     *
     * @return the attach type
     */
    public String getAttachType() {
        return attachType;
    }

    /**
     * Sets the attach type.
     *
     * @param attachType the new attach type
     */
    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }

    /**
     * Gets the meta data.
     *
     * @return the meta data
     */
    public Map<String, String> getMetaData() {
        return metaData;
    }

    /**
     * Sets the meta data.
     *
     * @param metaData the meta data
     */
    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }

}
