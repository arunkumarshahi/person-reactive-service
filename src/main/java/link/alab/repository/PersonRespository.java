package link.alab.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import link.alab.model.Person;
import reactor.core.publisher.Flux;

public interface PersonRespository extends ReactiveMongoRepository<Person, String> {
    Flux<Person> findByName(String name);
}