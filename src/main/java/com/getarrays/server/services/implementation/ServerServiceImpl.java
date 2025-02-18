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
import static com.getarrays.server.utils.Constantes.*;
import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final Random random = new Random();

    @Override
    public Server createNewServer(final Server server) {
        log.info("Creating new server: {}", server.getName());
        server.setImageUrl(generateServerImageUrl());
        return serverRepository.save(server);
    }


    private String generateServerImageUrl() {
        String randomImage = SERVER_IMAGES[random.nextInt(SERVER_IMAGES.length)];
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(IMAGE_PATH + randomImage)
                .toUriString();
    }

    @Override
    public Server pingServerWithIpAddress(final String ipAddress) throws IOException {
       log.info("Pinging server IP: {}", ipAddress);
       Server server = findServerByIpAddress(ipAddress);
       updateServerStatus(server, ipAddress);
       return serverRepository.save(server);
   }

    private Server findServerByIpAddress(final String ipAddress) {
        return serverRepository.findByIpAddress(ipAddress);
    }

    private void updateServerStatus(final Server server, final String ipAddress) throws IOException {
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatusEnum(address.isReachable(PING_TIMEOUT_MS) ? SERVER_UP : SERVER_DOWN);
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

}
