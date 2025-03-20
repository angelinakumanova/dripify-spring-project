package com.dripify.web.dto;

import com.dripify.order.model.OrderDeliveryCourier;
import com.dripify.order.model.OrderPayment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCreateRequest {

    @NotNull(message = "Full name must NOT be null!")
    @NotBlank(message = "Full name must NOT be empty!")
    private String purchaserFullName;

    @NotNull(message = "Address must NOT be null!")
    @NotBlank(message = "Address must NOT be empty!")
    private String purchaserAddress;

    @NotNull(message = "Phone number must NOT be null!")
    @Size(min = 9, max = 9, message = "Enter valid a phone number!")
    private String purchaserPhoneNumber;

    @NotNull(message = "Payment type must NOT be null!")
    private OrderPayment orderPayment;

    @NotNull(message = "Delivery courier must NOT be null!")
    private OrderDeliveryCourier orderDeliveryCourier;

    @NotEmpty(message = "Order's products must NOT be null or empty!")
    @Valid
    private List<OrderItemCreateRequest> products = new ArrayList<>();
}
