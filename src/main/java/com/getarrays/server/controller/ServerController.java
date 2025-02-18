package com.getarrays.server.controller;

import com.getarrays.server.model.Response;
import com.getarrays.server.model.Server;
import com.getarrays.server.services.implementation.ServerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import static com.getarrays.server.utils.Constantes.*;

import static com.getarrays.server.enumerations.StatusEnum.SERVER_UP;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {



    private final ServerServiceImpl serverService;


    @GetMapping("/getservers") 
    public ResponseEntity<Response> getAllServers() {
        return createResponse(DATA_KEY_SERVERS,serverService.getAllServers(DEFAULT_PAGE_SIZE),
                MSG_SERVERS_RETRIEVED,
                OK);
    }

    @GetMapping("/pingserver/{ipAddress}") 
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.pingServerWithIpAddress(ipAddress);
        String message = server.getStatusEnum() == SERVER_UP ? MSG_PING_SUCCESS: MSG_PING_FAILED;
        return createResponse(DATA_KEY_SERVER, server, message, OK);
    }

    @PostMapping("/saveserver")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
        return createResponse(
                DATA_KEY_SERVER,
                serverService.createNewServer(server),
                MSG_SERVER_CREATED,
                CREATED
        );
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServerById(@PathVariable("id") Long id) {
        return createResponse(
                DATA_KEY_SERVER,
                serverService.getServerById(id),
                MSG_SERVER_RETRIEVED,
                OK
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServerById(@PathVariable("id") Long id) {
        return createResponse(DATA_KEY_DELETED,
                serverService.deleteServerById(id),
                MSG_SERVER_DELETED,
                OK
                );
    }

    private ResponseEntity<Response> createResponse(String key, Object data, String message, HttpStatus status) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of(key, data))
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .build()
        );
    }


    @GetMapping(path = "/image/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return loadImageFile(fileName);
    }

    private byte[] loadImageFile(String fileName) throws IOException {
        Path imagePath = Paths.get(IMAGES_PATH + fileName);
        if (!Files.exists(imagePath)) {
            throw new IOException("Image file not found: " + fileName);
        }
        return Files.readAllBytes(imagePath);
    }


}
