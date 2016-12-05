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
import tiger.biz.loan.support.LoanNotificationSettingManager;
import tiger.biz.sms.support.SmsManager;
import tiger.core.domain.CustomerMsgScheduleDomain;
import tiger.core.service.SmsService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 6:43 PM yiliang.gyl Exp $
 */
@Component("SmsScheduler")
public class SmsScheduler {

    private static final Logger logger = Logger.getLogger(SmsScheduler.class);
    // 线程池
    private final ExecutorService e = Executors.newCachedThreadPool();

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsManager smsManager;

    @Autowired
    private LoanNotificationSettingManager loanNotificationSettingManager;

    /**
     * 短信扫描任务
     * ~ 每 1 分钟扫描一次
     * ~ 发送 1分10秒前 至 当前时间 内所有未发送的定时短信
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void schedulerSmsScanner() {
        Calendar cal = Calendar.getInstance();
        Date toTime = new Date(System.currentTimeMillis());

        if (logger.isInfoEnabled()) {
            logger.info("开始扫描未发送的定时短信");
        }

        cal.setTime(toTime);
        cal.add(Calendar.MINUTE, -1);
        cal.add(Calendar.SECOND, -10);
        Date fromTime = cal.getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<CustomerMsgScheduleDomain> smses = smsService.getTosendScheduleSmsBetweenTime(fromTime, toTime);

        if (CollectionUtils.isNotEmpty(smses)) {
            logger.info("发送" + format.format(fromTime) + "至" + format.format(toTime) + "间的定时短信, 共 [" + smses.size() + "] 条");
        }

        for (CustomerMsgScheduleDomain sms : smses) {
            e.execute(() -> {
                smsManager.sendScheduler(sms.getSenderId(), sms.getId());
                if (logger.isInfoEnabled()) {
                    logger.info("发送定时短信No. " + sms.getId());
                }
            });
        }

        if (logger.isInfoEnabled()) {
            logger.info("结束扫描未发送的定时短信");
        }
    }

    /**
     * 贷款短信扫描任务
     * ~ 每天 12 点开始扫描
     */
    @Scheduled(cron = "0 1 12 * * ?")
    public void loanSmsScanner() {
        if (logger.isInfoEnabled()) {
            logger.info("开始扫描贷款短信任务");
        }

        loanNotificationSettingManager.scanLoanSmsNotification(new Date());

        if (logger.isInfoEnabled()) {
            logger.info("结束扫描贷款短信任务");
        }
    }


}
