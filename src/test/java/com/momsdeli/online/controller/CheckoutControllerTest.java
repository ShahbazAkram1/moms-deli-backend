/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 9/30/2023$
 * Time: 2:08 PM$
 * Project Name: moms-deli$
 */

package com.momsdeli.online.controller;

import com.momsdeli.online.dto.PaymentInfo;
import com.momsdeli.online.dto.Purchase;
import com.momsdeli.online.dto.PurchaseResponse;
import com.momsdeli.online.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class CheckoutControllerTest {

    @Mock
    private CheckoutService checkoutService;

    @InjectMocks
    private CheckoutController checkoutController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test placing an order - Successful Scenario")
    public void testPlaceOrder() {

        Purchase purchase = new Purchase();
        PurchaseResponse expectedResponse = new PurchaseResponse("CN128928388");
        when(checkoutService.placeOrder(purchase)).thenReturn(expectedResponse);
        PurchaseResponse actualResponse = checkoutController.placeOrder(purchase);
        assertEquals(expectedResponse, actualResponse);
        verify(checkoutService, times(1)).placeOrder(purchase);

    }

    @Test
    @DisplayName("Test placing an order - Failed Order Placement")
    public void testPlaceOrderFailed() {

        Purchase purchase = new Purchase();
        when(checkoutService.placeOrder(purchase)).thenThrow(new RuntimeException("Failed to place order"));
        assertThrows(RuntimeException.class, () -> checkoutController.placeOrder(purchase));
        verify(checkoutService, times(1)).placeOrder(purchase);

    }

    @Test
    @DisplayName("Test creating a payment intent - Successful Scenario")
    public void testCreatedPaymentIntent() throws StripeException {

        PaymentInfo paymentInfo = new PaymentInfo();
        PaymentIntent paymentIntent = new PaymentIntent();

        String paymentStr = paymentIntent.toJson();

        when(checkoutService.createPaymentIntent(paymentInfo)).thenReturn(paymentIntent);
        ResponseEntity<String> responseEntity = checkoutController.createPaymentIntent(paymentInfo);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(paymentStr, responseEntity.getBody());

        verify(checkoutService, times(1)).createPaymentIntent(paymentInfo);

    }

    @Test
    @DisplayName("Test creating a payment intent - Failed Payment Processing")
    public void testCreatePaymentIntentFailed() throws StripeException {

        PaymentInfo paymentInfo = new PaymentInfo();
        when(checkoutService.createPaymentIntent(paymentInfo)).thenThrow(new RuntimeException("Payment Faild"));
        assertThrows(RuntimeException.class, () -> checkoutController.createPaymentIntent(paymentInfo));
        verify(checkoutService, times(1)).createPaymentIntent(paymentInfo);

    }
}
