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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.dao.drds", sqlSessionTemplateRef = "drdsSessionTemplate")
public class DrdsDataSourceConfig {
    @Value("${spring.datasource.drds.url}")
    private String url;
    @Value("${spring.datasource.drds.username}")
    private String username;
    @Value("${spring.datasource.drds.password}")
    private String password;
    @Value("${spring.datasource.drds.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.drds.mapper-locations}")
    private String mapperLocations;

    /**
     * 数据源
     * @return 数据源
     */
    @Bean(name = "drdsDataSource")
    @Primary
    public DataSource getDrdsDataSource() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
        return dataSource;
    }

    @Bean(name = "drdsSqlSessionFactory")
    @Primary
    public SqlSessionFactory drdsSqlSessionFactory(@Qualifier("drdsDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean(name = "drdsTransactionManager")
    @Primary
    public DataSourceTransactionManager drdsTransactionManager(@Qualifier("drdsDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "drdsSessionTemplate")
    @Primary
    public SqlSessionTemplate drdsSessionTemplate(@Qualifier("drdsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
