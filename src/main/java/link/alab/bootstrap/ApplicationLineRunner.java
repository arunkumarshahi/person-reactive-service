package link.alab.bootstrap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import link.alab.model.Person;
import link.alab.repository.PersonRespository;
import reactor.core.publisher.Flux;
@Component
public class ApplicationLineRunner implements CommandLineRunner {
	@Autowired
	PersonRespository personRespository;

	@Override
	public void run(String... args) throws Exception {
		List<Person> personList=new ArrayList<>();
		Person person=new Person();
		person.setAge(2);
		person.setName("Shravya");
		personList.add(person);
		personRespository.insert(Flux.fromIterable(personList)).log()
		  .subscribe();
		
	}

}
