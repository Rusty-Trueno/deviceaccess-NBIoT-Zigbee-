/*
package cn.edu.bupt.security.model;

public enum Authority {

    SYS_ADMIN("SYS_ADMIN"),
    TENANT_ADMIN("TENANT_ADMIN"),
    CUSTOMER_USER("CUSTOMER_USER");

    private String code;

    Authority(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Authority parse(String value) {
        Authority authority = null;
        if (value != null && value.length() != 0) {
            for (Authority current : Authority.values()) {
                if (current.name().equalsIgnoreCase(value)) {
                    authority = current;
                    break;
                }
            }
        }
        return authority;
    }
}
*/
