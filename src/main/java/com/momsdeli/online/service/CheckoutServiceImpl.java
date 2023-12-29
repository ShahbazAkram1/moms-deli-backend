package com.momsdeli.online.service;

import com.momsdeli.online.dto.PaymentInfo;
import com.momsdeli.online.dto.SMSRequest;
import com.momsdeli.online.model.Address;
import com.momsdeli.online.repository.CustomerRepository;
import com.momsdeli.online.dto.Purchase;
import com.momsdeli.online.dto.PurchaseResponse;
import com.momsdeli.online.model.Customer;
import com.momsdeli.online.model.Order;
import com.momsdeli.online.model.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;

    private final JavaMailSender emailSender;

    private final SMSService smsService;


    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               @Value("${stripe.key.secret}") String secretKey, JavaMailSender emailSender, SMSService smsService) {

        this.customerRepository = customerRepository;
        this.emailSender = emailSender;
        this.smsService = smsService;

        // initialize Stripe API with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(order::add);

        // populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // populate customer with order
        Customer customer = purchase.getCustomer();
        customer.add(order);

        // save to the database
        customerRepository.save(customer);

        sendOrderConfirmationEmail(customer.getEmail(), order);

        String messageTemplate =
                "Dear " + customer.getFirstName() + " " + customer.getLastName() +
                        ", Thank you for shopping with us! Your order #" + order.getId() + " at Momsdeli has been shipped. Your tracking number is: "
                        + order.getOrderTrackingNumber();

        SMSRequest request = new SMSRequest();
        request.setPhoneNumber(customer.getPhoneNumber());
        request.setMessage(messageTemplate);
        smsService.sendSMS(request);
        log.info("SMS sent successfully to: {}", customer.getPhoneNumber());

        String businessOwnerMessage = buildBusinessOwnerNotification(order);
        SMSRequest ownerRequest = new SMSRequest();
        ownerRequest.setPhoneNumber("+032172712636"); // Replace with owner's phone number
        ownerRequest.setMessage(businessOwnerMessage);
        smsService.sendSMS(ownerRequest);
        log.info("SMS sent successfully to business owner with order details");
        log.info("Business Owner Details:\n{}", businessOwnerMessage); // Log business owner details

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Mom's Deli Purchase");

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {

        return UUID.randomUUID().toString();
    }

    private void sendOrderConfirmationEmail1(String emailAddress, String orderTrackingNumber) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("momsdeli828@gmail.com");
            helper.setTo(emailAddress);
            helper.setSubject("Order Confirmation");
            helper.setText("Thank you for your order! Your order with tracking number "
                    + orderTrackingNumber + " has been placed successfully.");

            emailSender.send(message);
            log.info("Order confirmation email sent to: {}", emailAddress);

        } catch (Exception e) {
            log.error("Failed to send order confirmation email to: {}", emailAddress, e);
        }
    }

    private void sendOrderConfirmationEmail(String emailAddress, Order order) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("momsdeli828@gmail.com");
            helper.setTo(emailAddress);
            helper.setSubject("Order Confirmation - #" + order.getOrderTrackingNumber());

            // Assuming you have a method to load the HTML template
            String htmlContent = loadHtmlTemplate(order);
            helper.setText(htmlContent, true); // Set the second parameter to 'true' to enable HTML format

            emailSender.send(message);
            log.info("Order confirmation email sent to: {}", emailAddress);
        } catch (Exception e) {
            log.error("Failed to send order confirmation email to: {}", emailAddress, e);
        }
    }

    private String loadHtmlTemplate(Order order) throws IOException {
        try {
            // Load HTML template from the resources
            ClassPathResource resource = new ClassPathResource("templates/order-confirmation-template.html");
            try (InputStream inputStream = resource.getInputStream()) {
                String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                // Replace placeholders with actual order details
                String customerName = order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName();
                htmlTemplate = htmlTemplate.replace("[CustomerName]", customerName);
                htmlTemplate = htmlTemplate.replace("[OrderNumber]", order.getOrderTrackingNumber());

                // Construct the items details HTML
                StringBuilder itemsHtml = new StringBuilder();
                for (OrderItem item : order.getOrderItems()) {
                    itemsHtml.append("<div class=\"item\">")
                            .append("<img src=\"").append(item.getImageUrl()).append("\" alt=\"Product\">")
                            .append("<p>").append(item.getProductName()).append(" - $").append(item.getUnitPrice()).append("</p>")
                            .append("<p>Quantity: ").append(item.getQuantity()).append("</p>")
                            .append("</div>");
                }

                htmlTemplate = htmlTemplate.replace("[ItemsDetails]", itemsHtml.toString());
                htmlTemplate = htmlTemplate.replace("[Total]", String.valueOf(order.getTotalPrice()));
                // Ensure formatAddress method returns a String representation of the address
                String formattedShippingAddress = formatAddress(order.getShippingAddress());
                String formattedBillingAddress = formatAddress(order.getBillingAddress());

                htmlTemplate = htmlTemplate.replace("[ShippingAddress]", formattedShippingAddress);
                htmlTemplate = htmlTemplate.replace("[BillingAddress]", formattedBillingAddress);
                htmlTemplate = htmlTemplate.replace("[Total]", order.getTotalPrice().toString());
                // ... add more replacements as needed for shipping, subtotal, addresses etc.

                return htmlTemplate;
            }
        } catch (Exception e) {
            log.error("Failed to load email template", e);
            return ""; // Or handle the exception as needed
        }
    }


    private String buildBusinessOwnerNotification(Order order) {
        StringBuilder message = new StringBuilder();
        message.append("New Order Placed\n")
                .append("Order ID: ").append(order.getId()).append("\n")
                .append("Shipping Address: ").append(formatAddress(order.getShippingAddress())).append("\n")
                .append("Delivery Address: ").append(formatAddress(order.getBillingAddress())).append("\n")
                .append("Total Quantity: ").append(order.getTotalQuantity()).append("\n")
                .append("Total Price: ").append(order.getTotalPrice()).append("\n");

        for (OrderItem orderItem : order.getOrderItems()) {
            message.append("Product Name: ").append(orderItem.getProductName()).append("\n")
                    .append("Quantity: ").append(orderItem.getQuantity()).append("\n\n");
            // Include more item details if needed
        }

        // Add more order details as needed

        return message.toString();
    }

    private String formatAddress(Address address) {
//        return address.getStreet() + ", " + address.getCity() + ", " + address.getZipCode() + ", " + address.getCountry();
        return Stream.of(address.getStreet(), address.getCity(), address.getState(), address.getZipCode(), address.getCountry())
                .filter(Objects::nonNull)
                .filter(str -> !str.trim().isEmpty())
                .collect(Collectors.joining("<br>"));
    }

}