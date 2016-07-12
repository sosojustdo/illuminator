package com.steve.illuminator.core.services;

import com.steve.illuminator.core.Constants;
import com.steve.illuminator.core.excpetion.ExceptionHandler;
import com.steve.illuminator.core.reg.AbstractRegistryCenter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.springframework.stereotype.Service;

/**
 * Created by xuh18 on 6/27/16.
 */
public class LeaderElectionService {

    AbstractRegistryCenter registryCenter;

    public LeaderElectionService(AbstractRegistryCenter registryCenter){
        this.registryCenter = registryCenter;
    }

    public boolean hasLeader(){
        return registryCenter.isExisted(Constants.leaderNode);
    }

    public boolean isLeader(){
        String nodeId = registryCenter.getJobConfig().getNodeId();
        return nodeId.equals(registryCenter.get(Constants.leaderNode));
    }

    public void executeLeader(){
        try (LeaderLatch latch = new LeaderLatch((CuratorFramework) registryCenter.getRawClient(), Constants.leaderLatchNode)) {
            latch.start();
            latch.await();
            registryCenter.persistEphemeral(Constants.leaderNode, registryCenter.getJobConfig().getNodeId());
        } catch (final Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }


    public void addDataListener(final TreeCacheListener listener) {
        TreeCache cache = (TreeCache) registryCenter.getRawCache(Constants.hosts);
        cache.getListenable().addListener(listener);
    }



}
