package com.api.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.io.File;
import org.testng.annotations.Test;
import io.restassured.response.Response;

public class PostGetDeleteFileTest extends Base {

	String fileId = null;

	@Test
	public void uploadAFile() {
		Response uploadFileResponse = given().log().all().header("Content-Type", "multipart/form-data")
				.header("Authorization", authToken)
				.multiPart(new File(System.getProperty("user.dir") + "/src/test/resources/bodies/sample-file.docx"))
				.when().post("/file").then().log().all().assertThat().statusCode(200).assertThat()
				.body(matchesPattern("[a-z 0-9]{8}+-[a-z 0-9]{4}+-[a-z 0-9]{4}+-[a-z 0-9]{4}+-[a-z 0-9]{12}+"))
				.extract().response();
		fileId = uploadFileResponse.body().asString();
		System.out.println("FileId to pass: " + fileId);
	}

	@Test(dependsOnMethods = "uploadAFile")
	public void downloadFile() {
		given().log().all().header("Authorization", authToken).when().get("/file/" + fileId).then().log().all()
				.assertThat().statusCode(either(equalTo(200)).or(equalTo(202)));

	}

	@Test(dependsOnMethods = { "uploadAFile", "downloadFile" })
	public void deleteFile() {
		given().log().all().header("Authorization", authToken).when().delete("/file/" + fileId).then().log().all()
				.assertThat().statusCode(204);

	}
}
