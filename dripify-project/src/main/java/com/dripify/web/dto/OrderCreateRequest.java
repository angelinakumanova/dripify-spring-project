package com.dripify.web.dto;

import com.dripify.order.model.City;
import com.dripify.order.model.Country;
import com.dripify.order.model.OrderDeliveryCourier;
import com.dripify.order.model.OrderPayment;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class OrderCreateRequest {

    @NotNull(message = "Full name must NOT be null!")
    @NotBlank(message = "Full name is required!")
    private String purchaserFullName;

    @NotNull(message = "Address must NOT be null!")
    @NotBlank(message = "Address is required!!")
    private String purchaserAddress;

    @NotNull(message = "Country is required!")
    private Country country;

    @NotNull(message = "City is required!!")
    private City city;

    @NotNull(message = "Phone number must NOT be null!")
    @Pattern(regexp = "\\d{3}\\s+\\d{3}\\s+\\d{3}", message = "Enter valid a phone number!")
    private String purchaserPhoneNumber;

    @NotNull(message = "Payment type is required!")
    private OrderPayment orderPayment;

    @NotNull(message = "Delivery courier is required!")
    private OrderDeliveryCourier orderDeliveryCourier;
}
