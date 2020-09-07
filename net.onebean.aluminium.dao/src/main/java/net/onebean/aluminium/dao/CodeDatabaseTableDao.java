package net.onebean.aluminium.dao;
import net.onebean.core.base.BaseDao;
import net.onebean.aluminium.model.CodeDatabaseTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CodeDatabaseTableDao extends BaseDao<CodeDatabaseTable> {
    /**
     * 查询数据库所有表名
     * @param databaseName
     * @return
     */
    List<String> findDatabaseTableList(@Param("databaseName")String databaseName);

}