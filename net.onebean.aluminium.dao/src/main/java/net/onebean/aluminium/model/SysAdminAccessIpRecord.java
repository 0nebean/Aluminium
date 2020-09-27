package net.onebean.aluminium.model;

import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.TableName;
import net.onebean.core.model.BaseModel;
import net.onebean.core.model.InterfaceBaseDeletedModel;

import java.sql.Timestamp;

/**
* @author 0neBean
* @description 后台访问记录流水 model
* @date 2020-07-12 01:38:43
*/
@TableName("sys_admin_access_ip_record")
public class SysAdminAccessIpRecord extends BaseModel implements InterfaceBaseDeletedModel {

        public SysAdminAccessIpRecord() {
        }

        public SysAdminAccessIpRecord(String loginStatus, String loginIp, String userName) {
            this.loginStatus = loginStatus;
            this.loginIp = loginIp;
            this.userName = userName;
        }

        /**
        * 登录状态0:成功,1:账号密码错误
        */
        private String loginStatus;
        @FiledName("loginStatus")
        public String getLoginStatus() {
            return loginStatus;
        }
        public void setLoginStatus(String loginStatus) {
            this.loginStatus = loginStatus;
        }


        /**
        * IP地址
        */
        private String loginIp;
        @FiledName("loginIp")
        public String getLoginIp(){
            return this.loginIp;
        }
        public void setLoginIp(String loginIp){
            this.loginIp = loginIp;
        }


        /**
        * 登录名
        */
        private String userName;
        @FiledName("userName")
        public String getUserName(){
            return this.userName;
        }
        public void setUserName(String userName){
            this.userName = userName;
        }


        /**
        * 创建时间
        */
        private Timestamp createTime;
        @FiledName("createTime")
        public Timestamp getCreateTime(){
            return this.createTime;
        }
        public void setCreateTime(Timestamp createTime){
            this.createTime = createTime;
        }


        /**
        * 更新时间
        */
        private Timestamp updateTime;
        @FiledName("updateTime")
        public Timestamp getUpdateTime(){
            return this.updateTime;
        }
        public void setUpdateTime(Timestamp updateTime){
            this.updateTime = updateTime;
        }


        /**
        * 操作人ID
        */
        private Integer operatorId;
        @FiledName("operatorId")
        public Integer getOperatorId(){
            return this.operatorId;
        }
        public void setOperatorId(Integer operatorId){
            this.operatorId = operatorId;
        }


        /**
        * 操作人姓名
        */
        private String operatorName;
        @FiledName("operatorName")
        public String getOperatorName(){
            return this.operatorName;
        }
        public void setOperatorName(String operatorName){
            this.operatorName = operatorName;
        }


        /**
        * 逻辑删除,0否1是
        */
        private String isDeleted;
        @FiledName("isDeleted")
        public String getIsDeleted(){
            return this.isDeleted;
        }
        public void setIsDeleted(String isDeleted){
            this.isDeleted = isDeleted;
        }



}