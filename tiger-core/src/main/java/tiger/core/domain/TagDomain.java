package tiger.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.core.base.BaseDomain;

public class TagDomain extends BaseDomain {

    /**
     *
     */
    private static final long serialVersionUID = -2003675956925381258L;

    private String name;

    private Long accountId;

    private String color;

    @JsonIgnore
    private Long customerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Getter for property 'customerId'.
     *
     * @return Value for property 'customerId'.
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Setter for property 'customerId'.
     *
     * @param customerId Value to set for property 'customerId'.
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
