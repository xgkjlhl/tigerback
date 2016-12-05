package tiger.core.service;

import tiger.core.domain.LoanPawnParamDomain;

import java.util.List;

/**
 * @author alfred
 * @version $ID: v 0.1 下午4:22 alfred Exp $
 */
public interface LoanPawnParamService {
    /**
     * @param pawnId
     * @return
     */
    List<LoanPawnParamDomain> getPawnParamsByPawnId(long pawnId);

    /**
     * 创建抵押物参数列表
     *
     * @param paramDomains
     * @param pawnId
     * @return
     */
    boolean createParamDomainList(List<LoanPawnParamDomain> paramDomains, long pawnId);

    /**
     * 删除某一个抵押物所有参数字段
     *
     * @param pawnId
     * @return
     */
    boolean deletePawnParamsByPawnId(long pawnId);
}
