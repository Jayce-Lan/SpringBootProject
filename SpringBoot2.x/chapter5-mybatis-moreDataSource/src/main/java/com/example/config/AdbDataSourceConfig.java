package com.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.dao.adb", sqlSessionTemplateRef = "adbSqlSessionTemplate")
public class AdbDataSourceConfig {
    @Value("${spring.datasource.adb.url}")
    private String url;
    @Value("${spring.datasource.adb.username}")
    private String username;
    @Value("${spring.datasource.adb.password}")
    private String password;
    @Value("${spring.datasource.adb.driver-class-name}")
    private String driverClassName;
    private final static String MAPPER_LOCATION = "classpath:mybatis/mapper/*ADBMapper.xml";

    /**
     * 创建数据源
     * @return 数据源
     */
    @Bean(name = "adbDataSource")
    public DataSource getADBDataSource() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
        return dataSource;
    }

    /**
     * 创建 SqlSessionFactory
     * @param dataSource 数据源
     * @return SqlSessionFactory
     * @throws Exception
     */
    @Bean(name = "adbSqlSessionFactory")
    public SqlSessionFactory adbSqlSessionFactory(@Qualifier("adbDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 设置mapper配置文件
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return bean.getObject();
    }

    /**
     * 事务管理器
     * @param dataSource
     * @return
     */
    @Bean(name = "adbTransactionManager")
    public DataSourceTransactionManager adbTransactionManager(@Qualifier("adbDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "adbSqlSessionTemplate")
    public SqlSessionTemplate adbSqlSessionTemplate(@Qualifier("adbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
