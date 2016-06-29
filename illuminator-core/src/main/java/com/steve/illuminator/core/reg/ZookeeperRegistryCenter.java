package com.steve.illuminator.core.reg;

import com.steve.illuminator.core.Constants;
import com.steve.illuminator.core.excpetion.ExceptionHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuh18 on 6/12/16.
 */

public class ZookeeperRegistryCenter implements AbstractRegistryCenter{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistryCenter.class);

    private JobConfiguration zkConfig;

    private final Map<String, TreeCache> caches = new ConcurrentHashMap<String, TreeCache>();

    private CuratorFramework client;

    public ZookeeperRegistryCenter(final JobConfiguration zkConfig) {
        this.zkConfig = zkConfig;
    }

    public void register() {
        String jobNodePath = Constants.hosts+"/"+zkConfig.getNodeId();
        TreeCache cache = new TreeCache(client, jobNodePath);
        try {
            cache.start();
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
        }
        persistEphemeral(jobNodePath, "active");
        logger.info(zkConfig.getNodeId()+" has finished registered");
        /*caches.put(jobNodePath + "/", cache);*/
    }

    public void init() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConfig.getServerLists())
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMilliseconds(), zkConfig.getMaxRetries(), zkConfig.getMaxSleepTimeMilliseconds()))
                .namespace(zkConfig.getNamespace());
        client = builder.build();
        client.start();
        try {
            client.blockUntilConnected();
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void close() {
        for (Map.Entry<String, TreeCache> each : caches.entrySet()) {
            each.getValue().close();
        }
        waitForCacheClose();
        CloseableUtils.closeQuietly(client);
    }

    public String get(final String key) {
        TreeCache cache = findTreeCache(key);
        if (null == cache) {
            return getDirectly(key);
        }
        ChildData resultInCache = cache.getCurrentData(key);
        if (null != resultInCache) {
            return null == resultInCache.getData() ? null : new String(resultInCache.getData(), Charset.forName("UTF-8"));
        }
        return getDirectly(key);
    }

    private TreeCache findTreeCache(final String key) {
        for (Map.Entry<String, TreeCache> entry : caches.entrySet()) {
            if (key.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String getDirectly(final String key) {
        try {
            return new String(client.getData().forPath(key), Charset.forName("UTF-8"));
        } catch (final Exception ex) {
            return null;
        }
    }

    public List<String> getChildrenKeys(final String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            result.sort((final String o1, final String o2)->o1.compareTo(o2));
            return result;
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
            return Collections.emptyList();
        }
    }

    public boolean isExisted(final String key) {
        try {
            return null != client.checkExists().forPath(key);
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
            return false;
        }
    }

    public void persist(final String key, final String value) {
        try {
            if (!isExisted(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes());
            } else {
                update(key, value);
            }
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void update(final String key, final String value) {
        try {
            client.inTransaction().check().forPath(key).and().setData().forPath(key, value.getBytes(Charset.forName("UTF-8"))).and().commit();
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void persistEphemeral(final String key, final String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charset.forName("UTF-8")));
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void persistEphemeralSequential(final String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void remove(final String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public JobConfiguration getJobConfig() {
        return zkConfig;
    }

    public Object getRawClient() {
        return client;
    }

    private void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
