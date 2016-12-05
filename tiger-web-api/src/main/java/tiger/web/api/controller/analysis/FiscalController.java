/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiger.biz.analysis.support.FiscalManager;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanRecordItemStatusEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.common.dal.query.LoanFiscalQuery;
import tiger.core.base.BaseResult;
import tiger.core.domain.LoanFiscalListDomain;
import tiger.core.domain.LoanFiscalSummaryListDomain;
import tiger.core.domain.StatisticsDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.web.api.controller.base.BaseController;

import javax.validation.Valid;

/**
 * 数据分析类-财务接口
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 13, 2015 7:13:16 AM yiliang.gyl Exp $
 */
@RestController
@RequestMapping("analysis")
@ResponseBody
public class FiscalController extends BaseController {

    @Autowired
    private FiscalManager fiscalManager;

    @Autowired
    private WorkspaceManager workspaceManager;

    /**
     * 获取财务类信息
     *
     * @param loanFiscalQuery
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/fiscals", method = RequestMethod.GET, params = "scope=detail")
    @Permission(permission = {PermissionEnum.VIEW_FISCALS_MEMBER,PermissionEnum.VIEW_FISCALS_ALL})
    public BaseResult<LoanFiscalListDomain> getFiscalListDetail(@Valid LoanFiscalQuery loanFiscalQuery,
                                                                BindingResult bindingResult) {
        setAndCheckWorkspaceQuery(loanFiscalQuery);
        return new BaseResult<>(fiscalManager.getLoanFiscalListDetail(loanFiscalQuery));
    }

    /**
     * 获取财务类信息
     *
     * @param loanFiscalQuery
     * @return
     */
    @RequestMapping(value = "/fiscals", method = RequestMethod.GET, params = "scope=summary")
    @Permission(permission = {PermissionEnum.VIEW_FISCALS_MEMBER,PermissionEnum.VIEW_FISCALS_ALL})
    public BaseResult<LoanFiscalSummaryListDomain> getFiscalList(@Valid LoanFiscalQuery loanFiscalQuery,
                                                                 BindingResult bindingResult) {
        setAndCheckWorkspaceQuery(loanFiscalQuery);
        return new BaseResult<>(fiscalManager.getLoanFiscalListSummary(loanFiscalQuery));
    }

    /**
     * 获取财务统计信息
     *
     * @param loanFiscalQuery
     * @return
     */
    @RequestMapping(value = "/fiscals", method = RequestMethod.GET, params = "scope=statistics")
    @Permission(permission = {PermissionEnum.VIEW_FISCALS_MEMBER,PermissionEnum.VIEW_FISCALS_ALL})
    public BaseResult<StatisticsDomain> getFiscalLStatistics(@Valid LoanFiscalQuery loanFiscalQuery,
                                                             BindingResult bindingResult) {
        setAndCheckWorkspaceQuery(loanFiscalQuery);
        return new BaseResult<>(fiscalManager.getLoanFiscalListStatistics(loanFiscalQuery));
    }


    //~ private methods
    private void checkLoanFiscalQuery(LoanFiscalQuery query) {
        if (query == null) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER);
        }
        // 检查业务类型
        // query.businessType为必需
        if (query.getBusinessType() == null || BusinessTypeEnum.getEnumByCode(query.getBusinessType()) == null) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER);
        }
        // 检查支出类型
        if (!CollectionUtils.isEmpty(query.getOutComeTypes())) {
            for (String outComeType : query.getOutComeTypes()) {
                if (LoanPayItemEnum.getEnumByCode(outComeType) == null) {
                    throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE);
                }
            }
        }
        // 检查收入类型
        if (!CollectionUtils.isEmpty(query.getInComeTypes())) {
            for (String incomeType : query.getInComeTypes()) {
                if (LoanPayItemEnum.getEnumByCode(incomeType) == null) {
                    throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE);
                }
            }
        }
        // 检查兑付情况
        if (query.getLoanRecordStatus() == null || LoanRecordItemStatusEnum.getEnumByCode(query.getLoanRecordStatus()) == null) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE);
        }
    }

    /**
     * 检查并设置
     * @param loanFiscalQuery
     */
    private void setAndCheckWorkspaceQuery(@Valid LoanFiscalQuery loanFiscalQuery) {
        Long workspaceId = currentWorkspaceId();

        // 检查ownerId是否为当前工作空间的所有者
        if (loanFiscalQuery.getOwnerId() != null) {
            workspaceManager.checkIsWorkspaceMember(loanFiscalQuery.getOwnerId(), workspaceId);
        }

        loanFiscalQuery.setWorkSpaceId(workspaceId);
        if (!isPermitted(PermissionEnum.VIEW_FISCALS_ALL)) {
            Long currentAccountId = currentAccount().getId();
            if (loanFiscalQuery.getOwnerId() != null && !loanFiscalQuery.getOwnerId().equals(currentAccountId)) {
               throw  new TigerException(ErrorCodeEnum.UNAUTHORIZED, "没有权限访问团队其他成员的财务数据");
            }

            // 设置当前用户id
            loanFiscalQuery.setAccountId(currentAccountId);
        }

        checkLoanFiscalQuery(loanFiscalQuery);
    }

}
