package com.api.tests;

import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;

public class Base {
	
	@BeforeSuite
	public void setup() {
		
		String baseHostUrl = System.getProperty("server.base");
		if(baseHostUrl==null) {
			baseHostUrl = "https://reqres.in";
		}
		RestAssured.baseURI =baseHostUrl;
	}

}
