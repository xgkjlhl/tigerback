package tiger.core.service;

import tiger.core.domain.AccountLoginLogDomain;

/**
 * Created by Administrator on 2015/10/9.
 */
public interface LoginLogService {

    /**
     * 通过token获取accountId
     *
     * @param token
     * @return
     */
    long getAccountIdByToken(String token);

    /**
     * 创建登录记录
     *
     * @param loginLogDomain
     */
    void createLog(AccountLoginLogDomain loginLogDomain);

}
