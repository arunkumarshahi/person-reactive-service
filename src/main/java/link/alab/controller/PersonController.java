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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
@Configuration
@Slf4j
public class PersonController {
	@Autowired
	PersonRespository personRespository;
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
									.doOnNext(personRespository::save)
											.log("inserting in controller :: ")
											
											.subscribe(x -> log.info("message :: {}", x),
													ex -> log.error("error :: {}", ex.getMessage()));
									return ok().build();
								})
								.andRoute(POST("/update"), 
									      req -> req.body(toMono(Person.class))
									        .doOnNext(personRespository::save)
									        .log()
									        .then(ok().build()))	
				);
	}
	
	 @Bean
	    RouterFunction<ServerResponse> updateEmployeeRoute() {
		 List<Person> personList=new ArrayList<>();
			Person person=new Person();
			person.setAge(2);
			person.setName("Arun k");
			personList.add(person);
			personRespository.insert(Flux.fromIterable(personList)).log()
			  .subscribe();
			
	      return route(POST("/employees/update"), 
	        req -> req.body(toMono(Person.class))
	                  .doOnNext(personRespository::save)
	                  .log()
	                  .then(ok().build()));
	    }
}
