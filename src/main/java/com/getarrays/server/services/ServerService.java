package com.getarrays.server.services;

import com.getarrays.server.model.Server;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {
    Server createNewServer(Server server);
    Server pingServerWithIpAddress(String ipAddress) throws IOException;
    Collection<Server> getAllServers(int limit);
    Server getServerById(Long id);
    Boolean deleteServerById(Long id);
}
