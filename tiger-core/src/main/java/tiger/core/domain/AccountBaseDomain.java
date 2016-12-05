package tiger.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.HashMap;

/**
 * Created by Kris Chan on 3:26 PM 3/27/16 .
 * All right reserved.
 */
public class AccountBaseDomain extends BaseDomain {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 3868246400642180615L;

    /**
     * The user name.
     */
    private String userName;

    /**
     * 头像
     */
    @CopyIgnore
    private AttachDomain icon;

    /**
     * 头像id
     */
    @JsonIgnore
    private Long iconId;

    /**
     * 用户额外参数
     */
    @CopyIgnore
    private HashMap<String, String> extParams;


    public HashMap<String, String> getExtParams() {
        return extParams;
    }

    public void setExtParams(HashMap<String, String> extParams) {
        this.extParams = extParams;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AttachDomain getIcon() {
        return icon;
    }

    public void setIcon(AttachDomain icon) {
        this.icon = icon;
    }

    public Long getIconId() {
        return iconId;
    }

    public void setIconId(Long iconId) {
        this.iconId = iconId;
    }
}
