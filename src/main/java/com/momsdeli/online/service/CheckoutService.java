package com.momsdeli.online.service;

import com.momsdeli.online.dto.PaymentInfo;
import com.momsdeli.online.dto.Purchase;
import com.momsdeli.online.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {
    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;

}