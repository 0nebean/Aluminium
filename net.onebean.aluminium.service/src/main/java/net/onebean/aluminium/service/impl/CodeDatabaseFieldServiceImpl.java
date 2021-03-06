package net.onebean.aluminium.service.impl;

import net.onebean.aluminium.common.error.ErrorCodesEnum;
import net.onebean.aluminium.dao.CodeDatabaseFieldDao;
import net.onebean.aluminium.model.CodeDatabaseField;
import net.onebean.aluminium.service.CodeDatabaseFieldService;
import net.onebean.core.base.BaseBiz;
import net.onebean.core.error.BusinessException;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeDatabaseFieldServiceImpl extends BaseBiz<CodeDatabaseField, CodeDatabaseFieldDao> implements CodeDatabaseFieldService {

    @Override
    public List<CodeDatabaseField> findAllTableFieldbyTableName(String tablename) {
        List<CodeDatabaseField> list = baseDao.findAllTableFieldbyTableName(PropUtil.getInstance().getConfig("spring.datasource.databaseName",PropUtil.PUBLIC_CONF_JDBC),tablename);
        list = this.coverField(list);
        return list;
    }


    private List<CodeDatabaseField> coverField(List<CodeDatabaseField> childList){

        List<CodeDatabaseField> res = new ArrayList<>();
        for (CodeDatabaseField codeDatabaseField : childList) {
            CodeDatabaseField temp = new CodeDatabaseField();
            try {
                BeanUtils.copyProperties(temp,codeDatabaseField);
            } catch (Exception e) {
                throw new BusinessException(ErrorCodesEnum.REF_ERROR.code(),ErrorCodesEnum.REF_ERROR.msg());
            }
            String field = codeDatabaseField.getColumnName().toLowerCase();
            if(field.equals("id")) continue;
            String type = codeDatabaseField.getDatabaseType().toLowerCase();
            String classes = "String";
            if(type.contains("int")){
                classes = "Integer";
            }
            if(type.contains("bigint")){
                classes = "Long";
            }
            if(type.contains("numeric") || type.contains("double")){
                classes = "Double";
            }
            if(type.contains("decimal")){
                classes = "BigDecimal";
            }
            if(type.contains("float")){
                classes = "Float";
            }
            if(type.contains("time") || type.contains("date")){
                classes = "Timestamp";
            }
            String method_name = StringUtils.replaceUnderLineAndUpperCase(field);
            field = method_name.substring(0, 1).toLowerCase() + method_name.substring(1);
            temp.setDatabaseType(classes);
            temp.setColumnName(field);
            temp.setMethodName(method_name);
            temp.setJdbcType(StringUtils.getJdbcType(type));
            res.add(temp);
        }

        return res;
    }

}