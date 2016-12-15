package com.steve.illuminator.core.listener;

import com.steve.illuminator.core.Constants;
import com.steve.illuminator.core.reg.AbstractRegistryCenter;
import com.steve.illuminator.core.services.LeaderElectionService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * Created by xuh18 on 6/27/16.
 */
public class LeaderElectionListener extends AbstractJobListener {

    LeaderElectionService leaderElectionService;

    AbstractRegistryCenter registryCenter;

    public LeaderElectionListener(AbstractRegistryCenter registryCenter){
        this.registryCenter = registryCenter;
        leaderElectionService = new LeaderElectionService(registryCenter);
    }

    @Override
    protected void dataChanged(CuratorFramework client, TreeCacheEvent event, String path) {
        if (!leaderElectionService.hasLeader()) {
            leaderElectionService.executeLeader();
        }
        if((Constants.hosts+"/"+registryCenter.get(Constants.leaderNode)).equals(path)
                && TreeCacheEvent.Type.NODE_REMOVED == event.getType()){
            leaderElectionService.executeLeader();
        }
    }
}
