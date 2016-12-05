/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.kafka.scheduler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tiger.common.dal.enums.BizObjectTypeEnum;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.MessageDomain;
import tiger.core.domain.workspace.WorkspaceDomain;
import tiger.core.service.LoanService;
import tiger.core.service.MessageService;
import tiger.core.service.workspace.WorkspaceService;

import java.util.*;

/**
 * @author mi.li
 * @version v 0.2 2016-05-30 13:49:15
 */
@Component("DailyNotifyScheduler")
public class DailyNotifyScheduler {

    // 合同逾期提醒
    public static final String ONE_DAY_OVERDUE_NOTIFICATION = "已经逾期1天了, 请尽快催收。";
    public static final String OVERDUE_NOTIFICATION_FORMAT = "已经逾期%d天了, 已超过黄色逾期天数%d了, 请尽快催收。";
    public static final String BAD_LOAN_NOTIFICATION_FORMAT = "已经逾期%d天了, 已超过红色警告天数%d了, 请尽快催收。";
    // 一天的毫秒数
    public static final int DAY_MILLISECONDS = 1000 * 3600 * 24;

    private static Logger logger = Logger.getLogger(SmsScheduler.class);

    @Autowired
    private LoanService loanService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 每日提醒扫描任务
     * ~ 每天8：00扫描一次
     */
    @Scheduled(cron = "00 00 8 * * ?")
    public void schedulerDailyNotifyScaner() {
        logger.info("开始每日提醒扫描任务");

        sendTodayCollectNotification();

        sendThreeDayCollectNotification();

        sendOverDueNotification();

        logger.info("结束每日提醒扫描任务");
    }

    /**
     * 当天收款提醒
     */
    private void sendTodayCollectNotification() {
        if (logger.isInfoEnabled()) {
            logger.info("开始扫描当天收款提醒");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));

        String title = "你的贷款合同";
        String description = "今天就要收款了，请注意催收。";
        String content = "";
        MessageDomain messageDomain;

        List<MessageDomain> todayMessages = new ArrayList<>();
        List<LoanDomain> loanDomains = filterLoans(loanService.getPayProcessLoansAtOperateDate(cal.getTime()));
        Map<Long, String> workspaceIdNameMap = getWorkspaceIdNameMap(getWorkspaceIds(loanDomains));
        for (LoanDomain loanDomain : loanDomains) {
            messageDomain = getMessageDomain(title, description, content, workspaceIdNameMap, loanDomain);
            todayMessages.add(messageDomain);

            if (logger.isInfoEnabled()) {
                logger.info("Loan [" + loanDomain.getId() + "] 发送当天还款提醒至 Account [" + loanDomain.getAccountId() + "]");
            }
        }

        if (CollectionUtils.isNotEmpty(todayMessages)) {
            messageService.sendMessages(todayMessages);
        }

