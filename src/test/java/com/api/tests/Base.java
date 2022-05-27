package com.api.tests;

import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;

public class Base {

	String authToken = null;

	@BeforeSuite
	public void setup() {

		String baseHostUrl = System.getProperty("server.base");
		if (baseHostUrl == null) {
			baseHostUrl = "https://reqres.in";
		}
		RestAssured.baseURI = baseHostUrl;

		authToken = System.getProperty("server.auth.token");
		if (authToken == null) {
			authToken = "Token xdfiweu!49fanw@0";
		}

	}

}
