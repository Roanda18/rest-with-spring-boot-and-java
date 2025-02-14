package br.com.matheus.integrationtests.controller.YamlTest;

import br.com.matheus.configs.TestConfigs;
import br.com.matheus.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheus.integrationtests.vo.AccountCredentialsVO;
import br.com.matheus.integrationtests.vo.BookVO;
import br.com.matheus.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;

import java.lang.reflect.Type;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static BookVO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("matheus", "admin556");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }


    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;
        Assertions.assertNotNull(persistedBook);
        Assertions.assertNotNull(persistedBook.getId());
        Assertions.assertNotNull(persistedBook.getNameBook());
        Assertions.assertNotNull(persistedBook.getNameAuthor());
        Assertions.assertNotNull(persistedBook.getDescription());
        Assertions.assertNotNull(persistedBook.getGender());

        Assertions.assertTrue(persistedBook.getId() > 0);

        Assertions.assertEquals("O Senhor dos Anéis", persistedBook.getNameBook());
        Assertions.assertEquals("J.R.R. Tolkien", persistedBook.getNameAuthor());
        Assertions.assertEquals("Uma jornada épica pela Terra Média", persistedBook.getDescription());
        Assertions.assertEquals("Fantasia", persistedBook.getGender());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        book.setGender("Fantasia Medieval");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;
        Assertions.assertNotNull(persistedBook);
        Assertions.assertNotNull(persistedBook.getId());
        Assertions.assertNotNull(persistedBook.getNameBook());
        Assertions.assertNotNull(persistedBook.getNameAuthor());
        Assertions.assertNotNull(persistedBook.getDescription());
        Assertions.assertNotNull(persistedBook.getGender());

        Assertions.assertTrue(persistedBook.getId() > 0);

        Assertions.assertEquals("O Senhor dos Anéis", persistedBook.getNameBook());
        Assertions.assertEquals("J.R.R. Tolkien", persistedBook.getNameAuthor());
        Assertions.assertEquals("Uma jornada épica pela Terra Média", persistedBook.getDescription());
        Assertions.assertEquals("Fantasia Medieval", persistedBook.getGender());
    }


    @Test
    @Order(3)
    public void testFindById() throws JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .header(TestConfigs.HEADER_PARAM_ORIGINS, TestConfigs.ORIGIN_MATHEUS)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;
        Assertions.assertNotNull(persistedBook);
        Assertions.assertNotNull(persistedBook.getId());
        Assertions.assertNotNull(persistedBook.getNameBook());
        Assertions.assertNotNull(persistedBook.getNameAuthor());
        Assertions.assertNotNull(persistedBook.getDescription());
        Assertions.assertNotNull(persistedBook.getGender());

        Assertions.assertTrue(persistedBook.getId() > 0);

        Assertions.assertEquals("O Senhor dos Anéis", persistedBook.getNameBook());
        Assertions.assertEquals("J.R.R. Tolkien", persistedBook.getNameAuthor());
        Assertions.assertEquals("Uma jornada épica pela Terra Média", persistedBook.getDescription());
        Assertions.assertEquals("Fantasia Medieval", persistedBook.getGender());
    }

    @Test
    @Order(4)
    public void testDelete() throws JsonProcessingException {

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        //.as(new TypeRef<List<PersonVO>>() {});

        List<BookVO> book = objectMapper.readValue(content, new TypeReference<List<BookVO>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });

        BookVO foundBookOne = book.get(0);

        Assertions.assertNotNull(foundBookOne.getId());
        Assertions.assertNotNull(foundBookOne.getNameBook());
        Assertions.assertNotNull(foundBookOne.getNameAuthor());
        Assertions.assertNotNull(foundBookOne.getDescription());
        Assertions.assertNotNull(foundBookOne.getGender());

        Assertions.assertEquals(1, foundBookOne.getId());

        Assertions.assertEquals("Game of Thrones", foundBookOne.getNameBook());
        Assertions.assertEquals("George R. R. Martin", foundBookOne.getNameAuthor());
        Assertions.assertEquals("História de dragões", foundBookOne.getDescription());
        Assertions.assertEquals("Fantasia", foundBookOne.getGender());

        BookVO foundBookFive = book.get(5);

        Assertions.assertNotNull(foundBookFive.getId());
        Assertions.assertNotNull(foundBookFive.getNameBook());
        Assertions.assertNotNull(foundBookFive.getNameAuthor());
        Assertions.assertNotNull(foundBookFive.getDescription());
        Assertions.assertNotNull(foundBookFive.getGender());

        Assertions.assertEquals(6, foundBookFive.getId());

        Assertions.assertEquals("O Código Da Vinci", foundBookFive.getNameBook());
        Assertions.assertEquals("Dan Brown", foundBookFive.getNameAuthor());
        Assertions.assertEquals("Um mistério envolvendo arte e história", foundBookFive.getDescription());
        Assertions.assertEquals("Suspense", foundBookFive.getGender());

    }

    private void mockBook() {
        book.setNameBook("O Senhor dos Anéis");
        book.setNameAuthor("J.R.R. Tolkien");
        book.setDescription("Uma jornada épica pela Terra Média");
        book.setGender("Fantasia");
    }

}
