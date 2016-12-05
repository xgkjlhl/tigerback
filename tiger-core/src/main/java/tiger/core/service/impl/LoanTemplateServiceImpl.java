/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.LoanTplDO;
import tiger.common.dal.dataobject.example.LoanTplExample;
import tiger.common.dal.persistence.LoanTplMapper;
import tiger.common.dal.query.LoanTemplateQuery;
import tiger.common.util.Paginator;
import tiger.core.base.PageResult;
import tiger.core.domain.LoanTemplateDomain;
import tiger.core.domain.convert.LoanTemplateConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.LoanTemplateService;

import java.util.List;

/**
 * 贷款模板处理服务实现
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 19, 2015 3:05:43 PM yiliang.gyl Exp $
 */
@Service
public class LoanTemplateServiceImpl implements LoanTemplateService {

    private final Logger logger = Logger.getLogger(LoanTemplateServiceImpl.class);

    @Autowired
    private LoanTplMapper loanTplMapper;

    /**
     * @see tiger.core.service.LoanTemplateService#insertLoanTemplate(tiger.core.domain.LoanTemplateDomain)
     */
    @Override
    public LoanTemplateDomain insertLoanTemplate(LoanTemplateDomain loanTemplateDomain) {
        if (loanTemplateDomain == null) {
            throw new TigerException(ErrorCodeEnum.BIZ_EXCEPTION.getCode(),
                    ErrorCodeEnum.BIZ_EXCEPTION.getDefaultMessage());
        }
        LoanTplDO loanTplDO = LoanTemplateConvert.convertDomainToDO(loanTemplateDomain);
        if (loanTplMapper.insert(loanTplDO) > 0) {
            return LoanTemplateConvert.convertDoToDomain(loanTplDO);
        }
        return null;
    }

    /**
     * @see tiger.core.service.LoanTemplateService#deleteLoanTemplate(long)
     */
    @Override
    public boolean deleteLoanTemplate(long id) {
        return loanTplMapper.deleteByPrimaryKey(id) > 0;
    }

    /**
     * @see tiger.core.service.LoanTemplateService#getLoanTemplateById(long)
     */
    @Override
    public LoanTemplateDomain getLoanTemplateById(long id) {
        LoanTplDO loanTplDO = loanTplMapper.selectByPrimaryKey(id);
        if (loanTplDO != null) {
            return LoanTemplateConvert.convertDoToDomain(loanTplDO);
        } else {
            return null;
        }

    }

    /**
     * @see tiger.core.service.LoanTemplateService#updateLoanTemplate(tiger.core.domain.LoanTemplateDomain)
     */
    @Override
    public boolean updateLoanTemplate(LoanTemplateDomain loanTemplateDomain) {
        LoanTplDO loanTplDO = LoanTemplateConvert.convertDomainToDO(loanTemplateDomain);
        return loanTplMapper.updateByPrimaryKey(loanTplDO) > 0;
    }

    /**
     * @see tiger.core.service.LoanTemplateService#listTemplates(LoanTemplateQuery)
     */
    @Override
    public PageResult<List<LoanTemplateDomain>> listTemplates(LoanTemplateQuery query) {
        PageResult<List<LoanTemplateDomain>> results = new PageResult<>();

        LoanTplExample example = generateLoanTplExample(query);

        int totalItems = loanTplMapper.countByExample(example);

        // 分页器构建
        Paginator paginator = new Paginator();
        paginator.setItems(totalItems);
        paginator.setPage(query.getPageNum()); // 目前选择的页码
        paginator.setItemsPerPage(query.getPageSize());

        List<LoanTplDO> dos = loanTplMapper.selectByExampleAndOffset(example, paginator.getOffset(), paginator.getItemsPerPage());
        results.setData(LoanTemplateConvert.convertDOsToDomains(dos));
        results.setPaginator(paginator);
        return results;
    }

    /**
     * @see tiger.core.service.LoanTemplateService#all(LoanTemplateQuery)
     */
    @Override
    public List<LoanTemplateDomain> all(LoanTemplateQuery query) {
        LoanTplExample example = generateLoanTplExample(query);

        return LoanTemplateConvert.convertDOsToDomains(loanTplMapper.selectByExample(example));
    }

    /**
     * @see tiger.core.service.LoanTemplateService#countLoanTemplate(LoanTemplateQuery)
     */
    @Override
    public int countLoanTemplate(LoanTemplateQuery query) {
        if (query == null) {
            return 0;
        }

        LoanTplExample example = generateLoanTplExample(query);

        return loanTplMapper.countByExample(example);
    }

    /**
     * 根据query生成laonExample
     * @param query
     * @return
     */
    private LoanTplExample generateLoanTplExample(LoanTemplateQuery query) {
        LoanTplExample example = new LoanTplExample();
        example.createCriteria().
                andWorkspaceIdEqualTo(query.getWorkspaceId());
        example.setOrderByClause("update_time desc");

        return example;
    }

}
