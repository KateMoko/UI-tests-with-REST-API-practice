package api;

import static io.restassured.RestAssured.given;
import static specs.RequestSpec.requestSpec;

public class AuthorizationApi {

    public static String authCookieKey = "NOPCOMMERCE.AUTH";

    public String getAuthCookie(String login, String password) {
        return given(requestSpec)
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", login)
                .formParam("Password", password)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie(authCookieKey);
    }
}