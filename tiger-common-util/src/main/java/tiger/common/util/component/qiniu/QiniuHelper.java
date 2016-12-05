/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.common.util.component.qiniu;

import com.qiniu.util.Auth;

import java.util.Map;

/**
 * 帮助类,用来构建一些依赖于QiniuSDK, 但与业务无关的
 *
 * @author alfred_yuan
 * @version v 0.1 12:34 alfred_yuan Exp $
 */
public class QiniuHelper {
    private QiniuHelper() {
    }

    /**
     * 根据config, 构造出Qiniu Auth
     *
     * @param config
     * @return
     */
    public static Auth getAuth(Map<String, String> config) {
        return Auth.create(config.get("ACCESS_KEY"), config.get("SECRET_KEY"));
    }
}
