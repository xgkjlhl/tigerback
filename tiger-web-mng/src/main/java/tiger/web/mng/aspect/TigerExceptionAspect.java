package tiger.web.mng.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import tiger.core.base.PageResult;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 统一异常抓取
 */
@Aspect
@Component
@Order(1)
public class TigerExceptionAspect {

    /**
     * The Constant el.
     */
    private final static String el = "@annotation(org.springframework.web.bind.annotation.RequestMapping)";
    private static Logger logger = Logger.getLogger(TigerExceptionAspect.class);

    @Autowired
    private HttpServletRequest request;

    @Before(el)
    public void before() {

    }

    /**
     * 统一异常处理函数
     * ~ 可以处理基本返回异常 (BaseResult) 和 PageResult 返回异常
     *
     * @param p
     * @return
     */
    @Around(el)
    public Object around(ProceedingJoinPoint p) {
        boolean isPageResult = false;
        if (p.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) p.getSignature();
            if (signature.getReturnType() == PageResult.class) {
                isPageResult = true;
            }
        }
        try {
            return p.proceed();
        } catch (TigerException e) { //拦截tiger异常
            fillError4Display(e.getMessage());
            // TODO 后面暂时不知道如何处理

        } catch (DataAccessException | SQLException e) { //拦截数据库异常
            fillError4Display(ErrorCodeEnum.DB_EXCEPTION.getMessage());
            // 后面暂时不知道如何处理

        } catch (Throwable e) { //拦截其他异常
            fillError4Display(ErrorCodeEnum.UNKNOW_ERROR.getMessage());
            // 后面暂时不知道如何处理
        }
        return null;
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

    private void fillDefaultError() {
        fillError4Display("系统忙，请稍候再试。");
    }

    private void fillError4Display(String msg) {
        request.setAttribute("_error_msg_", msg);
    }

}
