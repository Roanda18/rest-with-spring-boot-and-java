package br.com.matheus.integrationtests.controller.JsonTest;

import br.com.matheus.configs.TestConfigs;
import br.com.matheus.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheus.integrationtests.vo.AccountCredentialsVO;
import br.com.matheus.integrationtests.vo.PersonVO;
import br.com.matheus.integrationtests.vo.TokenVO;
import br.com.matheus.integrationtests.vo.wrappers.WrapperPersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("matheus", "admin556");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }


    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;
        Assertions.assertNotNull(persistedPerson);
        Assertions.assertNotNull(persistedPerson.getId());
        Assertions.assertNotNull(persistedPerson.getFirstName());
        Assertions.assertNotNull(persistedPerson.getLastName());
        Assertions.assertNotNull(persistedPerson.getAddress());
        Assertions.assertNotNull(persistedPerson.getGender());
        Assertions.assertTrue(persistedPerson.getEnabled());

        Assertions.assertTrue(persistedPerson.getId() > 0);

        Assertions.assertEquals("Joao", persistedPerson.getFirstName());
        Assertions.assertEquals("Ferras", persistedPerson.getLastName());
        Assertions.assertEquals("Joao Pessoa", persistedPerson.getAddress());
        Assertions.assertEquals("Masculino", persistedPerson.getGender());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        person.setLastName("Mauricio");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;
        Assertions.assertNotNull(persistedPerson);
        Assertions.assertNotNull(persistedPerson.getId());
        Assertions.assertNotNull(persistedPerson.getFirstName());
        Assertions.assertNotNull(persistedPerson.getLastName());
        Assertions.assertNotNull(persistedPerson.getAddress());
        Assertions.assertNotNull(persistedPerson.getGender());
        Assertions.assertTrue(persistedPerson.getEnabled());

        Assertions.assertEquals(person.getId(), persistedPerson.getId());

        Assertions.assertEquals("Joao", persistedPerson.getFirstName());
        Assertions.assertEquals("Mauricio", persistedPerson.getLastName());
        Assertions.assertEquals("Joao Pessoa", persistedPerson.getAddress());
        Assertions.assertEquals("Masculino", persistedPerson.getGender());
    }

    @Test
    @Order(3)
    public void testDisablePersonById() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGINS, TestConfigs.ORIGIN_MATHEUS)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;
        Assertions.assertNotNull(persistedPerson);
        Assertions.assertNotNull(persistedPerson.getId());
        Assertions.assertNotNull(persistedPerson.getFirstName());
        Assertions.assertNotNull(persistedPerson.getLastName());
        Assertions.assertNotNull(persistedPerson.getAddress());
        Assertions.assertNotNull(persistedPerson.getGender());
        Assertions.assertFalse(persistedPerson.getEnabled());

        Assertions.assertEquals(person.getId(), persistedPerson.getId());

        Assertions.assertEquals("Joao", persistedPerson.getFirstName());
        Assertions.assertEquals("Mauricio", persistedPerson.getLastName());
        Assertions.assertEquals("Joao Pessoa", persistedPerson.getAddress());
        Assertions.assertEquals("Masculino", persistedPerson.getGender());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGINS, TestConfigs.ORIGIN_MATHEUS)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;
        Assertions.assertNotNull(persistedPerson);
        Assertions.assertNotNull(persistedPerson.getId());
        Assertions.assertNotNull(persistedPerson.getFirstName());
        Assertions.assertNotNull(persistedPerson.getLastName());
        Assertions.assertNotNull(persistedPerson.getAddress());
        Assertions.assertNotNull(persistedPerson.getGender());
        Assertions.assertFalse(persistedPerson.getEnabled());

        Assertions.assertEquals(person.getId(), persistedPerson.getId());

        Assertions.assertEquals("Joao", persistedPerson.getFirstName());
        Assertions.assertEquals("Mauricio", persistedPerson.getLastName());
        Assertions.assertEquals("Joao Pessoa", persistedPerson.getAddress());
        Assertions.assertEquals("Masculino", persistedPerson.getGender());
    }



    @Test
    @Order(5)
    public void testDelete() throws JsonProcessingException {

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }

    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3 , "limit", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
                //.as(new TypeRef<List<PersonVO>>() {});

        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapper.getEmbedded().getPersons();

        PersonVO foundPersonOne = people.get(0);

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getFirstName());
        Assertions.assertNotNull(foundPersonOne.getLastName());
        Assertions.assertNotNull(foundPersonOne.getAddress());
        Assertions.assertNotNull(foundPersonOne.getGender());
        Assertions.assertTrue(foundPersonOne.getEnabled());

        Assertions.assertEquals(1007, foundPersonOne.getId());

        Assertions.assertEquals("Allyson", foundPersonOne.getFirstName());
        Assertions.assertEquals("Switland", foundPersonOne.getLastName());
        Assertions.assertEquals("114 Corscot Way", foundPersonOne.getAddress());
        Assertions.assertEquals("Female", foundPersonOne.getGender());
        
        PersonVO foundPersonFive = people.get(5);

        Assertions.assertNotNull(foundPersonFive.getId());
        Assertions.assertNotNull(foundPersonFive.getFirstName());
        Assertions.assertNotNull(foundPersonFive.getLastName());
        Assertions.assertNotNull(foundPersonFive.getAddress());
        Assertions.assertNotNull(foundPersonFive.getGender());
        Assertions.assertTrue(foundPersonFive.getEnabled());

        Assertions.assertEquals(451, foundPersonFive.getId());

        Assertions.assertEquals("Alyss", foundPersonFive.getFirstName());
        Assertions.assertEquals("Carek", foundPersonFive.getLastName());
        Assertions.assertEquals("352 Sachs Circle", foundPersonFive.getAddress());
        Assertions.assertEquals("Female", foundPersonFive.getGender());

    }

    @Test
    @Order(7)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("firstName", "matheus")
                .queryParams("page", 0 , "limit", 5, "direction", "asc")
                .when()
                .get("findPersonByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
                //.as(new TypeRef<List<PersonVO>>() {});

        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapper.getEmbedded().getPersons();

        PersonVO foundPersonOne = people.get(0);

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getFirstName());
        Assertions.assertNotNull(foundPersonOne.getLastName());
        Assertions.assertNotNull(foundPersonOne.getAddress());
        Assertions.assertNotNull(foundPersonOne.getGender());
        Assertions.assertTrue(foundPersonOne.getEnabled());

        Assertions.assertEquals(1, foundPersonOne.getId());

        Assertions.assertEquals("Matheus", foundPersonOne.getFirstName());
        Assertions.assertEquals("Matos", foundPersonOne.getLastName());
        Assertions.assertEquals("Brasilia", foundPersonOne.getAddress());
        Assertions.assertEquals("Male", foundPersonOne.getGender());

    }

    @Test
    @Order(8)
    public void testHateoas() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3 , "limit", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        //.as(new TypeRef<List<PersonVO>>() {});

        Assertions.assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/1007\"}}}"));
        Assertions.assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/861\"}}}"));
        Assertions.assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/339\"}}}"));
        Assertions.assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/387\"}}}"));
        Assertions.assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/451\"}}}"));

        Assertions.assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1009,\"totalPages\":101,\"number\":3}}"));

        Assertions.assertTrue(content.contains("\"_links\":{\"first\":{\"href\":\"http://localhost:8888/api/person/v1?limit=10&direction=asc&page=0&size=10&sort=firstName,asc\"}"));
        Assertions.assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?limit=10&direction=asc&page=2&size=10&sort=firstName,asc\"}"));
        Assertions.assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&limit=10&direction=asc\"}"));
        Assertions.assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?limit=10&direction=asc&page=4&size=10&sort=firstName,asc\"}"));
        Assertions.assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?limit=10&direction=asc&page=100&size=10&sort=firstName,asc\"}}"));

    }

    private void mockPerson() {
        person.setFirstName("Joao");
        person.setLastName("Ferras");
        person.setAddress("Joao Pessoa");
        person.setGender("Masculino");
        person.setEnabled(true);
    }

}
