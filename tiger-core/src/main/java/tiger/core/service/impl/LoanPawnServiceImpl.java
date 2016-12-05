/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.LoanPawnDO;
import tiger.common.dal.persistence.LoanPawnMapper;
import tiger.common.dal.persistence.LoanPawnParamMapper;
import tiger.core.domain.LoanPawnDomain;
import tiger.core.domain.convert.LoanPawnConvert;
import tiger.core.service.LoanPawnService;

/**
 * 贷款抵押物管理实现类
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 4:26:28 PM yiliang.gyl Exp $
 */
@Service
public class LoanPawnServiceImpl implements LoanPawnService {
    @Autowired
    private LoanPawnMapper loanPawnMapper;

    @Autowired
    private LoanPawnParamMapper loanPawnParamMapper;

    /**
     * @see LoanPawnService#read(long)
     */
    @Override
    public LoanPawnDomain read(long loanPawnId) {
        LoanPawnDomain pawnDomain = LoanPawnConvert
                .convertToLoanPawnDomain(loanPawnMapper.selectByPrimaryKey(loanPawnId));
        if (pawnDomain == null) {
            return null;
        }
        // 添加参数
        pawnDomain.setLoanPawnParamDomains(LoanPawnConvert.convertToLoanPawnParamDomains(
                loanPawnParamMapper.getPawnParamsByPawnId(pawnDomain.getId())));
        return pawnDomain;
    }

    @Override
    public LoanPawnDomain getLoanPawnByLoanId(long loanId) {
        LoanPawnDomain pawnDomain = LoanPawnConvert
                .convertToLoanPawnDomain(loanPawnMapper.getLoanPawnDoByLoanId(loanId));
        return pawnDomain;
    }

    /**
     * @see tiger.core.service.LoanPawnService#create(tiger.core.domain.LoanPawnDomain)
     */
    @Override
    public LoanPawnDomain create(LoanPawnDomain loanPawnDomain) {
        LoanPawnDO loanPawnDO = LoanPawnConvert.convertToLoanPawnDO(loanPawnDomain);

        int rcCode = loanPawnMapper.insert(loanPawnDO);
        if (rcCode <= 0) {
            return null;
        }

        return read(loanPawnDO.getId());
    }

    /**
     * @see tiger.core.service.LoanPawnService#isExist(long)
     */
    @Override
    public boolean isExist(long pawnId) {
        LoanPawnDO loanPawnDO = loanPawnMapper.selectByPrimaryKey(pawnId);
        if (loanPawnDO == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isOwner(long pawnId, long accountId) {
        LoanPawnDO loanPawnDO = loanPawnMapper.selectByPrimaryKey(pawnId);
        if (null == loanPawnDO) {
            return false;
        }
        return loanPawnDO.getAccountId().equals(accountId);
    }

    /**
     * @see tiger.core.service.LoanPawnService#updateByIdSelective(tiger.core.domain.LoanPawnDomain)
     */
    @Override
    public boolean updateByIdSelective(LoanPawnDomain loanPawnDomain) {
        LoanPawnDO loanPawnDO = LoanPawnConvert.convertToLoanPawnDO(loanPawnDomain);
        if (loanPawnDO != null) {
            int rc = loanPawnMapper.updateByPrimaryKeySelective(loanPawnDO);
            return rc > 0;
        }
        return false;
    }

}
