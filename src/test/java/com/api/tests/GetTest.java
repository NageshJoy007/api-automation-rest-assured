package com.api.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

public class GetTest extends Base {
	
	ResponseSpecification resSpec = null;
	RequestSpecification reqSpec = null;

	@Test
	public void getUsers() {

		given().queryParam("page", "2").contentType(ContentType.JSON).when().get("/api/users").then().log().all()
				.statusCode(200).assertThat().body("page", equalTo(2)).assertThat().body("data", hasSize(6))
				.assertThat().body("data.email", hasItems("lindsay.ferguson@reqres.in", "george.edwards@reqres.in"))
				.assertThat().headers("Content-Type", "application/json; charset=utf-8", "Connection", "keep-alive")
				.assertThat().statusLine("HTTP/1.1 200 OK").assertThat()
				.statusCode(both(greaterThanOrEqualTo(200)).and(lessThan(400)));
	}

	@Test
	public void getUsersExtractValue() {

		Response resp = given().queryParam("page", "2").contentType(ContentType.JSON).when().get("/api/users").then()
				.log().all().statusCode(200).assertThat().body("page", equalTo(2)).assertThat().body("data", hasSize(6))
				.assertThat().body("data.email", hasItems("lindsay.ferguson@reqres.in", "george.edwards@reqres.in"))
				.assertThat().headers("Content-Type", "application/json; charset=utf-8", "Connection", "keep-alive")
				.assertThat().statusLine("HTTP/1.1 200 OK").assertThat().time(lessThan(2000L)).extract().response();

		JsonPath jsp = new JsonPath(resp.asString());
		System.out.println(jsp.getString("data[0].email"));
	}

	@BeforeClass
	public void setupReqResSpec() {

		reqSpec = RestAssured.given();
		reqSpec.contentType(ContentType.JSON);

		resSpec = RestAssured.expect();
		resSpec.contentType(ContentType.JSON);
		resSpec.statusLine("HTTP/1.1 200 OK");
		resSpec.time(lessThan(2000L));
		resSpec.statusCode(200);

	}

	@Test
	public void getUsersResSpec() {
		given().queryParam("page", "2").contentType(ContentType.JSON).when().get("/api/users").then().spec(resSpec)
				.and().assertThat()
				.body("data.email", hasItems("lindsay.ferguson@reqres.in", "george.edwards@reqres.in"));
	}

	@Test
	public void getUsersReqResSpec() {
		reqSpec.queryParam("page", "2").when().get("/api/users").then().spec(resSpec).assertThat().body("data.email",
				hasItems("lindsay.ferguson@reqres.in", "george.edwards@reqres.in"));
	}

	@Test
	public void getUser() {
		reqSpec.when().get("/api/users/3").then().log().all().assertThat().statusCode(200)
				.assertThat().body("data.email", equalTo("emma.wong@reqres.in"));

	}

	@Test
	public void getUsersJSONSchemaValidation() {

		File schema = new File(System.getProperty("user.dir") + "/src/test/resources/schema/schema1.json");
		given().queryParam("page", "2").contentType(ContentType.JSON).when().get("/api/users").then().spec(resSpec)
				.body(matchesJsonSchema(schema));

	}
	
	

}
