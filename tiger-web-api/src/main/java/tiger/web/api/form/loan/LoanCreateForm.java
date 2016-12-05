/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.web.api.form.loan;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.common.util.loan.LoanDateUtil;
import tiger.core.constants.LoanConstants;
import tiger.core.domain.AttachRelateDomain;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.ModifyLoanRecordDomain;
import tiger.core.domain.extraDomain.ExtraField;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;
import tiger.web.api.form.attach.AttachRelateForm;
import tiger.web.api.form.customer.CustomerCreateForm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 创建贷款/融资需要填写的字段
 * ~ 融资和贷款模板也采用这个Form
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 13, 2015 4:40:29 PM yiliang.gyl Exp $
 */
public class LoanCreateForm extends BaseForm implements FormInterface {

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

    @NotNull(message = "请选择开始时间")
    @CopyIgnore
    private Date startDate;

    @NotNull(message = "请选择贷款周期")
    @CopyIgnore
    private Integer payCircle;

    @NotNull(message = "请填写贷款总期数")
    @Min(value = 1, message = "期限设置错误")
    @Max(value = 60, message = "期限设置错误")
    @CopyIgnore
    private Integer payTotalCircle;

    @NotNull(message = "请填写首次收款时间")
    @CopyIgnore
    private Date startPayDate;

    @CopyIgnore
    private Date endDate;

    @NotNull(message = "请填写本金")
    @Min(value = 0, message = "本金设置错误")
    @CopyIgnore
    private Double amount;

    private Boolean hasHolder = false;

    /**
     * 模板名称
     */
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

    @CopyIgnore
    private List<String> customerId;

    @CopyIgnore
    private List<String> thirdCustomerId;

    @CopyIgnore
    private List<String> pawnId;

    @CopyIgnore
    private List<AttachRelateForm> attachs;

    /**
     * 贷款客户
     */
    private CustomerCreateForm loaner;

    /**
     * 担保人
     */
    private CustomerCreateForm holder;

    /**
     * 贷款抵押物
     */
    private LoanPawnCreateForm pawn;

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

    private List<ModifyLoanRecordDomain>  modifyLoanRecordDomains;

    public CustomerCreateForm getHolder() {
        return holder;
    }

    public void setHolder(CustomerCreateForm holder) {
        this.holder = holder;
    }

    public CustomerCreateForm getLoaner() {
        return loaner;
    }

    public void setLoaner(CustomerCreateForm loaner) {
        this.loaner = loaner;
    }

    public LoanPawnCreateForm getPawn() {
        return pawn;
    }

    public void setPawn(LoanPawnCreateForm pawn) {
        this.pawn = pawn;
    }

