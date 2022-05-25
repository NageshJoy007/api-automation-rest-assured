package com.api.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import java.io.File;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PostPutAndDeleteTest extends Base {

	String userName = null;

	@Test(priority = 1)
	public void createUser() {
		JSONObject payload = new JSONObject();
		payload.put("name", "morpheus");
		payload.put("job", "leader");

		File schema = new File(System.getProperty("user.dir") + "/src/test/resources/schema/schema2.json");
		Response response = given().contentType(ContentType.JSON).body(payload).when().post("/api/users").then().log()
				.all().assertThat().statusCode(201).body(matchesJsonSchema(schema)).time(lessThan(2000L)).extract()
				.response();
		JsonPath jsp = new JsonPath(response.asString());
		userName = jsp.getString("name");
	}

	@Test(priority = 2)
	public void updateUser() {
		String payload = "{\"name\": \"morpheus\", \"job\": \"technical lead\"}";
		File schema = new File(System.getProperty("user.dir") + "/src/test/resources/schema/schema3.json");
		given().contentType(ContentType.JSON).body(payload).when().put("/api/users/2").then().log().all().assertThat()
				.statusCode(200).assertThat().body("name", equalTo(userName)).assertThat()
				.body(matchesJsonSchema(schema)).assertThat().body("job", equalTo("technical lead"));

	}

	@Test(priority = 3)
	public void deleteUser() {

		given().pathParam("userId", "2").when().delete("/api/users/{userId}").then().log().all().assertThat()
				.statusCode(204);
	}
	
	@Test
	public void userLogin() {
		File payload = new File(System.getProperty("user.dir") + "/src/test/resources/bodies/payload1.json");
		File schema = new File(System.getProperty("user.dir") + "/src/test/resources/schema/schema4.json");
		given().contentType(ContentType.JSON).body(payload)
		.when().post("/api/login")
		.then().log().all()
		.assertThat().body(matchesJsonSchema(schema)).statusCode(200);

	}

}
