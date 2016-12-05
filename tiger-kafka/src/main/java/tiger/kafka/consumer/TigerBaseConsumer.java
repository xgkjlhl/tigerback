/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.kafka.consumer;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.biz.account.support.AccountManager;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.CustomerDomain;
import tiger.core.domain.LoanLightDomain;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.domain.workspace.WorkspaceDomain;
import tiger.core.service.CustomerService;
import tiger.core.service.LoanService;
import tiger.core.service.workspace.WorkspaceService;
import tiger.kafka.consumer.config.BaseConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author alfred_yuan
 * @version v 0.1 16:43 alfred_yuan Exp $
 */
public abstract class TigerBaseConsumer {

    protected final BaseConfig config;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private WorkspaceService workspaceService;

    protected Map<Long, AccountDomain> accountMap;
    protected Map<Long, CustomerDomain> customerMap;
    protected Map<Long, LoanLightDomain> loanLightMap;
    protected Map<Long, WorkspaceDomain> workspaceMap;

    public TigerBaseConsumer(BaseConfig config) {
        this.config = config;
    }

    /**
     * 暴露出的方法
     *
     * @param notificationMap
     */
    public void run(Map<NotificationBasicDomain, NotificationKeyEnum> notificationMap) {
        getBasicData(notificationMap);

        consume(notificationMap);
    }

    /**
     * 真正的处理message
     */
    protected abstract void consume(Map<NotificationBasicDomain, NotificationKeyEnum> notificationMap);

    /**
     * 获取基本信息
     *
     * @param notificationMap
     */
    protected void getBasicData(Map<NotificationBasicDomain, NotificationKeyEnum> notificationMap) {
        // 准备查询 workspace, account, loan, customer 的参数
        List<Long> accountIds = new ArrayList<>();
        List<Long> loanIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> workspaceIds = new ArrayList<>();

        notificationMap.forEach((mapBaseDomain, mapKeyEnum) -> {
            accountIds.add(mapBaseDomain.getAccountId());
            workspaceIds.add(mapBaseDomain.getWorkspaceId());
            switch (mapKeyEnum) {
                case LOAN:
                    loanIds.add(mapBaseDomain.getSubjectId());
                    break;
                case CUSTOMER:
                    customerIds.add(mapBaseDomain.getSubjectId());
                    break;
                default:
                    break;
            }
        });

        this.accountMap = new HashedMap();
        this.customerMap = new HashedMap();
        this.loanLightMap = new HashedMap();
        this.workspaceMap = new HashedMap();
        // 查询 workspace, account, loan, customer
        // 并放入对应的map当中
        accountManager.getAccountsByIds(accountIds).forEach(accountDomain -> this.accountMap.put(accountDomain.getId(), accountDomain));
        customerService.readAll(customerIds).forEach(customerDomain -> this.customerMap.put(customerDomain.getId(), customerDomain));
        loanService.lightBatchReadAll(loanIds).forEach(loanLightDomain -> this.loanLightMap.put(loanLightDomain.getId(), loanLightDomain));
        workspaceService.batchRead(workspaceIds).forEach(workspaceDomain -> this.workspaceMap.put(workspaceDomain.getId(), workspaceDomain));
    }
}
