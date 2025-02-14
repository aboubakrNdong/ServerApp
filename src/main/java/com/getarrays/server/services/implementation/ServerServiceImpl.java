package com.getarrays.server.services.implementation;

import com.getarrays.server.model.Server;
import com.getarrays.server.repository.ServerRepository;
import com.getarrays.server.services.ServerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static com.getarrays.server.enumerations.StatusEnum.SERVER_DOWN;
import static com.getarrays.server.enumerations.StatusEnum.SERVER_UP;
import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Server createNewServer(Server server) {
       log.info("Saving new Server : {}", server.getName());
       server.setImageUrl(setServerImageUrl());
       return serverRepository.save(server);
    }

    @Override
    public Server pingServerWithIpAddress(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatusEnum(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> getAllServers(int limit) {
        log.info("Fetching all servers");
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server getServerById(Long id) {
        log.info("Fetching server by Id: {}", id);
        return serverRepository.findById(id).get();
    }


    @Override
    public Boolean deleteServerById(Long id) {
        log.info("Deleting server by ID: {}", id);
        serverRepository.deleteById(id);
        return TRUE;
    }

    private String setServerImageUrl() {
        String[] imagesNames = {"server1.jpg", "server2.jpg", "server3.jpg", "server4.jpg"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imagesNames[new Random().nextInt(4)]).toUriString();
    }
}
