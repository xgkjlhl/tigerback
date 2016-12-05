package tiger.web.mng;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tiger.biz.mng.staff.support.StaffManager;
import tiger.common.util.StringUtil;
import tiger.core.domain.PermissionDomain;
import tiger.core.domain.RoleDomain;
import tiger.core.domain.StaffDomain;

/**
 * Created by HuPeng on 2015/9/1.
 */
@Component("shiroRealm")
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private StaffManager staffManager;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    ;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        StaffDomain staff = (StaffDomain) principals.fromRealm(getName()).iterator().next();
        if (null != staff) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (RoleDomain role : staff.getRoles()) {
                info.addRole(role.getName().getCode());
            }
            for (PermissionDomain permission : staff.getPermissions()) {
                info.addStringPermission(permission.toString());
            }
            return info;
        }
        return null;
    }

    /**
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        if (StringUtil.isNotEmpty(username)) {
            String password = new String(token.getPassword());
            StaffDomain staff = staffManager.login(username, password);
            if (null != staff) {
                return new SimpleAuthenticationInfo(staff, password, getName());
            }
        }
        return null;
    }
}
