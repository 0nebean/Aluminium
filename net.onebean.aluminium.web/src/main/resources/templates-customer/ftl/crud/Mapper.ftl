<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--author ${author}-->
<!--description ${description} mapper-->
<!--date ${create_time}-->
<mapper namespace="${dao_package_name}.${model_name}Dao">

    <#if field_arr?exists>
        <sql id="basicFiled">
                t.id,
            <#list field_arr as item>
                t.${item.databaseColumnName} as ${item.columnName}<#sep>, </#sep>
            </#list>
        </sql>

        <resultMap id="basicResultMap" type="${model_package_name}.${model_name}">
            <id column="id" jdbcType="INTEGER" property="id"/>
            <#list field_arr as item>
                <result column="${item.databaseColumnName}" jdbcType="${item.jdbcType}" property="${item.columnName}"/>
            </#list>
        </resultMap>
    </#if>

</mapper>