        if (logger.isInfoEnabled()) {
            logger.info("结束扫描当天收款提醒");
        }
    }

    /**
     * 提前三天收款提醒
     */
    private void sendThreeDayCollectNotification() {
        if (logger.isInfoEnabled()) {
            logger.info("开始扫描提前三天收款提醒");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.DATE, +3);
        Date threeDayLater = cal.getTime();

        String title = "你的贷款合同";
        String description = "还有三天就要收款了，请注意催收。";
        String content = "";
        MessageDomain messageDomain;

        List<MessageDomain> threeDayMessages = new ArrayList<>();
        List<LoanDomain> loanDomains = filterLoans(loanService.getPayProcessLoansAtOperateDate(threeDayLater));
        Map<Long, String> workspaceIdNameMap = getWorkspaceIdNameMap(getWorkspaceIds(loanDomains));
        for (LoanDomain loanDomain : loanDomains) {
            messageDomain = getMessageDomain(title, description, content, workspaceIdNameMap, loanDomain);
            threeDayMessages.add(messageDomain);

            if (logger.isInfoEnabled()) {
                logger.info("Loan [" + loanDomain.getId() + "] 发送提前三天还款提醒至 Account [" + loanDomain.getAccountId() + "]");
            }
        }

        if (CollectionUtils.isNotEmpty(threeDayMessages)) {
            messageService.sendMessages(threeDayMessages);
        }

        if (logger.isInfoEnabled()) {
            logger.info("结束扫描提前三天收款提醒");
        }
    }

    /**
     * 逾期提醒
     */
    private void sendOverDueNotification() {
        if (logger.isInfoEnabled()) {
            logger.info("开始扫描逾期提醒");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));

        String title = "你的贷款合同";
        String description;
        String content = "";
        int loanOverdueDay, worksapceOverdueDay, workspaceBadloanDay;
        Map<String, String> workspaceExtParm;
        MessageDomain messageDomain;

        // 获取逾期贷款
        List<MessageDomain> overdueMessages = new ArrayList<>();
        List<LoanDomain> loanDomains = filterLoans(loanService.getPayProcessLoansBeforeOperateDate(cal.getTime()));

        // 获取 工作空间 逾期坏账设置
        List<Long> workspaceIds = getWorkspaceIds(loanDomains);
        Map<Long, String> workspaceIdNameMap = getWorkspaceIdNameMap(workspaceIds);
        Map<Long, Map<String, String>> workspaceExtParamMap = workspaceService.getExtParamsByIds(workspaceIds);
        for (LoanDomain loanDomain : loanDomains) {
            // 获取工作空间的 逾期,坏账 提醒
            workspaceExtParm = workspaceExtParamMap.get(loanDomain.getWorkspaceId());
            if (workspaceExtParm == null) {
                continue;
            }

            worksapceOverdueDay = Integer.valueOf(workspaceExtParm.get(SystemConstants.OVER_DUE_DAY));
            workspaceBadloanDay = Integer.valueOf(workspaceExtParm.get(SystemConstants.BAD_LOAN_DAY));

            //计算逾期天数
            loanOverdueDay = Long.valueOf((cal.getTime().getTime() - loanDomain.getOperateDate().getTime()) / DAY_MILLISECONDS)
                    .intValue();

            // 只提醒 逾期一天 或 逾期 或 坏账 的合同
            if (loanOverdueDay == workspaceBadloanDay) {
                description = String.format(BAD_LOAN_NOTIFICATION_FORMAT, loanOverdueDay, workspaceBadloanDay);
            } else if (loanOverdueDay == worksapceOverdueDay) {
                description = String.format(OVERDUE_NOTIFICATION_FORMAT, loanOverdueDay, worksapceOverdueDay);
            } else if (loanOverdueDay == 1) {
                description = ONE_DAY_OVERDUE_NOTIFICATION;
            } else {
                continue;
            }

            messageDomain = getMessageDomain(title, description, content, workspaceIdNameMap, loanDomain);
            overdueMessages.add(messageDomain);

            if (logger.isInfoEnabled()) {
                logger.info("Loan [" + loanDomain.getId() + "] 发送逾期" + loanOverdueDay + "天提醒至 Account [" + loanDomain.getAccountId() + "]");
            }
        }

        if (CollectionUtils.isNotEmpty(overdueMessages)) {
            messageService.sendMessages(overdueMessages);
        }

        if (logger.isInfoEnabled()) {
            logger.info("结束扫描逾期提醒");
        }
    }

    /**
     * 生成默认的贷款提醒
     *
     * @param title
     * @param description
     * @param content
     * @param workspaceIdNameMap
     * @param loanDomain
     * @return
     */
    private MessageDomain getMessageDomain(String title, String description, String content, Map<Long, String> workspaceIdNameMap, LoanDomain loanDomain) {
        MessageDomain messageDomain = new MessageDomain();
        messageDomain.setType(MessageTypeEnum.LOAN_NOTIFICATION);
        messageDomain.setBizId(loanDomain.getId());
        messageDomain.setTitle(title);
        messageDomain.setContent(content);
        messageDomain.setDescription(description);
        messageDomain.setReceiverId(loanDomain.getAccountId());
        messageDomain.setWorkspaceId(loanDomain.getWorkspaceId());
        messageDomain.setWorkspaceName(workspaceIdNameMap.get(loanDomain.getWorkspaceId()));
        messageDomain.setBizType(BizObjectTypeEnum.LOAN);
        messageDomain.setBizName(loanDomain.getKeyId());
        messageDomain.setSenderId(SystemConstants.ZERO_LONG);
        return messageDomain;
    }

    /**
     * 获取loanDomains中的workspaceId列表
     *
     * @param loanDomains
     * @return
     */
    private List<Long> getWorkspaceIds(List<LoanDomain> loanDomains) {
        List<Long> workspaceIds = new ArrayList<>();
        loanDomains.forEach(loanDomain -> workspaceIds.add(loanDomain.getWorkspaceId()));
        return workspaceIds;
    }

    /**
     * 根据id获取id -> name的map索引
     *
     * @param ids
     * @return
     */
    private Map<Long, String> getWorkspaceIdNameMap(List<Long> ids) {
        List<WorkspaceDomain> workspaceDomains = workspaceService.batchRead(ids);

        Map<Long, String> workspaceIdNameMap = new HashMap<>(workspaceDomains.size());
        workspaceDomains.forEach(workspaceDomain ->
                workspaceIdNameMap.put(workspaceDomain.getId(), workspaceDomain.getName()));

        return workspaceIdNameMap;
    }

    private List<LoanDomain> filterLoans(List<LoanDomain> loanDomains) {
        if (CollectionUtils.isEmpty(loanDomains)) {
            return new ArrayList<>();
        }

        Set<Long> accountIdSet = new HashSet<>();
        loanDomains.forEach(loanDomain -> accountIdSet.add(loanDomain.getAccountId()));

        Map<Long, List<Long>> accountWorkspaceIdMap = workspaceService.getAccountWorkspaceIdMap(accountIdSet);

        List<LoanDomain> filteredLoanDomains = new ArrayList<>(loanDomains.size());
        loanDomains.forEach(loanDomain -> {
            if (accountWorkspaceIdMap.get(loanDomain.getAccountId()).contains(loanDomain.getWorkspaceId())) {
                filteredLoanDomains.add(loanDomain);
            }
        });

        return filteredLoanDomains;
    }

}
