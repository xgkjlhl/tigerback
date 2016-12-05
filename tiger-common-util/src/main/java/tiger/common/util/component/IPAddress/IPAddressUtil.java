/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.common.util.component.IPAddress;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import tiger.common.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据IP地址获取详细的地域信息
 * @author alfred_yuan
 * @version v 0.1 23:16 alfred_yuan Exp $
 */
public class IPAddressUtil {

    public static Logger logger = Logger.getLogger(IPAddressUtil.class);

    private static final String TAO_BAO_REST_URL = "http://ip.taobao.com/service/getIpInfo.php";
    private static final String IP = "ip";

    private static final Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "text/html;charset=UTF-8");
    }
    /**
     * 访问淘宝IP接口,获取地区编码
     * @param ipAddress
     * @return
     */
    public static IPHttpResponse.Data IP2RegionCode(String ipAddress) {
        // 构造queryParams
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(IP, ipAddress);

        String jsonString;

        // 访问 淘宝 接口
        try {
            String url = HttpUtil.appendQueryParams(TAO_BAO_REST_URL, queryParams);
            jsonString = HttpUtil.get(url, headers);
            IPHttpResponse response =  new Gson().fromJson(jsonString, IPHttpResponse.class);
            // code的值的含义为，0：成功，1：失败
            if (response.getCode() == 0) {
                return null;
            }

            return response.getData();
        } catch (Exception e) {
            logger.error("访问接口失败, 参数 ipAddress: ["+ ipAddress +"], 异常为 ["+ e + "]");
            return null;
        }
    }
}
