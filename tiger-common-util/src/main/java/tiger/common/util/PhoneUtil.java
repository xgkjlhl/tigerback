/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.common.util;

/**
 * @author zhangbin
 * @version v0.1 2015/9/26 10:03
 */
public class PhoneUtil {

    private static final int PHONE_NUMBER_LENGTH = 11;

    /**
     * 判断是否是合法的手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isValidMobile(String mobile) {
        return (mobile.length() == PHONE_NUMBER_LENGTH) && StringUtil.isNumeric(mobile);
    }

}
