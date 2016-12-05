/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.service;

import java.util.List;
import tiger.core.domain.TagDomain;

/**
 * @author alfred_yuan
 * @version v 0.1 11:16 alfred_yuan Exp $
 */
public interface TagService {

    TagDomain read(Long id);

    List<TagDomain> getTagsByAccountId(Long accountId);

}
