package com.getarrays.services.implementation;

import com.getarrays.enumerations.StatusEnum;
import com.getarrays.model.Server;
import com.getarrays.repository.ServerRepository;
import com.getarrays.services.ServerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;

import static com.getarrays.enumerations.StatusEnum.SERVER_DOWN;
import static com.getarrays.enumerations.StatusEnum.SERVER_UP;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
       log.info("Saving new Server : {}", server.getName());
       server.setImageUrl(setServerImageUrl()); //TODO: write code for the setServerImageUrl at the bottom
       return serverRepository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatusEnum(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        return List.of();
    }

    @Override
    public Server get(Long id) {
        return null;
    }

    @Override
    public Server update(Server server) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }
    //TODO: implements this method
    private String setServerImageUrl() {
        return null;
    }
}
