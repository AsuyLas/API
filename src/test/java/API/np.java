package API;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
public class np extends baseTest {
    String APIKey = "1d7e21fe2740db2e59e53d8758ae7ecd";

    @Test
    public void validateThatSuccess() {
        Map<String, Object> methodProperties = new HashMap<>();
        Map<String, Object> reqBody = new HashMap<>();

        methodProperties.put("CityName", "Київ");
        methodProperties.put("Warehouse", "1");
        methodProperties.put("Limit", "5");

        reqBody.put("apiKey", APIKey);
        reqBody.put("modelName", "Address");
        reqBody.put("calledMethod", "getSettlements");
        reqBody.put("methodProperties", methodProperties);

        given()
                .contentType(ContentType.JSON)
                .body(reqBody)
                .when()
                .post("https://api.novaposhta.ua/v2.0/json/")
                .then().log().all()
                .body("success", equalTo(true));
    }
    @Test
    public void validateOfficeAvailability() {
        Map<String, Object> methodProperties = new HashMap<>();
        methodProperties.put("Page", 1);
        methodProperties.put("Warehouse", 1);
        methodProperties.put("FindByString", "Київ");
        methodProperties.put("Limit", 20);

        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("apiKey", APIKey);
        reqBody.put("modelName", "Address");
        reqBody.put("calledMethod", "getSettlements");
        reqBody.put("methodProperties", methodProperties);

        Response response = given()
                .header("X-API-KEY", APIKey)
                .auth().basic("apiKey", APIKey)
                .contentType(ContentType.JSON)
                .body(reqBody)
                .when()
                .post("https://api.novaposhta.ua/v2.0/json/")
                .then().log().all()  // Log response details for debugging
                .extract().response();

        List<Map<String, ?>> offices = response.jsonPath().getList("data");

        assertThat("Offices are found", offices.size(), greaterThan(0));
        assertThat("First office has a Description", offices.get(0).get("Description"), notNullValue());
    }

}
