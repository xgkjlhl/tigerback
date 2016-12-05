/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.market;

import tiger.common.util.JsonConverterUtil;
import tiger.common.util.JsonUtil;

/**
 * 贷款项目联系人
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 1:49 PM yiliang.gyl Exp $
 */
public class ContactInfoDomain {

    public ContactInfoDomain(){
    }

    public ContactInfoDomain(String name, String mobilePhone){
        this.name = name;
        this.mobilePhone = mobilePhone;
    }

    private String name;

    private String mobilePhone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String serilized() {
        return JsonConverterUtil.serialize(this);
    }

    public static ContactInfoDomain deserilized(String string) {
        return JsonUtil.fromJson(string, ContactInfoDomain.class);
    }
}
