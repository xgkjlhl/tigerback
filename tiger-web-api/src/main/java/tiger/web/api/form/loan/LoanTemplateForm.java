/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.web.api.form.loan;

import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.constants.LoanConstants;
import tiger.core.domain.LoanTemplateDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import java.util.Date;
import java.util.HashMap;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author alfred
 * @version $ID: v 0.1 下午8:31 alfred Exp $
 */
public class LoanTemplateForm extends BaseForm implements FormInterface {

    private long id;

    /**
     * 业务类型
     */
    @NotNull(message = "请选择业务类型")
    @CopyIgnore
    private String businessType;

    /**
     * 抵押类型
     */
    @NotNull(message = "请填写抵押方式")
    @CopyIgnore
    private String type;

    /**
     * 还款类型
     */
    @NotNull(message = "请选择还款方式")
    @CopyIgnore
    private String payWay;

    @CopyIgnore
    private Date startDate;

    @CopyIgnore
    private Date endDate;

    private Integer payCircle;

    private Integer payTotalCircle;

    private Double amount;

    /**
     * 模板名称
     */
    @NotNull(message = "请填写合同模板名称")
    @Size(min = 1, max = 256, message = "合同模板名称长度应在1到256之间")
    private String templateName;

    /**
     * 提前结清违约金率（千分之）
     */
    @NotNull(message = "请填写违约金利率")
    private Double penaltyRate;

    @NotNull(message = "请填写贷款利率")
    private Double interestRate;

    /**
     * 履约保证金
     */
    private Double bondPerform;

    /**
     * 服务费用
     */
    private Double costService;

    /**
     * 其他费用
     */
    private Double costOther;

    /**
     * 暂收费用
     */
    private Double costTemp;

    /**
     * 暂收费用备注
     */
    @Size(max = 128, message = "暂收费用备注信息过长,< 128")
    private String costTempDesc = "";

    /**
     * 其他费用备注
     */
    @Size(max = 128, message = "其他费用备注信息过长,< 128")
    private String costOtherDesc = "";

    /**
     * 按期费用
     * 用于用户定制
     * 存放于额外字段中
     */
    private Double costSchedule;

    /**
     * 按期费用名称
     */
    @Size(max = 32, message = "按期费用名称过长,< 32")
    private String costScheduleName;

    /**
     * 其他费用1
     * 用于用户定制
     * 存放于额外字段中
     */
    private Double costOtherFirst;

    /**
     * 其他费用1名称
     */
    @Size(max = 32, message = "其他费用1名称过长,< 32")
    private String costOtherFirstName;

    /**
     * 其他费用1备注
     */
    @Size(max = 128, message = "其他费用1备注信息过长,< 128")
    private String costOtherFirstDesc;


    /**
     * 其他费用2
     * 用于用户定制
     * 存放于额外字段中
     */
    private Double costOtherSecond;

    /**
     * 其他费用2名称
     */
    @Size(max = 32, message = "其他费用2名称过长,< 32")
    private String costOtherSecondName;

    /**
     * 其他费用2备注
     */
    @Size(max = 128, message = "其他费用2备注信息过长,< 128")
    private String costOtherSecondDesc;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public Integer getPayCircle() {
        return payCircle;
    }

    public void setPayCircle(Integer payCircle) {
        this.payCircle = payCircle;
    }

    public Integer getPayTotalCircle() {
        return payTotalCircle;
    }

