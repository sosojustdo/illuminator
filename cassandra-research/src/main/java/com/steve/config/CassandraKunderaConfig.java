package com.steve.config;

import com.impetus.kundera.KunderaPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * @author stevexu
 * @Since 9/29/17
 */
@Configuration
public class CassandraKunderaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("buybox_cassandra");
        factoryBean.setPersistenceProviderClass(KunderaPersistence.class);
        return factoryBean;
    }

}
