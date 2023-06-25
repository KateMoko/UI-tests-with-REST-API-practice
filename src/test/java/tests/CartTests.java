package tests;

import models.AddProductToCartResponse;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import static api.AuthorizationApi.authCookieKey;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.RequestSpec.requestSpec;

public class CartTests extends TestBase {

    String authCookieValue;
    int initialCartItemsQuantity;
    int quantityOfItemsToAdd = 2;

    String data = "product_attribute_72_5_18=52" +
            "&product_attribute_72_6_19=54" +
            "&product_attribute_72_3_20=58" +
            "&addtocart_72.EnteredQuantity=" + quantityOfItemsToAdd;

    @Test
    void addItemToCartAsAuthorizedTest() {
        step("Get authorization cookie by api", () -> {
            authCookieValue = authApi.getAuthCookie(login, password);
        });

        step("Get actual cart size", () -> {
            String shopPage = given()
                    .cookie(authCookieKey, authCookieValue)
                    .when()
                    .get()
                    .then()
                    .statusCode(200)
                    .extract()
                    .asString();

            String cartItemsQuantityText = Jsoup.parse(shopPage).select(".cart-qty").text();
            initialCartItemsQuantity = Integer.parseInt(cartItemsQuantityText.replaceAll("[()]", ""));
        });

        AddProductToCartResponse response = step("Add products to cart", () ->
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie(authCookieKey, authCookieValue)
                        .body(data)
                        .when()
                        .post("/addproducttocart/details/72/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(AddProductToCartResponse.class));

        step("Check response values ", () -> {
            assertEquals("true", response.getSuccess());
            assertEquals("The product has been added to your <a href=\"/cart\">shopping cart</a>", response.getMessage());
            assertEquals(String.format("(%s)", (initialCartItemsQuantity + quantityOfItemsToAdd)),
                    response.getTopCartTotalItems());
        });
    }

    @Test
    void addItemToCartAsAnonymousTest() {
        AddProductToCartResponse response = step("Add products to cart as anonymous", () ->
                given(requestSpec)
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body(data)
                        .when()
                        .post("/addproducttocart/details/72/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(AddProductToCartResponse.class));

        step("Check response values ", () -> {
            assertEquals("true", response.getSuccess());
            assertEquals("The product has been added to your <a href=\"/cart\">shopping cart</a>", response.getMessage());
            assertEquals(String.format("(%s)", quantityOfItemsToAdd), response.getTopCartTotalItems());
        });
    }
}