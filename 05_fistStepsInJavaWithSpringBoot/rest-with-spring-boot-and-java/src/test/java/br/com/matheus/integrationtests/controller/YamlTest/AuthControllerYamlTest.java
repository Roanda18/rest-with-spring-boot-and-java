package br.com.matheus.integrationtests.controller.YamlTest;


import br.com.matheus.configs.TestConfigs;
import br.com.matheus.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheus.integrationtests.vo.AccountCredentialsVO;
import br.com.matheus.integrationtests.vo.TokenVO;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static TokenVO tokenVO;
    private static YmlMapper ymlMapper;

    @BeforeAll
    public static void setup (){
        ymlMapper = new YmlMapper();
    }

    @Test
    @Order(1)
    public void testSignin() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsVO user = new AccountCredentialsVO("matheus", "admin556");

        RequestSpecification specification = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        tokenVO = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(user, ymlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, ymlMapper);


        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }
    @Test
    @Order(2)
    public void testRefresh() throws JsonMappingException, JsonProcessingException {


        AccountCredentialsVO user = new AccountCredentialsVO("matheus", "admin556");

        var newTokenVO = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("username", tokenVO.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class,ymlMapper);


        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}
