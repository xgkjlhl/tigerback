package tiger.core.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.dataobject.AccountBaseDO;
import tiger.common.dal.enums.GenderEnum;
import tiger.core.AbstractCoreTests;
import tiger.core.domain.AccountBaseDomain;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.CompanyDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhenyu Liao on 2015/12/1.
 */
public class AccountServiceTests extends AbstractCoreTests {
    @Autowired
    AccountService accountService;
    @Autowired
    CompanyService companyService;

    AccountDomain accountDomain;

    @Before
    public void createNewAccount() {
        accountDomain = new AccountDomain();
        accountDomain.setUserName("testAccount");
        accountDomain.setPassword("testtesttest");
        accountDomain.setGender(GenderEnum.MALE);
        accountDomain.setMobile("18900010001");
        CompanyDomain companyDomain = new CompanyDomain();
        companyService.addCompany(companyDomain);
        accountDomain.setCompanyId(companyDomain.getId());
    }

    @Test
    public void testAddAccount() {
        AccountDomain result = accountService.addAccount(accountDomain);
        Assert.assertEquals("��֤�����˻�����-�˻���", accountDomain.getUserName(), result.getUserName());
    }

    @Test
    public void testGetBaseInfo() {
        AccountBaseDomain result = accountService.getBaseInfo(3L);
        Assert.assertEquals("赵日天",  result.getUserName());
    }

    @Test
    public void testGetBaseInfos() {
        List<Long> tmp = new ArrayList<>();
        tmp.add(2L);
        tmp.add(3L);
        List<AccountBaseDomain> results = accountService.getBaseInfos(tmp);
        Assert.assertEquals("赵日天",  results.get(1).getUserName());
        Assert.assertEquals("Li Mi",  results.get(0).getUserName());
    }
}
