/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.enums.InvitationCodeTypeEnum;
import tiger.core.AbstractCoreTests;
import tiger.core.domain.InvitationCodeDomain;

import java.util.List;

/**
 * @author alfred.yx
 * @version $ID: v 0.1 上午11:10 alfred.yx Exp $
 */
public class InvitationCodeServiceTest extends AbstractCoreTests {
    private final Long ACCOUNT_ID = 2L;
    @Autowired
    AccountService accountService;
    @Autowired
    InvitationCodeService invitationCodeService;
    private String ACTIVE_CODE;
    private String EXPIRED_CODE;

    private void setUPCode() {
        logger.info("生成测试注册码");
        accountService.read(ACCOUNT_ID);
        List<InvitationCodeDomain> codeDomainList;
        // 生成2个邀请码
        codeDomainList = invitationCodeService.generateInvitationCodes(ACCOUNT_ID, 2, InvitationCodeTypeEnum.REGISTER);

        ACTIVE_CODE = codeDomainList.get(0).getCode();

        EXPIRED_CODE = codeDomainList.get(1).getCode();
        invitationCodeService.useInvitationCode(ACCOUNT_ID, EXPIRED_CODE);
    }

    @Test
    public void testGenerateInviationCodes() {
        List<InvitationCodeDomain> codeDomainList;
        // 生成10个邀请码
        codeDomainList = invitationCodeService.generateInvitationCodes(ACCOUNT_ID, 10, InvitationCodeTypeEnum.REGISTER);

        Assert.assertEquals(10, codeDomainList.size());

        for (InvitationCodeDomain code : codeDomainList) {
            System.out.println(code.getCode());
            Assert.assertEquals(false, code.getIsExpired());
            Assert.assertEquals(InvitationCodeTypeEnum.REGISTER, code.getType());
        }
    }

    @Test
    public void testIsActive() {
        setUPCode();

        boolean result = invitationCodeService.isActive(EXPIRED_CODE);

        Assert.assertEquals(false, result);

        result = invitationCodeService.isActive(ACTIVE_CODE);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testRead() {
        setUPCode();

        InvitationCodeDomain codeDomain = invitationCodeService.readByCode(EXPIRED_CODE);
        Assert.assertEquals(EXPIRED_CODE, codeDomain.getCode());
        Assert.assertEquals(ACCOUNT_ID, codeDomain.getAccountId());
    }

    @Test
    public void testUseInvitationCode() {
        setUPCode();

        boolean result = invitationCodeService.useInvitationCode(ACCOUNT_ID, EXPIRED_CODE);
        Assert.assertEquals(false, result);

        result = invitationCodeService.useInvitationCode(ACCOUNT_ID, ACTIVE_CODE);
        InvitationCodeDomain domain = invitationCodeService.readByCode(ACTIVE_CODE);
        Assert.assertEquals(true, result);
        Assert.assertEquals(true, domain.getIsExpired());
    }

    @Test
    public void testReadNull() {
        InvitationCodeDomain nullDomain = invitationCodeService.readByCode(null);
        Assert.assertEquals(null, nullDomain);
    }

    @Test
    public void testIsActiveNull() {
        boolean result = invitationCodeService.isActive(null);
        Assert.assertEquals(false, false);
    }

}
