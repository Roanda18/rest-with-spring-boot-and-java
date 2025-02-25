package br.com.matheus.integrationtests.controller.XmlTest;

import br.com.matheus.configs.TestConfigs;
import br.com.matheus.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheus.integrationtests.vo.AccountCredentialsVO;
import br.com.matheus.integrationtests.vo.PersonVO;
import br.com.matheus.integrationtests.vo.TokenVO;
import br.com.matheus.integrationtests.vo.pagedmodels.PagedModelPerson;
import br.com.matheus.integrationtests.vo.wrappers.WrapperPersonVO;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        person = new PersonVO();
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

                //.asString();
                //TokenVO token = objectMapper.readValue(accessToken, TokenVO.class);
                //assertNotNull(token);
                //assertNotNull(token.getAccessToken());


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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 3 , "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        var people = wrapper.getContent();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        Assertions.assertTrue(foundPersonOne.getEnabled());

        Assertions.assertEquals(153, foundPersonOne.getId());

        Assertions.assertEquals("Anatole", foundPersonOne.getFirstName());
        Assertions.assertEquals("Denekamp", foundPersonOne.getLastName());
        Assertions.assertEquals("0865 6th Alley", foundPersonOne.getAddress());
        Assertions.assertEquals("Male", foundPersonOne.getGender());

        PersonVO foundPersonFive = people.get(5);

        assertNotNull(foundPersonFive.getId());
        assertNotNull(foundPersonFive.getFirstName());
        assertNotNull(foundPersonFive.getLastName());
        assertNotNull(foundPersonFive.getAddress());
        assertNotNull(foundPersonFive.getGender());
        Assertions.assertFalse(foundPersonFive.getEnabled());

        Assertions.assertEquals(869, foundPersonFive.getId());

        Assertions.assertEquals("Angy", foundPersonFive.getFirstName());
        Assertions.assertEquals("Crumb", foundPersonFive.getLastName());
        Assertions.assertEquals("442 Blaine Avenue", foundPersonFive.getAddress());
        Assertions.assertEquals("Female", foundPersonFive.getGender());

    }

    @Test
    @Order(7)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("firstName", "matheus")
                .queryParams("page", 0 , "size", 5, "direction", "asc")
                .when()
                .get("findPersonByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        var people = wrapper.getContent();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 3 , "limit", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Assertions.assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/1007</href></links>"));
        Assertions.assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/861</href></links>"));
        Assertions.assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/268</href></links>"));
        Assertions.assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/860</href></links>"));
        Assertions.assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/209</href></links>"));

        Assertions.assertTrue(content.contains("<page><size>10</size><totalElements>1009</totalElements><totalPages>101</totalPages><number>3</number></page></PagedModel>"));

        Assertions.assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/person/v1?limit=10&amp;direction=asc&amp;page=0&amp;size=10&amp;sort=firstName,asc</href></links>"));
        Assertions.assertTrue(content.contains("<links><rel>prev</rel><href>http://localhost:8888/api/person/v1?limit=10&amp;direction=asc&amp;page=2&amp;size=10&amp;sort=firstName,asc</href></links>"));
        Assertions.assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1?page=3&amp;limit=10&amp;direction=asc</href></links>"));
        Assertions.assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/person/v1?limit=10&amp;direction=asc&amp;page=4&amp;size=10&amp;sort=firstName,asc</href></links>"));
        Assertions.assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/person/v1?limit=10&amp;direction=asc&amp;page=100&amp;size=10&amp;sort=firstName,asc</href></links>"));
    }

    private void mockPerson() {
        person.setFirstName("Joao");
        person.setLastName("Ferras");
        person.setAddress("Joao Pessoa");
        person.setGender("Masculino");
        person.setEnabled(true);
    }

}
