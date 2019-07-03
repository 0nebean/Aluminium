package net.onebean.spring.config;

import net.onebean.core.extend.DynamicMapperSqlSessionFactoryBean;
import net.onebean.util.PropUtil;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis配置类
 * @author 0neBean
 */
@Configuration
public class MybatisConfig {
    /**
     * mybatis的扫描配置实例
     * @param sqlSessionFactory DynamicMapperSqlSessionFactoryBean
     * @return 返回一个mybatis扫描配置实例
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(@Qualifier("net.onebean.core.extend.DynamicMapperSqlSessionFactoryBean") DynamicMapperSqlSessionFactoryBean sqlSessionFactory){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //设置mybatis接口的扫描路径
        mapperScannerConfigurer.setBasePackage(PropUtil.getInstance().getConfig("org.mybatis.base.package",PropUtil.PUBLIC_CONF_JDBC));
        //设置mybatis接口的抽象接口
        mapperScannerConfigurer.setMarkerInterface(net.onebean.core.extend.SqlMapper.class);
        //指定sqlSessionFactory
        mapperScannerConfigurer.setSqlSessionFactoryBeanName(sqlSessionFactory.getClass().getName());
        return  mapperScannerConfigurer;
    }
}
