package tiger.core.domain.convert;

import tiger.common.dal.dataobject.AccountBaseDO;
import tiger.common.util.BeanUtil;
import tiger.common.util.JsonConverterUtil;
import tiger.common.util.JsonUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.AccountBaseDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kris Chan on 4:14 PM 3/27/16 .
 * All right reserved.
 */
public class AccountBaseConvert {
    /**
     * @param accountBaseDO
     * @return
     */
    public static AccountBaseDomain convert2Domain(AccountBaseDO accountBaseDO) {
        if (accountBaseDO == null) {
            return null;
        }
        AccountBaseDomain baseDomain = new AccountBaseDomain();

        baseDomain.setId(accountBaseDO.getId());
        baseDomain.setIconId(accountBaseDO.getIconId());
        baseDomain.setUserName(accountBaseDO.getUserName());
        if (StringUtil.isNotBlank(accountBaseDO.getExtParams())) {
            baseDomain.setExtParams(JsonUtil.fromJson(accountBaseDO.getExtParams(), HashMap.class));
        }

        return baseDomain;
    }

    /**
     * @param accountBaseDomain
     * @return
     */
    public static AccountBaseDO convert2DO(AccountBaseDomain accountBaseDomain) {
        if (accountBaseDomain == null) {
            return null;
        }
        AccountBaseDO accountBaseDO = new AccountBaseDO();

        accountBaseDO.setId(accountBaseDO.getId());
        accountBaseDO.setIconId(accountBaseDO.getIconId());
        accountBaseDO.setUserName(accountBaseDO.getUserName());
        if (null != accountBaseDomain.getExtParams()) {
            accountBaseDO.setExtParams(JsonConverterUtil.serialize(accountBaseDomain.getExtParams()));
        }

        return accountBaseDO;
    }

    /**
     * @param accountBaseDOs
     * @return
     */
    public static List<AccountBaseDomain> convert2Domain(List<AccountBaseDO> accountBaseDOs) {
        List<AccountBaseDomain> accountBaseDomains = new ArrayList<>();
        for (AccountBaseDO accountBaseDO : accountBaseDOs) {
            accountBaseDomains.add(convert2Domain(accountBaseDO));
        }
        return accountBaseDomains;
    }
}
