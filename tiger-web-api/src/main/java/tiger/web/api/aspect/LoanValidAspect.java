package tiger.web.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tiger.common.dal.annotation.LoanValidCheck;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.core.domain.LoanDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.LoanService;
import tiger.web.api.constants.APIConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * 1. 检查贷款项目是否属于当前工作组
 * 2. 访问已删除的贷款项目拦截
 * <p>
 * Created by Jaric Liao on 2016/1/9.
 */
@Aspect
@Component
public class LoanValidAspect {
    /**
     * The Constant el.
     */
    private final String el = "@annotation(tiger.common.dal.annotation.LoanValidCheck)";

    @Autowired
    private LoanService loanService;

    @Autowired
    private HttpServletRequest request;

    @Before(el)
    public void before() {

    }

    @Around(el)
    public Object around(ProceedingJoinPoint p) throws Throwable {
        LoanValidCheck loanValidCheck = ((MethodSignature) p.getSignature()).getMethod()
                .getAnnotation(LoanValidCheck.class);

        Object[] args = p.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long) {
                Long loadId = (Long) arg;

                LoanDomain loanDomain = loanService.read(loadId);

                if (loanDomain == null || !loanDomain.getWorkspaceId().equals(getWorkspaceId())) {
                    throw new TigerException(ErrorCodeEnum.NOT_FOUND, "不存在的贷款合同");
                }
                if (loanValidCheck.isTrashChecked() && loanDomain.getLoanStatus().equals(LoanStatusEnum.TRASH)) {
                    throw new TigerException(ErrorCodeEnum.BIZ_STATUS_ERROR, "该笔贷款合同处于垃圾箱状态,不能进行相关操作");
                }

                break;
            }
        }
        return p.proceed();
    }

    @After(el)
    public void after() {

    }

    /**
     * Throwing.
     *
     * @param e the e
     */
    @AfterThrowing(value = el, throwing = "e")
    public void throwing(Exception e) {
        e.printStackTrace();
    }

    private Long getWorkspaceId() {
        try {
            return Long.valueOf(request.getHeader(APIConstants.HEADER_WORKSPACE_ID));
        } catch (Exception e) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_HTTP_REQUEST);
        }
    }

}
