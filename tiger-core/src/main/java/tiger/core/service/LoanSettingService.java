/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.enums.LoanSettingTypeEnum;
import tiger.core.domain.LoanSettingDomain;

import java.util.List;

/**
 * 贷款设置服务类接口
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 8:15 PM yiliang.gyl Exp $
 */
public interface LoanSettingService {

    /**
     * 插入贷款设置
     *
     * @param loanSettingDomain
     * @return
     */
    LoanSettingDomain insert(LoanSettingDomain loanSettingDomain);

    /**
     * 删除贷款设置
     *
     * @param settingId
     * @return
     */
    Boolean delete(Long settingId);

    /**
     * 更新设置
     *
     * @param loanSettingDomain
     * @return
     */
    LoanSettingDomain update(LoanSettingDomain loanSettingDomain);

    /**
     * 获取设置
     *
     * @param settingId
     * @return
     */
    LoanSettingDomain read(Long settingId);


    /**
     * 通过设置类型和贷款合同主键查询设置领域模型
     *
     * @param typeEnum
     * @param loanId
     * @return
     */
    List<LoanSettingDomain> findByTypeAndLoanId(LoanSettingTypeEnum typeEnum, Long loanId);


    /**
     * 通过设置类型和开关类型查询
     */
    List<LoanSettingDomain> findByTypeAndStatus(LoanSettingTypeEnum typeEnum, Boolean isActive);
}
