package com.momsdeli.online.service.impl;

import com.momsdeli.online.dto.*;
import com.momsdeli.online.model.*;
import com.momsdeli.online.repository.ProductRepository;
import com.momsdeli.online.repository.CustomerRepository;
import com.momsdeli.online.service.CheckoutService;
import com.momsdeli.online.service.SMSService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final JavaMailSender emailSender;
    private final SMSService smsService;
    private static int orderCounter = 1;

    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               ProductRepository productRepository,
                               JavaMailSender emailSender,
                               SMSService smsService) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.emailSender = emailSender;
        this.smsService = smsService;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        Order order = purchase.getOrder();
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        Set<OrderItem> orderItems = purchase.getOrderItems();
        for (OrderItem item : orderItems) {
            order.add(item);
        }

        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());
        Customer customer = purchase.getCustomer();
        customer.add(order);
        customerRepository.save(customer);

        // Send email confirmation
        sendOrderConfirmationEmail(customer.getEmail(), orderTrackingNumber, order);
        // Send SMS
        String smsMessage = formatSMSMessage(order);
        smsService.sendSMS(new SMSRequest(customer.getPhoneNumber(), smsMessage));

        return new PurchaseResponse(orderTrackingNumber);


    }

    private String generateOrderTrackingNumber() {

        long timestamp = System.currentTimeMillis();
        String randomDigits = String.format("%o4d", new Random().nextInt(10000));
        String sequentialPart = String.format("%04d", orderCounter++);
        return "ORD" + timestamp + sequentialPart + randomDigits;

    }


    private void sendOrderConfirmationEmail(String emailAddress, String orderTrackingNumber, Order order) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("momsdeli828@gmail.com");
            helper.setTo(emailAddress);
            helper.setSubject("Order Confirmation");

            StringBuilder emailContent = new StringBuilder("Thank you for your order! Your order with tracking number " + orderTrackingNumber + " has been placed successfully.\n\n");
            emailContent.append("Order Details:\n");
            for (OrderItem item : order.getOrderItems()) {
                //Product product = item.getProduct();
                Product product = productRepository.findById(item.getProductId()).orElse(null);
                assert product != null;
                BigDecimal productPrice = product.getPrice(); // Fetching price from Product
                String priceString = (productPrice != null) ? productPrice.toString() : "N/A";

                emailContent.append("Product: ")
                        .append(product.getName())
                        .append(", Quantity: ")
                        .append(item.getQuantity())
                        .append(", Price: ")
                        .append(priceString)
                        .append("\n");

                // Check and append additional items
                Set<AdditionalItem> additionalItems = product.getAdditionalItems(); // Assuming getAdditionalItems() method exists
                if (additionalItems != null && !additionalItems.isEmpty()) {
                    emailContent.append("Additional Items: ");
                    for (AdditionalItem additionalItem : additionalItems) {
                        emailContent.append(additionalItem.getName())
                                .append(", ");
                    }
                    // Remove the last comma and space
                    emailContent.setLength(emailContent.length() - 2);
                    emailContent.append("\n");
                }
            }
            emailContent.append("Total Price: ").append(order.getTotalPrice().toString());

            helper.setText(emailContent.toString(), false); // false for plain text email
            emailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send order confirmation email to: " + emailAddress, e);
        }
    }


    private String formatSMSMessage(Order order) {
        StringBuilder smsMessage = new StringBuilder("Receipt:\n");
        smsMessage.append("Order #: ").append(order.getOrderTrackingNumber()).append("\n");
        Address shippingAddress = order.getShippingAddress();

        if (shippingAddress != null) {
            smsMessage.append("Address: ");

            // Validate and append street
            if (StringUtils.isNotBlank(shippingAddress.getStreet())) {
                smsMessage.append("Street: ").append(shippingAddress.getStreet()).append(", ");
            }

            // Validate and append city
            if (StringUtils.isNotBlank(shippingAddress.getCity())) {
                smsMessage.append("City: ").append(shippingAddress.getCity()).append(", ");
            }

            // Validate and append state
            if (StringUtils.isNotBlank(shippingAddress.getState())) {
                smsMessage.append("State: ").append(shippingAddress.getState()).append(", ");
            }

            // Validate and append country
            if (StringUtils.isNotBlank(shippingAddress.getCountry())) {
                smsMessage.append("Country: ").append(shippingAddress.getCountry()).append(", ");
            }

            // Validate and append zip code
            if (StringUtils.isNotBlank(shippingAddress.getZipCode())) {
                smsMessage.append("Zip Code: ").append(shippingAddress.getZipCode());
            }

            smsMessage.append("\n");
        }

        smsMessage.append("Phone Number: ").append(order.getCustomer().getPhoneNumber()).append("\n");
        smsMessage.append("Products:\n");

        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                log.info("Product: " + product.getName());

                BigDecimal productPrice = product.getPrice(); // Fetching price from Product
                String priceString = (productPrice != null) ? productPrice.toString() : "N/A";

                smsMessage.append("Product: ")
                        .append(product.getName())
                        .append(", Quantity: ")
                        .append(item.getQuantity())
                        .append(", Product Price: ")
                        .append(priceString);

                // Append selected additional items
                Set<AdditionalItem> additionalItems = product.getAdditionalItems();
                if (additionalItems != null && !additionalItems.isEmpty()) {
                    StringBuilder additionalItemsStr = new StringBuilder();
                    log.info("Additional Items: " + product.getAdditionalItems());
                    for (AdditionalItem additionalItem : additionalItems) {
                        if (additionalItem.isSelected()) { // Check if the additional item is selected
                            additionalItemsStr.append(additionalItem.getName()).append(", ");
                        }
                    }
                    // Append and format the additional items string
                    if (additionalItemsStr.length() > 0) {
                        additionalItemsStr.setLength(additionalItemsStr.length() - 2); // Remove the last comma and space
                        smsMessage.append(", Additional Items: ").append(additionalItemsStr);
                    }
                }
                smsMessage.append("\n");
            }
        }

        log.info("sms sent response {} :", smsMessage);

        smsMessage.append("Total Price: ").append(order.getTotalPrice().toString());
        return smsMessage.toString();
    }


    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        // Ensure that the amount is greater than or equal to 1
        if (paymentInfo.getAmount() < 1) {
            throw new IllegalArgumentException("Amount must be greater than or equal to 1");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", Collections.singletonList("card"));
        params.put("description", "Mom's Deli Purchase");

        // Create a PaymentIntent with the specified parameters
        return PaymentIntent.create(params);
    }
}