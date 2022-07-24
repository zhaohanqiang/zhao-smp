package jp.co.seamaple.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment enviroment;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(enviroment.getProperty("spring.datasource.driver-class-name"));

        dataSource.setUrl(enviroment.getProperty("spring.datasource.url"));

        dataSource.setUsername(enviroment.getProperty("spring.datasource.username"));

        dataSource.setPassword(enviroment.getProperty("spring.datasource.password"));

        return dataSource;
    }
}
