package com.steve.illuminator.core.reg;

/**
 * Created by xuh18 on 6/12/16.
 */
public interface AbstractRegistryCenter {

    void register();

    void init();

    void close();

    String get(String key);

    String getDirectly(String key);

    boolean isExisted(String key);

    void persist(String key, String value);

    void update(String key, String value);

    void persistEphemeral(String key, String value);

    void remove(String key);

    JobConfiguration getJobConfig();

    Object getRawClient();

    Object getRawCache(String cachePath);
}
