package com.steve.illuminator.core;

import com.steve.illuminator.core.reg.ZookeeperRegistryCenter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xuh18 on 6/12/16.
 */
public class Main {

     public static void main(String args[]){
         ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
         ZookeeperRegistryCenter registryCenter = (ZookeeperRegistryCenter) applicationContext.getBean("regCenter");
         registryCenter.register();
     }

}
