package tiger.web.mng.controller.base;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import tiger.core.domain.StaffDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

/**
 * @author HuPeng
 * @version v 0.1 2015年10月15日 下午8:30:17 HuPeng Exp $
 */
public class BaseController {
    private static Logger logger = Logger.getLogger(BaseController.class);

    public Subject currentUser() {
        return SecurityUtils.getSubject();
    }

    public StaffDomain currentAccount() {
        StaffDomain currentUser = (StaffDomain) currentUser().getPrincipal();
        /**如果是未登录用户，直接抛异常返回*/
        if (currentUser == null) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED, "未登录");
        }
        return currentUser;
    }
}
