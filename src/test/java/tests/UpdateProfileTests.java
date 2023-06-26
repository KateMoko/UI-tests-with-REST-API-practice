package tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.HashMap;
import java.util.Map;

import static api.AuthorizationApi.authCookieKey;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.RequestSpec.requestSpec;

public class UpdateProfileTests extends TestBase {
    String authCookieValue;

    @Test
    void addNewAddressTest() {
        Map<String, String> addressData = new HashMap<>();
        addressData.put("Address.Id", "0");
        addressData.put("Address.FirstName", faker.name().firstName());
        addressData.put("Address.LastName", faker.name().lastName());
        addressData.put("Address.Email", login);
        addressData.put("Address.Company", faker.company().name());
        addressData.put("Address.CountryId", "1");
        addressData.put("Address.StateProvinceId", "15");
        addressData.put("Address.City", faker.address().city());
        addressData.put("Address.Address1", faker.address().streetAddress());
        addressData.put("Address.Address2", faker.address().secondaryAddress());
        addressData.put("Address.ZipPostalCode", faker.address().zipCode());
        addressData.put("Address.PhoneNumber", faker.phoneNumber().cellPhone());
        addressData.put("Address.FaxNumber", faker.phoneNumber().phoneNumber());

        step("Get authCookieValue", () ->
            authCookieValue = authApi.getAuthCookieValue(login, password));

        step("Add new address by API", () -> {
            given(requestSpec)
                    .contentType("application/x-www-form-urlencoded")
                    .cookie(authCookieKey, authCookieValue)
                    .formParams(addressData)
                    .when()
                    .post("/customer/addressadd")
                    .then()
                    .log().all()
                    .statusCode(302);
        });

        step("Set authorization cookie to browser", () -> {
            open("/Content/jquery-ui-themes/smoothness/images/ui-bg_flat_75_ffffff_40x100.png");
            getWebDriver().manage().addCookie(new Cookie(authCookieKey, authCookieValue));
        });

        step("Open addresses page", () ->
                open("/customer/addresses"));

        step("Check the added address in the addresses list", () -> {
            $$(".address-list .address-item .title")
                    .filterBy(Condition.text(String.format("%s %s", addressData.get("Address.FirstName"),
                            addressData.get("Address.LastName"))))
                    .get(0).shouldBe(visible);
        });
    }
}