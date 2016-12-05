/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import tiger.common.dal.enums.BaseEnum;

/**
 * @author alfred_yuan
 * @version v 0.1 14:56 alfred_yuan Exp $
 */
public class EhcacheUtil {

    private static final Logger LOGGER = Logger.getLogger(EhcacheUtil.class);
    public static final String EH_CACHE = "[ ehCache ]";

    private static CacheManager sCacheManager;
    private static Cache sCache;

    private EhcacheUtil() {
    }

    /**
     * 使用配置文件中的cache
     */
    public static final String CACHE_NAME = "userCache";

    /**
     * 构建默认的 cache Manager 及 cache环境
     */
    static {
        sCacheManager = CacheManager.getInstance();
        sCacheManager.addCache(CACHE_NAME);
        sCache = sCacheManager.getCache(CACHE_NAME);
    }

    /**
     * 查看是否存在元素
     *
     * @param enumCode
     * @return
     */
    public static boolean contains(BaseEnum enumCode) {
        return sCache.isKeyInCache(enumCode);
    }

    /**
     * 存放缓存
     *
     * @param enumCode
     * @param object
     */
    public static void put(BaseEnum enumCode, Object object) {
        try {
            LOGGER.info("存放缓存 [" + enumCode + "] -> [" + object + "]");
            sCache.put(new Element(enumCode, object));
        } catch (Exception e) {
            LOGGER.error(EH_CACHE + "存放缓存失败");
        }
    }

    /**
     * 获取缓存
     *
     * @param enumCode
     * @return
     */
    public static Object get(BaseEnum enumCode) {
        Object cacheValue = null;
        try {
            cacheValue = sCache.get(enumCode).getObjectValue();
        } catch (Exception e) {
            LOGGER.error(EH_CACHE + "获取缓存 [" + enumCode + "] 失败");
        } finally {
            LOGGER.info("成功获取缓存 [" + enumCode + "] -> [" + cacheValue + "]");
            return cacheValue;
        }
    }

}
