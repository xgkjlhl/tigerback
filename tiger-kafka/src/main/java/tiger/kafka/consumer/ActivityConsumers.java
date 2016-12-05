package tiger.kafka.consumer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.biz.activity.support.ActivityManager;
import tiger.common.dal.enums.BizObjectTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.dal.enums.WorkSpaceTypeEnum;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.domain.workspace.ActivityDomain;
import tiger.core.domain.workspace.WorkspaceDomain;
import tiger.kafka.consumer.config.BaseConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by domicc on 5/4/16.
 */
public class ActivityConsumers extends TigerBaseConsumer {

    private static final Logger LOGGER = Logger.getLogger(ActivityConsumers.class);

    @Autowired
    private ActivityManager activityManager;

    public ActivityConsumers(BaseConfig config) {
        super(config);
    }

    @Override
    public void consume(Map<NotificationBasicDomain, NotificationKeyEnum> notificationMap) {
        List<ActivityDomain> activities = new ArrayList<>();
        // 逐个创建activity
        notificationMap.forEach((mapBaseDomain, mapKeyEnum) -> {
            if (config.isSupport(mapBaseDomain.getTypeEnum())) {
                AccountDomain accountDomain = accountMap.get(mapBaseDomain.getAccountId());
                Long subjectId = mapBaseDomain.getSubjectId();
                WorkspaceDomain workspaceDomain = workspaceMap.get(mapBaseDomain.getWorkspaceId());
                String operatorName = workspaceDomain.getType() == WorkSpaceTypeEnum.PERSONAL ?
                        SystemConstants.PERSONAL_WORKSPACE_OPERATOR_NAME : accountDomain.getUserName();

                ActivityDomain activityDomain = new ActivityDomain();
                activityDomain.setOperatorId(mapBaseDomain.getAccountId());
                activityDomain.setWorkspaceId(mapBaseDomain.getWorkspaceId());
                activityDomain.setOperation(mapBaseDomain.getTypeEnum());
                activityDomain.setOperationParams(mapBaseDomain.getOperationParams());
                activityDomain.setObjectId(subjectId);
                activityDomain.setOperatorName(operatorName);
                activityDomain.setOperatorAvatarId(accountDomain.getIconId());
                switch (mapKeyEnum) {
                    case LOAN:
                        activityDomain.setObjectType(BizObjectTypeEnum.LOAN);
                        activityDomain.setObjectName(loanLightMap.get(subjectId).getKeyId());
                        activities.add(activityDomain);
                        break;
                    case CUSTOMER:
                        activityDomain.setObjectType(BizObjectTypeEnum.CUSTOMER);
                        activityDomain.setObjectName(customerMap.get(subjectId).getName());
                        activities.add(activityDomain);
                        break;
                    case WORKSPACE:
                        activityDomain.setObjectId(mapBaseDomain.getWorkspaceId());
                        activityDomain.setObjectType(BizObjectTypeEnum.WORKSPACE);
                        activityDomain.setObjectName(workspaceDomain.getName());
                        activities.add(activityDomain);
                        break;
                    default:
                        LOGGER.error("kafka consumer a record without key; key: [" + mapKeyEnum + "], value: [" + mapBaseDomain + "]");
                        break;
                }
            }

        });

        // 一次性创建所有的activities
        activityManager.createAll(activities);
    }
}
