package tiger.web.api.form.loan;

import tiger.web.api.form.BaseForm;

import javax.validation.constraints.NotNull;

/**
 * Created by Jaric Liao on 2016/3/26.
 */
public class LoanAssignmentForm extends BaseForm {

    @NotNull(message = "请指定目标用户")
    private Long targetId;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
