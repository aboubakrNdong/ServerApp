package com.getarrays.server.service;

import com.getarrays.server.model.Server;
import com.getarrays.server.repository.ServerRepository;
import com.getarrays.server.services.implementation.ServerServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ServerServiceImpTest {

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerServiceImpl serverService;

    private Server testServer;
    private Server testServer2;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        testServer = new Server();
        testServer2 = new Server();

        testServer.setId(1L);
        testServer.setName("Test Server");
        testServer.setIpAddress("192.168.1.59");

        testServer2.setName("Test Server2");
        testServer2.setIpAddress("192.168.1.55");

        //Set up MockHttpServletRequest
        request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setContextPath("");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterAll
    static void tearDown () {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void createNewServer_ShouldSaveAndReturnServer() {
        // Given
        when(serverRepository.save(any(Server.class)))
                .thenReturn(testServer2);

        // When
        Server result = serverService.createNewServer(testServer2);

        //then
        assertNotNull(result);
        assertNotNull(result.getImageUrl());
        assertTrue(result.getImageUrl().contains(
                "/server/image/"));
        verify(serverRepository).save(any(Server.class));
    }


    @Test
    void getAllServers_ShouldReturnLimitedServers() {

        //Given
        int limit = 2;
        Page<Server> mockPage = new PageImpl<>(Arrays.asList
                (testServer, testServer ));
        when(serverRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        //When
        Collection<Server> results = serverService.getAllServers(limit);

        //Then
        assertEquals(2, results.size());
        verify(serverRepository).findAll(PageRequest.of(0, limit));
    }

    @Test
    void getServerById_ShouldReturnServer() {
        //Given
        when(serverRepository.findById(1L)).thenReturn(Optional.of(testServer));

        //When
        Server result = serverService.getServerById(1L);

        //Then
        assertNotNull(result);
        assertEquals(testServer.getId(), result.getId());
        verify(serverRepository).findById(1L);
    }


    @Test
    void pingServerWithIpAddress_ServerDown_ShouldUpdateStatus()
        throws IOException {
        //Given
        when(serverRepository.findByIpAddress(testServer.getIpAddress())).thenReturn(testServer);
        when(serverRepository.save(any(Server.class))).thenReturn(testServer);

        //When
        Server result = serverService.pingServerWithIpAddress(testServer.getIpAddress());

        //Then
        assertNotNull(result);
        verify(serverRepository).findByIpAddress(testServer.getIpAddress());
        verify(serverRepository).save(any(Server.class));
    }

    @Test
    void createNewServer_WithNullServer_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> serverService
                .createNewServer(null));
        verify(serverRepository, never()).save(any());
    }

    @Test
    void getServerById_WithNonExistentId_ShouldThrowException() {
        when(serverRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> serverService.getServerById(99L));
    }

    @Test
    void getAllServers_WithZeroLimit_ShouldReturnEmptyList() {
        //Given
        when(serverRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        //When
        Collection<Server> results = serverService.getAllServers(30);

        //Then
        assertTrue(results.isEmpty());
        verify(serverRepository).findAll(any(PageRequest.class));

    }
}


