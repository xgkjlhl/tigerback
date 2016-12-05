/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.example.LoanPawnParamExample;
import tiger.common.dal.persistence.LoanPawnParamMapper;
import tiger.core.domain.LoanPawnParamDomain;
import tiger.core.domain.convert.LoanPawnConvert;
import tiger.core.service.LoanPawnParamService;

import java.util.List;

/**
 * @author alfred
 * @version $ID: v 0.1 下午4:24 alfred Exp $
 */
@Service
public class LoanPawnParamServiceImpl implements LoanPawnParamService {
    private final Logger logger = Logger.getLogger(LoanPawnParamServiceImpl.class);

    @Autowired
    private LoanPawnParamMapper loanPawnParamMapper;

    /**
     * @see LoanPawnParamService#getPawnParamsByPawnId(long)
     */
    @Override
    public List<LoanPawnParamDomain> getPawnParamsByPawnId(long pawnId) {
        return LoanPawnConvert.convertToLoanPawnParamDomains(
                loanPawnParamMapper.getPawnParamsByPawnId(pawnId));
    }

    /**
     * @see LoanPawnParamService#createParamDomainList(List, long)
     */
    @Override
    public boolean createParamDomainList(List<LoanPawnParamDomain> paramDomains, long pawnId) {
        if (logger.isInfoEnabled()) {
            logger.info("插入贷款抵押物 [" + pawnId + "] 参数列表，参数列表为 [" + paramDomains + "]");
        }

        boolean rcCode = loanPawnParamMapper.saveListOfParams(
                LoanPawnConvert.convertToLoanPawnParamDOs(paramDomains), pawnId
        ) > 0;

        if (rcCode) {
            if (logger.isInfoEnabled()) {
                logger.info("插入贷款抵押物 [" + pawnId + "] 参数列表成功");
            }
        } else {
            logger.error("插入贷款抵押物 [" + pawnId + "] 参数列表失败");
        }

        return rcCode;
    }

    /**
     * @see LoanPawnParamService#deletePawnParamsByPawnId(long)
     */
    @Override
    public boolean deletePawnParamsByPawnId(long pawnId) {
        if (logger.isInfoEnabled()) {
            logger.info("删除贷款抵押物 [" + pawnId + "] 参数列表");
        }

        LoanPawnParamExample paramExample = new LoanPawnParamExample();
        paramExample.createCriteria().andLoanPawnIdEqualTo(pawnId);
        boolean rcCode = loanPawnParamMapper.deleteByExample(paramExample) > 0;

        if (rcCode) {
            if (logger.isInfoEnabled()) {
                logger.info("删除贷款抵押物 [" + pawnId + "] 参数列表成功");
            }
        } else {
            logger.error("删除贷款抵押物 [" + pawnId + "] 参数列表失败");
        }

        return rcCode;
    }
}
