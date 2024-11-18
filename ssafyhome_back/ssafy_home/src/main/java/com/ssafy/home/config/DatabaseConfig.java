package com.ssafy.home.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties") // 프로퍼티 파일 로드
@MapperScan(basePackages = { "com.ssafy.home.*.model.mapper" }) // Mapper 인터페이스 스캔 경로
public class DatabaseConfig {

    final ApplicationContext applicationContext;

    public DatabaseConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    // HikariCP 설정
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    // MyBatis SqlSessionFactory 설정
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);

        // MyBatis 매퍼 XML 파일 경로 설정
        sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));

        // MyBatis 타입 앨리어스 설정
        sessionFactoryBean.setTypeAliasesPackage("com.ssafy.home.*.model");

        return sessionFactoryBean.getObject();
    }

    // MyBatis SqlSessionTemplate 설정
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
