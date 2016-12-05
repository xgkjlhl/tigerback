/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.util;

import java.util.List;

/**
 * 处理前端传递ID解析问题的工具类.
 *
 * @param <T> the generic type
 * @author yiliang.gyl
 * @version v 0.1 Sep 23, 2015 9:06:35 AM yiliang.gyl Exp $
 */
public class IDUtils {

    /**
     * 从一组 ID 里获取第一一个 ID
     *
     * @param list the list
     * @return the unique id from list
     */
    public static String getFirstIdFromList(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

}
