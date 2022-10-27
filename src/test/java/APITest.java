import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import static io.restassured.RestAssured.*;

public class APITest {

	public static void main(String[] args) {
		
		// Validate AddPlace API is working
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String response = 
				given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(PayLoad.AddPlace()).
		when().post("maps/api/place/add/json").
		then().assertThat().log().all().statusCode(200)
		.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		String place_id = rawToJson(response, "place_id");
		
		
		// Validate UpdatePlace API is working
		String newAddress = "770 Summer walk, USA";
		
		given().queryParam("key", "qaclick()").header("Content-Type", "application/json").
		body("{\r\n"
				+ "\"place_id\":\""+place_id+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "").
		when().put("maps/api/place/update/json").
		then().assertThat().log().all()
		.statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		// Validate GetPlace API is working
		String getAddressResponse = given().queryParam("key", "qaclick123").queryParam("place_id", place_id).
		when().get("maps/api/place/get/json").
		then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		String actualAddress = rawToJson(getAddressResponse, "address");
		System.out.println(actualAddress);
		
		// TestNG validation
		Assert.assertEquals(newAddress, actualAddress);

	}
	
	public static String rawToJson(String response, String keyword)
	{
		JsonPath jp = new JsonPath(response);	
		return jp.getString(keyword);
	}

}
