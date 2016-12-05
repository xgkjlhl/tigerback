/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.util;

import org.apache.log4j.Logger;
import org.junit.Test;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.common.util.JsonUtil;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.workspace.AccountWorkspaceDomain;

import java.util.Date;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 11:58 PM yiliang.gyl Exp $
 */
public class JsonUtilTest {

    private static Logger logger = Logger.getLogger(JsonUtilTest.class);

    @Test
    public void testFromJsonForEnum() {

        LoanDomain loanDomain = new LoanDomain();
        loanDomain.setBusinessType(BusinessTypeEnum.BORROW);
        loanDomain.setId(1l);

        String jsonStr = JsonUtil.toJson(loanDomain);

        logger.info("转换之前的对象 [" + jsonStr + "]");

        LoanDomain loanDomain1 = JsonUtil.fromJson(jsonStr, LoanDomain.class);

        logger.info("转换回来后的对象 [" + loanDomain1 + "]");
    }


    @Test
    public void testFromJsonDate() {
        AccountDomain accountDomain = new AccountDomain();

        accountDomain.setUpdateTime(new Date());
        accountDomain.setCreateTime(new Date());

        String conetnt = JsonUtil.toJson(accountDomain);

        logger.info("时间转换结果 [" + conetnt + "]");

        AccountDomain newDomain = JsonUtil.fromJson(conetnt, AccountDomain.class);

        logger.info("转换回来的结果 [" + newDomain + "]");

    }

    @Test
    public void testTransferEnumList(){
        AccountWorkspaceDomain accountWorkspace = new AccountWorkspaceDomain();
        accountWorkspace.getPermissions().add(PermissionEnum.DELETE_WORKSPACE);

        String content = JsonUtil.toJson(accountWorkspace);

        logger.info("时间转换结果 [" + content + "]");

        AccountWorkspaceDomain newDomain = JsonUtil.fromJson(content, AccountWorkspaceDomain.class);

        logger.info("转换回来的结果 [" + newDomain + "]");

    }
}
