package com.steve.config;

/**
 * @author stevexu
 * @Since 10/2/17
 */

import com.mysema.query.jpa.impl.JPAQueryFactory;
import com.steve.repository.MyJpaRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Properties;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "buyboxEntityManagerFactory",
                       transactionManagerRef = "buyboxTransactionManager",
                       repositoryFactoryBeanClass = MyJpaRepositoryFactoryBean.class)
public class JPAConfig {

    @Autowired
    private Environment environment;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "buyboxEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(primaryDataSource());

        // Classpath scanning of @Component, @Service, etc annotated class
        entityManagerFactory.setPackagesToScan("com.steve");

        // Vendor adapter
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        // Hibernate properties
        Properties additionalProperties = new Properties();
        /*additionalProperties.put(
                "hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        additionalProperties.put(
                "hibernate.show_sql",
                env.getProperty("hibernate.show_sql"));
        additionalProperties.put(
                "hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));*/
        entityManagerFactory.setJpaProperties(additionalProperties);

        return entityManagerFactory;

    }

    @Bean(name = "buyboxTransactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager =
                new JpaTransactionManager();
        LocalContainerEntityManagerFactoryBean factoryBean = entityManagerFactory();
        transactionManager.setEntityManagerFactory(
                factoryBean.getObject());
        return transactionManager;
    }

    @Bean
    JPAQueryFactory jpaQueryFactory(@Qualifier(value = "buyboxEntityManagerFactory")
                                            Provider<EntityManager> entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}

