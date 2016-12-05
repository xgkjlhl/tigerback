/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.persistence.TagMapper;
import tiger.core.domain.TagDomain;
import tiger.core.domain.convert.TagConverter;
import tiger.core.service.TagService;

import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 11:17 alfred_yuan Exp $
 */
@Service
public class TagServiceImpl implements TagService {

    private static final Logger logger = Logger.getLogger(TagServiceImpl.class);

    @Autowired
    private TagMapper tagMapper;

    @Override
    public TagDomain read(Long id) {
        if (logger.isInfoEnabled()) {
            logger.info("开始读取id为 [" + id + "] 的标签");
        }

        return TagConverter.convert2Domain(tagMapper.getById(id));
    }

    @Override
    public List<TagDomain> getTagsByAccountId(Long accountId) {
        return TagConverter.convert2Domain(tagMapper.getTagsByAccountId(accountId));
    }
}