    public void setPayTotalCircle(Integer payTotalCircle) {
        this.payTotalCircle = payTotalCircle;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Double getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(Double penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getBondPerform() {
        return bondPerform;
    }

    public void setBondPerform(Double bondPerform) {
        this.bondPerform = bondPerform;
    }

    public Double getCostService() {
        return costService;
    }

    public void setCostService(Double costService) {
        this.costService = costService;
    }

    public Double getCostOther() {
        return costOther;
    }

    public void setCostOther(Double costOther) {
        this.costOther = costOther;
    }

    public Double getCostTemp() {
        return costTemp;
    }

    public void setCostTemp(Double costTemp) {
        this.costTemp = costTemp;
    }

    public String getCostTempDesc() {
        return costTempDesc;
    }

    public void setCostTempDesc(String costTempDesc) {
        this.costTempDesc = costTempDesc;
    }

    public String getCostOtherDesc() {
        return costOtherDesc;
    }

    public void setCostOtherDesc(String costOtherDesc) {
        this.costOtherDesc = costOtherDesc;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Double getCostSchedule() {
        return costSchedule;
    }

    public void setCostSchedule(Double costSchedule) {
        this.costSchedule = costSchedule;
    }

    public String getCostScheduleName() {
        return costScheduleName;
    }

    public void setCostScheduleName(String costScheduleName) {
        this.costScheduleName = costScheduleName;
    }

    public Double getCostOtherFirst() {
        return costOtherFirst;
    }

    public void setCostOtherFirst(Double costOtherFirst) {
        this.costOtherFirst = costOtherFirst;
    }

    public String getCostOtherFirstName() {
        return costOtherFirstName;
    }

    public void setCostOtherFirstName(String costOtherFirstName) {
        this.costOtherFirstName = costOtherFirstName;
    }

    public String getCostOtherFirstDesc() {
        return costOtherFirstDesc;
    }

    public void setCostOtherFirstDesc(String costOtherFirstDesc) {
        this.costOtherFirstDesc = costOtherFirstDesc;
    }

    public Double getCostOtherSecond() {
        return costOtherSecond;
    }

    public void setCostOtherSecond(Double costOtherSecond) {
        this.costOtherSecond = costOtherSecond;
    }

    public String getCostOtherSecondName() {
        return costOtherSecondName;
    }

    public void setCostOtherSecondName(String costOtherSecondName) {
        this.costOtherSecondName = costOtherSecondName;
    }

    public String getCostOtherSecondDesc() {
        return costOtherSecondDesc;
    }

    public void setCostOtherSecondDesc(String costOtherSecondDesc) {
        this.costOtherSecondDesc = costOtherSecondDesc;
    }

    @Override
    public LoanTemplateDomain convert2Domain() {
        LoanTemplateDomain loanTemplateDomain = new LoanTemplateDomain();
        BeanUtil.copyPropertiesWithIgnores(this, loanTemplateDomain);
        //~ 枚举类型
        loanTemplateDomain.setBusinessType(BusinessTypeEnum.getEnumByCode(this.getBusinessType()));
        loanTemplateDomain.setType(LoanPawnTypeEnum.getEnumByCode(this.getType()));
        loanTemplateDomain.setPayWay(LoanPayWayEnum.getEnumByCode(this.getPayWay()));

        // 额外参数
        HashMap<String, String> map = new HashMap<>();
        map.put(LoanConstants.COST_OTHER_DESC, StringUtil.defaultIfBlank(this.costOtherDesc, ""));
        map.put(LoanConstants.COST_TEMP_DESC, StringUtil.defaultIfBlank(this.costTempDesc, ""));
        loanTemplateDomain.setExtParams(map);

        // 按期费用
        String costShedule = "0.0";
        if (this.costSchedule != null) {
            costShedule = this.costSchedule.toString();
        }
        map.put(LoanConstants.COST_SCHEDULE_VALUE, costShedule);
        map.put(
            LoanConstants.COST_SCHEDULE_NAME,
            StringUtil.defaultIfBlank(this.costScheduleName, LoanPayItemEnum.COST_SCHEDULE.getValue())
        );

        // 其他费用1
        String costOtherFirst = "0.0";
        if (this.costOtherFirst != null) {
            costOtherFirst = this.costOtherFirst.toString();
        }
        map.put(LoanConstants.COST_OTHER_FIRST_VALUE, costOtherFirst);
        map.put(
            LoanConstants.COST_OTHER_FIRST_NAME,
            StringUtil.defaultIfBlank(this.costOtherFirstName, LoanPayItemEnum.COST_CUSTOMIZE_OTHER_FIRST.getValue())
        );
        map.put(LoanConstants.COST_OTHER_FIRST_DESC, StringUtil.defaultIfBlank(this.costOtherFirstDesc, ""));

        // 其他费用2
        String costOtherSecond = "0.0";
        if (this.costOtherSecond != null) {
            costOtherSecond = this.costOtherSecond.toString();
        }
        map.put(LoanConstants.COST_OTHER_SECOND_VALUE, costOtherSecond);
        map.put(
            LoanConstants.COST_OTHER_SECOND_NAME,
            StringUtil.defaultIfBlank(this.costOtherSecondName, LoanPayItemEnum.COST_CUSTOMIZE_OTHER_SECOND.getValue())
        );
        map.put(LoanConstants.COST_OTHER_SECOND_DESC, StringUtil.defaultIfBlank(this.costOtherSecondDesc, ""));

        return loanTemplateDomain;
    }
}
