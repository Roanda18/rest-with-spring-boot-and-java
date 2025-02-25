package br.com.matheus.integrationtests.controller.YamlTest;

import br.com.matheus.configs.TestConfigs;
import br.com.matheus.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheus.integrationtests.vo.AccountCredentialsVO;
import br.com.matheus.integrationtests.vo.BookVO;
import br.com.matheus.integrationtests.vo.TokenVO;
import br.com.matheus.integrationtests.vo.pagedmodels.PagedModelBook;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YmlMapper objectMapper;

    private static BookVO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new YmlMapper();
        book = new BookVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("matheus", "admin556");

        var accessToken = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(user, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, objectMapper)
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

        var persistedBook = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(book, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, objectMapper);

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

        var persistedBook = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(book, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, objectMapper);

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

        var persistedBook = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .header(TestConfigs.HEADER_PARAM_ORIGINS, TestConfigs.ORIGIN_MATHEUS)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, objectMapper);

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
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var wrapper = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelBook.class, objectMapper);

        var book = wrapper.getContent();

        BookVO foundBookOne = book.get(0);

        Assertions.assertNotNull(foundBookOne.getId());
        Assertions.assertNotNull(foundBookOne.getNameBook());
        Assertions.assertNotNull(foundBookOne.getNameAuthor());
        Assertions.assertNotNull(foundBookOne.getDescription());
        Assertions.assertNotNull(foundBookOne.getGender());

        Assertions.assertEquals(419, foundBookOne.getId());

        Assertions.assertEquals("All the President's Men", foundBookOne.getNameBook());
        Assertions.assertEquals("Julienne Glowacz", foundBookOne.getNameAuthor());
        Assertions.assertEquals("NextEra Energy, Inc.", foundBookOne.getDescription());
        Assertions.assertEquals("Suspense", foundBookOne.getGender());

        BookVO foundBookFive = book.get(5);

        Assertions.assertNotNull(foundBookFive.getId());
        Assertions.assertNotNull(foundBookFive.getNameBook());
        Assertions.assertNotNull(foundBookFive.getNameAuthor());
        Assertions.assertNotNull(foundBookFive.getDescription());
        Assertions.assertNotNull(foundBookFive.getGender());

        Assertions.assertEquals(324, foundBookFive.getId());

        Assertions.assertEquals("Amen.", foundBookFive.getNameBook());
        Assertions.assertEquals("Vanessa Wyrall", foundBookFive.getNameAuthor());
        Assertions.assertEquals("AXT Inc", foundBookFive.getDescription());
        Assertions.assertEquals("Aventura", foundBookFive.getGender());

    }

    @Test
    @Order(6)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        var wrapper = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("nameBook", "principe")
                .queryParams("page", 0, "size", 5, "direction", "asc")
                .when()
                .get("findBookByName/{nameBook}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelBook.class, objectMapper);

        var book = wrapper.getContent();

        BookVO foundBookOne = book.get(0);

        Assertions.assertNotNull(foundBookOne.getId());
        Assertions.assertNotNull(foundBookOne.getNameBook());
        Assertions.assertNotNull(foundBookOne.getNameAuthor());
        Assertions.assertNotNull(foundBookOne.getDescription());
        Assertions.assertNotNull(foundBookOne.getGender());

        Assertions.assertEquals(10, foundBookOne.getId());

        Assertions.assertEquals("O Pequeno Príncipe", foundBookOne.getNameBook());
        Assertions.assertEquals("Antoine de Saint-Exupéry", foundBookOne.getNameAuthor());
        Assertions.assertEquals("Uma história sobre amizade e amor", foundBookOne.getDescription());
        Assertions.assertEquals("Clássico", foundBookOne.getGender());

    }

    @Test
    @Order(7)
    public void testHateoas() throws JsonMappingException, JsonProcessingException {

        var unthreatedContent = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 3, "limit", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var content = unthreatedContent.replace("\n", "").replace("\r", "");

        Assertions.assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/book/v1/284\""));
        Assertions.assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/book/v1/496\""));
        Assertions.assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/book/v1/66\""));
        Assertions.assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/book/v1/894\""));
        Assertions.assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/book/v1/380\""));

        Assertions.assertTrue(content.contains("page:  size: 10  totalElements: 1010  totalPages: 101  number: 3"));

        Assertions.assertTrue(content.contains("rel: \"first\"  href: \"http://localhost:8888/api/book/v1?limit=10&direction=asc&page=0&size=10&sort=nameBook,asc\""));
        Assertions.assertTrue(content.contains("rel: \"prev\"  href: \"http://localhost:8888/api/book/v1?limit=10&direction=asc&page=2&size=10&sort=nameBook,asc\""));
        Assertions.assertTrue(content.contains("rel: \"self\"  href: \"http://localhost:8888/api/book/v1?page=3&limit=10&direction=asc\""));
        Assertions.assertTrue(content.contains("rel: \"next\"  href: \"http://localhost:8888/api/book/v1?limit=10&direction=asc&page=4&size=10&sort=nameBook,asc\""));
        Assertions.assertTrue(content.contains("rel: \"last\"  href: \"http://localhost:8888/api/book/v1?limit=10&direction=asc&page=100&size=10&sort=nameBook,asc\""));

    }

    private void mockBook() {
        book.setNameBook("O Senhor dos Anéis");
        book.setNameAuthor("J.R.R. Tolkien");
        book.setDescription("Uma jornada épica pela Terra Média");
        book.setGender("Fantasia");
    }

}
