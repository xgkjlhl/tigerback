/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.web.api.form.loan;

import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.core.constants.LoanConstants;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.extraDomain.ExtraField;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import java.util.Date;
import java.util.HashMap;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author alfred_yuan
 * @version ${ID}: v 0.1 16:43 alfred_yuan Exp $
 */
public class LoanRefineForm extends BaseForm implements FormInterface {

    @NotNull(message = "请选择还款周期")
    private Integer payCircle;

    @NotNull(message = "请填写合同期限")
    @Min(value = 1, message = "期限设置错误")
    @Max(value = 60, message = "期限设置错误")
    private Integer payTotalCircle;

    @NotNull(message = "请填写合同金额")
    private Double amount;

    @NotNull(message = "请填写贷款利率")
    private Double interestRate;

    /**
     * 还款类型
     */
    @NotNull(message = "请选择还款类型")
    private String payWay;

    /**
     * 开始时间
     */
    @NotNull(message = "请选择开始时间")
    private Date startDate;

    /**
     * 首次还款日期
     */
    @NotNull(message = "请输入首次还款时间")
    private Date startPayDate;

    /**
     * 提前结清违约金率（千分之）
     */
    @NotNull(message = "请填写违约金利率")
    private Double penaltyRate;

    /**
     * 履约保证金
     */
    private Double bondPerform = 0.00;

    /**
     * 服务费用
     */
    private Double costService = 0.00;

    /**
     * 其他费用
     */
    private Double costOther = 0.00;

    /**
     * 暂收费用
     */
    private Double costTemp = 0.00;

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
     * 客户备注信息
     */
    @Size(max = 256, message = "客户备注信息过长,< 256")
    private String customerDesc = "";

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

    public Date getStartPayDate() {
        return startPayDate;
    }

    public void setStartPayDate(Date startPayDate) {
        this.startPayDate = startPayDate;
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

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startTime) {
        this.startDate = startTime;
    }

    public Double getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(Double penaltyRate) {
        this.penaltyRate = penaltyRate;
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

    /**
     * Getter for property 'customerDesc'.
     *
     * @return Value for property 'customerDesc'.
     */
    public String getCustomerDesc() {
        return customerDesc;
    }

    /**
     * Setter for property 'customerDesc'.
     *
     * @param customerDesc Value to set for property 'customerDesc'.
     */
    public void setCustomerDesc(String customerDesc) {
        this.customerDesc = customerDesc;
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
    public LoanDomain convert2Domain() {
        LoanDomain result = new LoanDomain();

        BeanUtil.copyPropertiesWithIgnores(this, result);
        result.setPayWay(LoanPayWayEnum.getEnumByCode(payWay));

        HashMap<String, String> map = new HashMap<>();
        map.put(LoanConstants.COST_OTHER_DESC, StringUtil.defaultIfBlank(costOtherDesc, ""));
        map.put(LoanConstants.COST_TEMP_DESC, StringUtil.defaultIfBlank(costTempDesc, ""));
        map.put(LoanConstants.CUSTOMER_DESC, StringUtil.defaultIfBlank(customerDesc, ""));

        result.setExtParams(map);

        // 按期费用
        if (this.costSchedule != null && this.costScheduleName != null) {
            ExtraField costSchedule = new ExtraField(this.costScheduleName, this.costSchedule);
            result.setCostSchedule(costSchedule);
        }

        // 其他费用1
        if (this.costOtherFirst != null && this.costOtherFirstName != null) {
            ExtraField costOtherFirst = new ExtraField(this.costOtherFirstName, this.costOtherFirst, this.costOtherFirstDesc);
            result.setCostOtherFirst(costOtherFirst);
        }

        // 其他费用2
        if (this.costOtherSecond != null && this.costOtherSecondName != null) {
            ExtraField costOtherSecond = new ExtraField(this.costOtherSecondName, this.costOtherSecond, this.costOtherSecondDesc);
            result.setCostOtherSecond(costOtherSecond);
        }

        return result;
    }
}
