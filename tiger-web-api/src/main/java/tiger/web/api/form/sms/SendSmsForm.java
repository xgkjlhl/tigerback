/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.sms;

import tiger.web.api.form.BaseForm;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author zhangbin
 * @version v0.1 2015/10/6 15:34
 */
public class SendSmsForm extends BaseForm {

    @NotNull
    Long[] customerIds;

    /**
     * 短信内容
     */
    @NotNull
    Long msgTplId;

    /**
     * 短信发送时间
     */
    Timestamp dateToSendMsg;

    /**
     * 是否同意短信发送协议
     */
    @NotNull
    Boolean agreeLiscense;


    public Long getMsgTplId() {
        return msgTplId;
    }

    public void setMsgTplId(Long msgTplId) {
        this.msgTplId = msgTplId;
    }

    public Timestamp getDateToSendMsg() {
        return dateToSendMsg;
    }

    public void setDateToSendMsg(Timestamp dateToSendMsg) {
        this.dateToSendMsg = dateToSendMsg;
    }

    public Boolean getAgreeLiscense() {
        return agreeLiscense;
    }

    public void setAgreeLiscense(Boolean agreeLiscense) {
        this.agreeLiscense = agreeLiscense;
    }

    public Long[] getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }

}
