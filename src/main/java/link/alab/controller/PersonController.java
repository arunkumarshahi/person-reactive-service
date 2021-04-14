package link.alab.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;

import link.alab.model.Person;
import link.alab.repository.PersonRespository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PersonController {
	@Bean
	RouterFunction<?> routes(PersonRespository personRespository) {
		return nest(path("/person"),
				route(RequestPredicates.GET("/{id}"),
						request -> ok().body(personRespository.findById(request.pathVariable("id")), Person.class))
								.andRoute(RequestPredicates.GET("/"),
										request -> ok().body(personRespository.findAll(), Person.class))
								.andRoute(method(HttpMethod.POST), request -> {
									(request.bodyToMono(Person.class))
									.switchIfEmpty(Mono.error(new ServerWebInputException("Request body cannot be empty.")))
									.doOnNext(personRespository::insert)
											.log("inserting in controller :: ")
											
											.subscribe(x -> log.info("message :: {}", x),
													ex -> log.error("error :: {}", ex.getMessage()));
									return ok().build();
								}));
	}
}
