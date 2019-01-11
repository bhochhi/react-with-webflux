package com.bhochhi.spring.reactwithwebflux;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.UUID;

@SpringBootApplication
public class ReactWithWebfluxApplication {
	@Bean
	CommandLineRunner demoData(CoffeeRepository repo) {
		return args -> {
			repo.deleteAll().thenMany(
					Flux.just("Jet Black Mongo", "Darth Mongo", "Black Alert Mongo")
							.map(name -> new Coffee(UUID.randomUUID().toString(), name))
							.flatMap(repo::save))
					.thenMany(repo.findAll())
					.subscribe(System.out::println);
		};
	}

	@Bean
	WebClient client() {
		return WebClient.create("http://localhost:8080");
	}
	public static void main(String[] args) {
		SpringApplication.run(ReactWithWebfluxApplication.class, args);
	}
}


@RestController
class MongoController {
	private final CoffeeRepository repo;
	private final WebClient client;

	MongoController(CoffeeRepository repo, WebClient client) {
		this.repo = repo;
		this.client = client;
	}

	@GetMapping("/coffees")
	public Flux<Coffee> getAllCoffees() {
		return repo.findAll();
	}

	@GetMapping(value = "/originalcoffees", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Coffee> getOriginalCoffees() {
		return client.get()
				.uri("/coffees")
				.retrieve()
				.bodyToFlux(Coffee.class);
	}

}

interface CoffeeRepository extends ReactiveCrudRepository<Coffee, String> {

}

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
class Coffee {
	@Id
	private String id;
	private String name;
}