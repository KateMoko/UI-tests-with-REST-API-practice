package tests;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileTests extends TestBase {

    @Test
    void addNewAddressTest(){
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
    }
}
