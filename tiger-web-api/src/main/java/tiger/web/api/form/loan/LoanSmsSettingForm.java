/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.loan;

import tiger.core.domain.LoanSmsSettingDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 短信设置表单
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 7:07 PM yiliang.gyl Exp $
 */
public class LoanSmsSettingForm extends BaseForm implements FormInterface {

    /**
     * 是否开放短信通知
     */
    @NotNull(message = "需要设置是否开启")
    private Boolean open;

    /**
     * 通知时间设置
     */
    @Max(value = 10, message = "不能超过10天")
    @Min(value = 0, message = "不能低于0天")
    private Integer day;

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     * @return
     */
    @Override
    public LoanSmsSettingDomain convert2Domain() {
        LoanSmsSettingDomain target = new LoanSmsSettingDomain();

        target.setOpen(this.open);
        target.setDay(this.day);

        return target;
    }
}