    public List<AttachRelateForm> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<AttachRelateForm> attachs) {
        this.attachs = attachs;
    }

    public Boolean getHasHolder() {
        return hasHolder;
    }

    public void setHasHolder(Boolean hasHolder) {
        this.hasHolder = hasHolder;
    }

    /**
     * Getter method for property <tt>businessType</tt>.
     *
     * @return property value of businessType
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * Setter method for property <tt>businessType</tt>.
     *
     * @param businessType value to be assigned to property businessType
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     *
     * @param type value to be assigned to property type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter method for property <tt>payWay</tt>.
     *
     * @return property value of payWay
     */
    public String getPayWay() {
        return payWay;
    }

    /**
     * Setter method for property <tt>payWay</tt>.
     *
     * @param payWay value to be assigned to property payWay
     */
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    /**
     * Getter method for property <tt>startDate</tt>.
     *
     * @return property value of startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Setter method for property <tt>startDate</tt>.
     *
     * @param startDate value to be assigned to property startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter method for property <tt>payCircle</tt>.
     *
     * @return property value of payCircle
     */
    public Integer getPayCircle() {
        return payCircle;
    }

    /**
     * Setter method for property <tt>payCircle</tt>.
     *
     * @param payCircle value to be assigned to property payCircle
     */
    public void setPayCircle(Integer payCircle) {
        this.payCircle = payCircle;
    }

    /**
     * Getter method for property <tt>payTotalCircle</tt>.
     *
     * @return property value of payTotalCircle
     */
    public Integer getPayTotalCircle() {
        return payTotalCircle;
    }

    /**
     * Setter method for property <tt>payTotalCircle</tt>.
     *
     * @param payTotalCircle value to be assigned to property payTotalCircle
     */
    public void setPayTotalCircle(Integer payTotalCircle) {
        this.payTotalCircle = payTotalCircle;
    }

    /**
     * Getter method for property <tt>endDate</tt>.
     *
     * @return property value of endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Setter method for property <tt>endDate</tt>.
     *
     * @param endDate value to be assigned to property endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Getter method for property <tt>amount</tt>.
     *
     * @return property value of amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter method for property <tt>amount</tt>.
     *
     * @param amount value to be assigned to property amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter method for property <tt>templateName</tt>.
     *
     * @return property value of templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Setter method for property <tt>templateName</tt>.
     *
     * @param templateName value to be assigned to property templateName
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Getter method for property <tt>penaltyRate</tt>.
     *
     * @return property value of penaltyRate
     */
    public Double getPenaltyRate() {
        return penaltyRate;
    }

    /**
     * Setter method for property <tt>penaltyRate</tt>.
     *
     * @param penaltyRate value to be assigned to property penaltyRate
     */
    public void setPenaltyRate(Double penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    /**
     * Getter method for property <tt>interestRate</tt>.
     *
     * @return property value of interestRate
     */
    public Double getInterestRate() {
        return interestRate;
    }

    /**
     * Setter method for property <tt>interestRate</tt>.
     *
     * @param interestRate value to be assigned to property interestRate
     */
    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Getter method for property <tt>bondPerform</tt>.
     *
     * @return property value of bondPerform
     */
    public Double getBondPerform() {
        return bondPerform;
    }

    /**
     * Setter method for property <tt>bondPerform</tt>.
     *
     * @param bondPerform value to be assigned to property bondPerform
     */
    public void setBondPerform(Double bondPerform) {
        this.bondPerform = bondPerform;
    }

    /**
     * Getter method for property <tt>costService</tt>.
     *
     * @return property value of costService
     */
    public Double getCostService() {
        return costService;
    }

    /**
     * Setter method for property <tt>costService</tt>.
     *
     * @param costService value to be assigned to property costService
     */
    public void setCostService(Double costService) {
        this.costService = costService;
    }

    /**
     * Getter method for property <tt>costOther</tt>.
     *
     * @return property value of costOther
     */
    public Double getCostOther() {
        return costOther;
    }

    /**
     * Setter method for property <tt>costOther</tt>.
     *
     * @param costOther value to be assigned to property costOther
     */
    public void setCostOther(Double costOther) {
        this.costOther = costOther;
    }

    /**
     * Getter method for property <tt>costTemp</tt>.
     *
     * @return property value of costTemp
     */
    public Double getCostTemp() {
        return costTemp;
    }

    /**
     * Setter method for property <tt>costTemp</tt>.
     *
     * @param costTemp value to be assigned to property costTemp
     */
    public void setCostTemp(Double costTemp) {
        this.costTemp = costTemp;
    }

    /**
     * Getter method for property <tt>costTempDesc</tt>.
     *
     * @return property value of costTempDesc
     */
    public String getCostTempDesc() {
        return costTempDesc;
    }

    /**
     * Setter method for property <tt>costTempDesc</tt>.
     *
     * @param costTempDesc value to be assigned to property costTempDesc
     */
    public void setCostTempDesc(String costTempDesc) {
        this.costTempDesc = costTempDesc;
    }

    /**
     * Getter method for property <tt>costOtherDesc</tt>.
     *
     * @return property value of costOtherDesc
     */
    public String getCostOtherDesc() {
        return costOtherDesc;
    }

    /**
     * Setter method for property <tt>costOtherDesc</tt>.
     *
     * @param costOtherDesc value to be assigned to property costOtherDesc
     */
    public void setCostOtherDesc(String costOtherDesc) {
        this.costOtherDesc = costOtherDesc;
    }

    /**
     * Getter method for property <tt>customerId</tt>.
     *
     * @return property value of customerId
     */
    public List<String> getCustomerId() {
        return customerId;
    }

    /**
     * Setter method for property <tt>customerId</tt>.
     *
     * @param customerId value to be assigned to property customerId
     */
    public void setCustomerId(List<String> customerId) {
        this.customerId = customerId;
    }

    /**
     * Getter method for property <tt>thirdCustomerId</tt>.
     *
     * @return property value of thirdCustomerId
     */
    public List<String> getThirdCustomerId() {
        return thirdCustomerId;
    }

    /**
     * Setter method for property <tt>thirdCustomerId</tt>.
     *
     * @param thirdCustomerId value to be assigned to property thirdCustomerId
     */
    public void setThirdCustomerId(List<String> thirdCustomerId) {
        this.thirdCustomerId = thirdCustomerId;
    }

    /**
     * Getter method for property <tt>pawnId</tt>.
     *
     * @return property value of pawnId
     */
    public List<String> getPawnId() {
        return pawnId;
    }

    /**
     * Setter method for property <tt>pawnId</tt>.
     *
     * @param pawnId value to be assigned to property pawnId
     */
    public void setPawnId(List<String> pawnId) {
        this.pawnId = pawnId;
    }

    /**
     * Getter method for property <tt>id</tt>.
     *
     * @return property value of id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     *
     * @param id value to be assigned to property id
     */
    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerDesc() {
        return customerDesc;
    }

    public void setCustomerDesc(String customerDesc) {
        this.customerDesc = customerDesc;
    }

    public Date getStartPayDate() {
        return startPayDate;
    }

    public void setStartPayDate(Date startPayDate) {
        this.startPayDate = startPayDate;
    }

    /**
     * Getter for property 'costSchedule'.
     *
     * @return Value for property 'costSchedule'.
     */
    public Double getCostSchedule() {
        return costSchedule;
    }

    /**
     * Setter for property 'costSchedule'.
     *
     * @param costSchedule Value to set for property 'costSchedule'.
     */
    public void setCostSchedule(Double costSchedule) {
        this.costSchedule = costSchedule;
    }

    /**
     * Getter for property 'costScheduleName'.
     *
     * @return Value for property 'costScheduleName'.
     */
    public String getCostScheduleName() {
        return costScheduleName;
    }

    /**
     * Setter for property 'costScheduleName'.
     *
     * @param costScheduleName Value to set for property 'costScheduleName'.
     */
    public void setCostScheduleName(String costScheduleName) {
        this.costScheduleName = costScheduleName;
    }

    /**
     * Getter for property 'costOtherFirst'.
     *
     * @return Value for property 'costOtherFirst'.
     */
    public Double getCostOtherFirst() {
        return costOtherFirst;
    }

    /**
     * Setter for property 'costOtherFirst'.
     *
     * @param costOtherFirst Value to set for property 'costOtherFirst'.
     */
    public void setCostOtherFirst(Double costOtherFirst) {
        this.costOtherFirst = costOtherFirst;
    }

    /**
     * Getter for property 'costOtherFirstName'.
     *
     * @return Value for property 'costOtherFirstName'.
     */
    public String getCostOtherFirstName() {
        return costOtherFirstName;
    }

    /**
     * Setter for property 'costOtherFirstName'.
     *
     * @param costOtherFirstName Value to set for property 'costOtherFirstName'.
     */
    public void setCostOtherFirstName(String costOtherFirstName) {
        this.costOtherFirstName = costOtherFirstName;
    }

    /**
     * Getter for property 'costOtherFirstDesc'.
     *
     * @return Value for property 'costOtherFirstDesc'.
     */
    public String getCostOtherFirstDesc() {
        return costOtherFirstDesc;
    }

    /**
     * Setter for property 'costOtherFirstDesc'.
     *
     * @param costOtherFirstDesc Value to set for property 'costOtherFirstDesc'.
     */
    public void setCostOtherFirstDesc(String costOtherFirstDesc) {
        this.costOtherFirstDesc = costOtherFirstDesc;
    }

    /**
     * Getter for property 'costOtherSecond'.
     *
     * @return Value for property 'costOtherSecond'.
     */
    public Double getCostOtherSecond() {
        return costOtherSecond;
    }

    /**
     * Setter for property 'costOtherSecond'.
     *
     * @param costOtherSecond Value to set for property 'costOtherSecond'.
     */
    public void setCostOtherSecond(Double costOtherSecond) {
        this.costOtherSecond = costOtherSecond;
    }

    /**
     * Getter for property 'costOtherSecondName'.
     *
     * @return Value for property 'costOtherSecondName'.
     */
    public String getCostOtherSecondName() {
        return costOtherSecondName;
    }

    /**
     * Setter for property 'costOtherSecondName'.
     *
     * @param costOtherSecondName Value to set for property 'costOtherSecondName'.
     */
    public void setCostOtherSecondName(String costOtherSecondName) {
        this.costOtherSecondName = costOtherSecondName;
    }

    /**
     * Getter for property 'costOtherSecondDesc'.
     *
     * @return Value for property 'costOtherSecondDesc'.
     */
    public String getCostOtherSecondDesc() {
        return costOtherSecondDesc;
    }


    public List<ModifyLoanRecordDomain> getModifyLoanRecordDomains() {
        return modifyLoanRecordDomains;
    }

    public void setModifyLoanRecordDomains(List<ModifyLoanRecordDomain> modifyLoanRecordDomains) {
        this.modifyLoanRecordDomains = modifyLoanRecordDomains;
    }

    /**
     * Setter for property 'costOtherSecondDesc'.
     *
     * @param costOtherSecondDesc Value to set for property 'costOtherSecondDesc'.
     */

    public void setCostOtherSecondDesc(String costOtherSecondDesc) {
        this.costOtherSecondDesc = costOtherSecondDesc;
    }



    @Override
    public LoanDomain convert2Domain() {
        LoanDomain loanDomain = new LoanDomain();
        BeanUtil.copyPropertiesWithIgnores(this, loanDomain);
        //~ 枚举类型
        loanDomain.setBusinessType(BusinessTypeEnum.getEnumByCode(businessType));
        loanDomain.setType(LoanPawnTypeEnum.getEnumByCode(type));
        loanDomain.setPayWay(LoanPayWayEnum.getEnumByCode(payWay));

        loanDomain.setAmount(amount);
        loanDomain.setInterestRate(interestRate);

        //~ 获取贷款日期
        loanDomain.setStartDate(startDate);
        loanDomain.setStartPayDate(startPayDate);
        loanDomain.setPayTotalCircle(payTotalCircle);
        loanDomain.setPayCircle(payCircle);

        //计算贷款结束日期
        loanDomain.setFinishDate(LoanDateUtil.getLoanEndDate(startDate, payTotalCircle * payCircle));

        HashMap<String, String> map = new HashMap<>();
        map.put(LoanConstants.COST_OTHER_DESC, StringUtil.defaultIfBlank(costOtherDesc, ""));
        map.put(LoanConstants.COST_TEMP_DESC, StringUtil.defaultIfBlank(costTempDesc, ""));
        map.put(LoanConstants.CUSTOMER_DESC, StringUtil.defaultIfBlank(customerDesc, ""));
        loanDomain.setExtParams(map);

        //加入贷款附件
        if (CollectionUtils.isNotEmpty(attachs)) {
            List<AttachRelateDomain> attachDomains = new ArrayList<>(attachs.size());
            attachs.forEach(attach -> attachDomains.add(attach.convert2Domain()));
            loanDomain.setAttachRelateDomains(attachDomains);
        }
        ;
        // 按期费用
        if (this.costSchedule != null && !StringUtils.isEmpty(this.costScheduleName )) {
            ExtraField costSchedule = new ExtraField(this.costScheduleName, this.costSchedule);
            loanDomain.setCostSchedule(costSchedule);
        }

        // 其他费用1
        if (this.costOtherFirst != null && !StringUtils.isEmpty(this.costOtherFirstName)) {
            ExtraField costOtherFirst = new ExtraField(this.costOtherFirstName, this.costOtherFirst, this.costOtherFirstDesc);
            loanDomain.setCostOtherFirst(costOtherFirst);
        }

        // 其他费用2
        if (this.costOtherSecond != null && !StringUtils.isEmpty(this.costOtherSecondName)) {
            ExtraField costOtherSecond = new ExtraField(this.costOtherSecondName, this.costOtherSecond, this.costOtherSecondDesc);
            loanDomain.setCostOtherSecond(costOtherSecond);
        }

        //等额本息需要提前结清违约金率
        //        if (loanDomain.getPayWay() == LoanPayWayEnum.EVEN_PRINCIPAL_INTEREST) {
        //            if (PenaltyRate() == null) {
        //                throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL.getCode(),
        //                    "提前结清违约金率不能为空!");
        //            } else {
        //                loanDomain.setPenaltyRate(PenaltyRate());
        //            }
        //        } else {
        //            loanDomain.setPenaltyRate(0.00);
        //        }

        return loanDomain;
    }
}
