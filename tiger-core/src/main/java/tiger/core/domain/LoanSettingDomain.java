/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.LoanSettingTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

/**
 * 贷款项目配置的领域模型
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:37 PM yiliang.gyl Exp $
 */
public class LoanSettingDomain extends BaseDomain {

    private Long loanId;

    private String name;

    private String value;

    @CopyIgnore
    private Boolean isActive;

    @CopyIgnore
    private LoanSettingTypeEnum loanSettingTypeEnum;


    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LoanSettingTypeEnum getLoanSettingTypeEnum() {
        return loanSettingTypeEnum;
    }

    public void setLoanSettingTypeEnum(LoanSettingTypeEnum loanSettingTypeEnum) {
        this.loanSettingTypeEnum = loanSettingTypeEnum;
    }
}
