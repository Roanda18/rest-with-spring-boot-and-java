package br.com.matheus.integrationtests.Repository;

import br.com.matheus.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheus.model.Person;
import br.com.matheus.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    public PersonRepository repository;
    private static Person person;

    @BeforeAll
    public static void setup() {
        person = new Person();
    }

    @Test
    @Order(0)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonByName("matheus", pageable).getContent().get(0);

        Assertions.assertNotNull(person.getId());
        Assertions.assertNotNull(person.getFirstName());
        Assertions.assertNotNull(person.getLastName());
        Assertions.assertNotNull(person.getAddress());
        Assertions.assertNotNull(person.getGender());

        Assertions.assertEquals(1, person.getId());

        Assertions.assertEquals("Matheus", person.getFirstName());
        Assertions.assertEquals("Matos", person.getLastName());
        Assertions.assertEquals("Brasilia", person.getAddress());
        Assertions.assertEquals("Male", person.getGender());

    }
    @Test
    @Order(1)
    public void testDisablePerson() throws JsonMappingException, JsonProcessingException {

        repository.disablePerson(person.getId());
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonByName("matheus", pageable).getContent().get(0);

        Assertions.assertNotNull(person.getId());
        Assertions.assertNotNull(person.getFirstName());
        Assertions.assertNotNull(person.getLastName());
        Assertions.assertNotNull(person.getAddress());
        Assertions.assertNotNull(person.getGender());

        Assertions.assertFalse(person.getEnabled());

        Assertions.assertEquals(1, person.getId());

        Assertions.assertEquals("Matheus", person.getFirstName());
        Assertions.assertEquals("Matos", person.getLastName());
        Assertions.assertEquals("Brasilia", person.getAddress());
        Assertions.assertEquals("Male", person.getGender());

    }
}
