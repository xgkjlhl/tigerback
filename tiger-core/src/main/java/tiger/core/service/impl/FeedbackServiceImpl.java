/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.FeedbackDO;
import tiger.common.dal.persistence.FeedbackMapper;
import tiger.common.dal.query.FeedbackQuery;
import tiger.common.util.Paginator;
import tiger.core.base.PageResult;
import tiger.core.domain.FeedbackDomain;
import tiger.core.domain.convert.FeedbackConvert;
import tiger.core.domain.listDomain.FeedbackListDomain;
import tiger.core.service.FeedbackService;

import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 16:46 alfred_yuan Exp $
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    Logger logger = Logger.getLogger(FeedbackServiceImpl.class);

    @Autowired
    private FeedbackMapper feedbackMapper;

    /**
     * @see FeedbackService#create(FeedbackDomain)
     */
    @Override
    public FeedbackDomain create(FeedbackDomain feedbackDomain) {
        if (feedbackDomain == null || feedbackDomain.getAccountId() == null) {
            return null;
        }

        FeedbackDO feedbackDO = FeedbackConvert.convert2DO(feedbackDomain);

        if (feedbackMapper.insertSelective(feedbackDO) > 0) {
            return FeedbackConvert.convert2Domain(feedbackDO);
        }

        return null;
    }

    /**
     * @see FeedbackService#query(FeedbackQuery)
     */
    @Override
    public PageResult<List<FeedbackListDomain>> query(FeedbackQuery query) {
        //构建分页查询器
        Paginator paginator = new Paginator();
        int count = feedbackMapper.countFeedback(query);
        paginator.setItems(count);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum());

        PageResult<List<FeedbackListDomain>> result = new PageResult<>();
        result.setData(FeedbackConvert.convert2ListDomains(feedbackMapper.query(query, paginator.getOffset(), paginator.getLength())));
        result.setPaginator(paginator);

        return result;
    }

    /**
     * @see FeedbackService#getFeedbackById(Long)
     */
    public FeedbackDomain getFeedbackById(Long id) {
        if (id == null) {
            return null;
        }

        return FeedbackConvert.convert2Domain(feedbackMapper.selectByPrimaryKey(id));
    }
}
