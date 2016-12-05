/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.redis.RedisComponent;
import tiger.core.AbstractCoreTests;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 7:27 PM yiliang.gyl Exp $
 */
public class RedisTest extends AbstractCoreTests {

    @Autowired
    RedisComponent redisComponent;

    @Test
    public void testRedisInsert() {
        redisComponent.saveObject("aa", "test1test2");
        String value = redisComponent.getObject("aa");
        logger.info("从 Redis 中获取数据 [" + value + "]");
    }
}
