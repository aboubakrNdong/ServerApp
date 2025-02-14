package com.getarrays.server;

import com.getarrays.server.model.Server;
import com.getarrays.server.repository.ServerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.getarrays.server.enumerations.StatusEnum.SERVER_DOWN;
import static com.getarrays.server.enumerations.StatusEnum.SERVER_UP;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}


	@Bean
	CommandLineRunner run(ServerRepository serverRepository) {
		return args -> {
			serverRepository.save(new Server(null, "192.168.1.160","Ubuntu Linux","16GB", "Personal PC",
					"http://localhost:8080/server/image/server1.png", SERVER_UP));
			serverRepository.save(new Server(null, "192.168.1.59","Fedora Linux","16GB", "Dell Tower",
					"http://localhost:8080/server/image/serve2.png", SERVER_UP));
			serverRepository.save(new Server(null, "192.168.1.12","MS 20008","32GB", "Web Server",
					"http://localhost:8080/server/image/server3.png", SERVER_DOWN));
			serverRepository.save(new Server(null, "192.168.1.43","Red Hat enterprise Linux","64GB", "Mail Server",
					"http://localhost:8080/server/image/server4.png", SERVER_UP));
		};
	}
}
