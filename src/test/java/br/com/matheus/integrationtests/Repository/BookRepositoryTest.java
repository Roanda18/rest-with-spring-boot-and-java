package br.com.matheus.integrationtests.Repository;

import br.com.matheus.configs.TestConfigs;
import br.com.matheus.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheus.integrationtests.vo.BookVO;
import br.com.matheus.integrationtests.vo.wrappers.WrapperBookVO;
import br.com.matheus.model.Book;
import br.com.matheus.model.Person;
import br.com.matheus.repositories.BookRepository;
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

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    public BookRepository repository;
    private static Book book;

    @BeforeAll
    public static void setup() {
        book = new Book();
    }

    @Test
    @Order(0)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "nameBook"));
        book = repository.findBookByName("príncipe", pageable).getContent().get(0);

        Assertions.assertNotNull(book.getId());
        Assertions.assertNotNull(book.getNameBook());
        Assertions.assertNotNull(book.getNameAuthor());
        Assertions.assertNotNull(book.getDescription());
        Assertions.assertNotNull(book.getGender());

        Assertions.assertEquals(10, book.getId());

        Assertions.assertEquals("O Pequeno Príncipe", book.getNameBook());
        Assertions.assertEquals("Antoine de Saint-Exupéry", book.getNameAuthor());
        Assertions.assertEquals("Uma história sobre amizade e amor", book.getDescription());
        Assertions.assertEquals("Clássico", book.getGender());
    }
}