package com.jkx.geteway.lb;

import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ConfigurationBasedServerList;
import com.netflix.loadbalancer.Server;

import java.util.List;

/**
 * Created by Taoxs on 2017/12/26.
 */
public class LocalFileConfigurationServerList extends ConfigurationBasedServerList {

    @Override
    public List<Server> getInitialListOfServers() {
        ConfigurationManager.getConfigInstance().setProperty(
                "sample-client.ribbon.listOfServers", "");
        ConfigurationManager.getConfigInstance().getProperties("");
        return super.getInitialListOfServers();
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        return super.getUpdatedListOfServers();
    }
}
