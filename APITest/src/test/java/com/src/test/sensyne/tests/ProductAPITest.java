package com.src.test.sensyne.tests;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Constants;

public class ProductAPITest {

	public Integer productCode = null;
	public Float price = null;
	public Integer productName = null;

	@Test(priority = 1)
	public void createProduct() {
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON)
				.body("{\"name\": \"Rose\",\"price\": 9.25}").when().post(Constants.BASE_URI + Constants.API_VERSION + "/product");
		Assert.assertEquals(response.getStatusCode(), 200); // Here status code should be 201 but it is returning me as 200. Also no content is returned 

	}

	@Test(priority = 2)
	public void getProducts() {
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON).when()
				.get(Constants.BASE_URI + Constants.API_VERSION + "/products");
		Assert.assertEquals(response.getStatusCode(), 200);
		JSONArray jsonArray = new JSONArray(response.getBody().asString());

		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject objectInArray = jsonArray.getJSONObject(i);
			String productName = objectInArray.getString("name");
			Assert.assertNotNull(productName);
			if (productName.equalsIgnoreCase("Rose")) {
				productCode = objectInArray.getInt("id");
				Assert.assertNotNull(productCode);
				price = objectInArray.getFloat("price");
				Assert.assertNotNull(price);

			}

		}

	}

	@Test(priority = 3)
	public void updateProduct() {
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON)
				.body("{\"name\": \"Rose\",\"price\": 10.25}").when()
				.put(Constants.BASE_URI + Constants.API_VERSION +"/product/" + productCode);
		Assert.assertEquals(response.getStatusCode(), 200);

	}

	@Test(priority = 4)
	public void getProduct() {
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON).when()
				.get(Constants.BASE_URI + Constants.API_VERSION +"/product/" + productCode);
		Assert.assertEquals(response.getStatusCode(), 200);
		response.then().body("name", Matchers.is("Rose"));
		response.then().body("price", Matchers.is("10.25"));
	}

	@Test(priority = 5)
	public void deleteProduct() {
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON).when()
				.delete(Constants.BASE_URI + Constants.API_VERSION + "/product/" + productCode);
		Assert.assertEquals(response.getStatusCode(), 200);
	}

	@Test(priority = 6)
	public void verifyDeletedProduct() {
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON).when()
				.get(Constants.BASE_URI + Constants.API_VERSION + "/product/" + productCode);
		Assert.assertEquals(response.getStatusCode(), 404);
	}

}
