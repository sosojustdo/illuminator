package com.steve.illuminator.core.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * Created by xuh18 on 6/27/16.
 */
public abstract class AbstractJobListener implements TreeCacheListener{

    @Override
    public final void childEvent(final CuratorFramework client, final TreeCacheEvent event) throws Exception {
        String path = null == event.getData() ? "" : event.getData().getPath();
        if (path.isEmpty()) {
            return;
        }
        dataChanged(client, event, path);
    }

    protected abstract void dataChanged(final CuratorFramework client, final TreeCacheEvent event, final String path);

}
