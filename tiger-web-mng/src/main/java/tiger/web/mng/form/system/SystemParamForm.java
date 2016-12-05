/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.mng.form.system;

import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.core.domain.SystemParamDomain;
import tiger.web.mng.form.BaseForm;
import tiger.web.mng.form.FormInterface;

import javax.validation.constraints.Size;

/**
 * @author alfred_yuan
 * @version v 0.1 20:36 alfred_yuan Exp $
 */
public class SystemParamForm extends BaseForm implements FormInterface {

    @Size(max = 64, min = 1, message = "不合法的参数名称")
    private String paramName;

    @Size(max = 1024, min = 1, message = "不合法的参数值")
    private String paramValue;

    private boolean isActive;

    private String paramType;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    @Override
    public SystemParamDomain convert2Domain() {
        SystemParamDomain target = new SystemParamDomain();
        target.setParamName(this.paramName);
        target.setParamValue(this.paramValue);
        target.setParamType(SystemParamTypeEnum.getEnumByCode(this.paramType));
        target.setIsActive(this.isActive);
        return target;
    }
}
