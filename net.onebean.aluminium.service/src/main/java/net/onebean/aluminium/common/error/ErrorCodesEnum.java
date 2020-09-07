package net.onebean.aluminium.common.error;


/**
 * 错误码定义
 * @author Heisenberg
 * 1-4位 8886 代表 
 * 异常类型：5～6位，标识数据库、接口、缓存、权限等
 *	01-参数异常，参数有效性校验错误，在接口的response 的msg中请将校验有问题的参数返回给调用方；
 *	02-数据库异常，操作数据库异常，log的message中要有sql、参数信息，sql如果能在堆栈中体现，可以不放在message中；
 *	03-缓存异常，如对redis、JVM缓存操作失败；
 *	04-权限异常，功能权限、数据权限类异常；
 *	05-接口异常，接口调用异常，log 的message中要包含接口地址、接口参数信息，如果能在堆栈中体现，可以不放在message中；
 *	06-业务异常, 业务执行异常， 比如”用户名已存在”，此种类型的异常通常前端要关注；
 *	07-其它运行异常，如JAVA程序运行时异常；
 *	08-消息队列异常，如对rabbitMq 的操作失败；
 *	...
 *	99-除以上外的异常
 *	异常序列号：6～10位，每个模块自定义，要求模块范围内唯一性
 */
public enum ErrorCodesEnum {
    SUSSESS("0",""),
    //01
    REQUEST_PARAM_ERROR("8886010001","请求参数校验异常"),
    //02
    NONE_QUERY_DATA("8886020001","没有对应的数据"),
    GET_DATE_ERR("8886020002","数据库查询数据异常"),
    REPEAT_MOBILE_ERR("8886020003","该手机号已注册"),
    REPEAT_WALLET_ERR("8886020004","该钱包地址已被使用"),
    //03
    GET_DATA_FROM_CACHE_ERR("8886030001","从缓存获取数据失败"),
    //06
    ASSOCIATED_DATA_CANNOT_BE_DELETED("8886060001","关联数据不能删除"),
    NO_FREE_WALLET("8886060002","地址池异常,注册失败,请稍后重试"),
    NO_FREE_MONEY_TO_USE("8886060003","余额不足,请充值"),
    //07
    JSON_CAST_ERROR("8886070001","序列化JSON异常"),
    REF_ERROR("8886070002","反射对象属性异常"),
    GENERATE_FILE_FAILURE("8886070003","生成文件失败"),
    INVALID_INVITATION_CODE("8886070004","无效的邀请码"),
    ORDER_ALREADY_OFF("8886070005","订单已解约,不能重复操作"),
    SETTLE_ORDER_ERR("8886070006","结算订单异常"),
    WITHDRAW_PRICE_ERR("8886070007","提现金额错误"),
    ACCOUNT_ERR("8886070008","账户信息异常,请提交工单联系客服"),
    NO_WITHDRAW_WALLET_ADDRESS("8886070009","请填完善款钱包地址,再申请提现"),
    WITHDRAW_ONE_DAY_ONCE_ERR("8886070010","24小时内只能提现一次"),
    EARNING_ORDER_STATUS_ERR("8886070011","收益合约状态已更改,请勿重复提交"),
    WITHDRAW_PRICE_MORE_THAN_BALANCE_ERR("8886070012","提现金额,不能超过账户余额"),
    WITHDRAW_ONLY_ONE_RUNNING_ERR("8886070013","您有一笔正在处理中的提现未完成,请耐心等待"),
    JOB_ORDER_PROCESSING("8886070014","您有一笔工单处理中,请耐心等待"),
    JOB_ORDER_OVER("8886070015","工单已处理完毕"),
    SAFE_PASSWORD_ERR("8886070016","资金密码错误"),
    ACTIVE_LIMIT("8886070017","合约金额不能小于"),
    UPGRADE_LIMIT_TIME_ERR("8886070018","静态收益合约创建未满"),
    LESS_THAN_MINI_MUN_WITHDRAW("8886070019","最小提现金额为:"),
    LESS_THAN_MINI_MUN_DEPOSIT("8886070020","最小充值金额为:"),
    DEPOSIT_ONLY_ONE_RUNNING_ERR("8886070021","您有一笔入金申请未完成,请尽确认付款"),
    ORDER_PAYED("8886070022","订单已支付,不能重复操作"),
    ORDER_CHECKED("8886070023","订单已确认,不能重复操作"),
    //99
    OTHER("8886999999","操作失败")
    ;

    private String code;

    private String msg;

    private ErrorCodesEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String code(){
        return this.code;
    }

    public String msg(){
        return this.msg;
    }
}
