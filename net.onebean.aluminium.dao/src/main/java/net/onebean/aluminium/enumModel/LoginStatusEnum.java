package net.onebean.aluminium.enumModel;

/**
 * CodeDatabaseTable枚举类
 * @author Heisenberg
 */
public enum LoginStatusEnum {
    SUCCESS("0"),//成功
    PASSWORD_ERR("1"),//密码错误
    ;

    private String value;

    LoginStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
