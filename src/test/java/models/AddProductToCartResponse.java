package models;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddProductToCartResponse {

    private String message;
    private String success;

    @JsonProperty("updatetopcartsectionhtml")
    private String topCartTotalItems;
}