package com.momsdeli.online.service.impl;

import com.momsdeli.online.dto.*;
import com.momsdeli.online.model.*;
import com.momsdeli.online.repository.ProductRepository;
import com.momsdeli.online.repository.CustomerRepository;
import com.momsdeli.online.service.CheckoutService;
import com.momsdeli.online.service.EmailService;
import com.momsdeli.online.service.SMSService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final JavaMailSender emailSender;
    private final SMSService smsService;

    private final EmailService emailService;
    private static int orderCounter = 1;

    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               ProductRepository productRepository,
                               JavaMailSender emailSender,
                               SMSService smsService, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.emailSender = emailSender;
        this.smsService = smsService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        log.info("Received purchase request: {}", purchase);
        Order order = purchase.getOrder();

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<html><head><style>");
        emailContent.append(".card {");
        emailContent.append("   box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);");
        emailContent.append("   transition: 0.3s;");
        emailContent.append("   border-radius: 5px; /* 5px rounded corners */");
        emailContent.append("   margin: 10px; /* Add some margin */");
        emailContent.append("}");
        emailContent.append(".container {");
        emailContent.append("   padding: 2px 16px;");
        emailContent.append("}");
        emailContent.append("</style></head><body>");
// Mom's Deli logo
        emailContent.append("<center><img src='https://momsdelionline.com/assets/images/moms-deli-logo.gif' alt='Mom's Deli Logo' style='max-width: 200px;'><br><br>");
        emailContent.append("<p>Hello ").append(purchase.getCustomer().getFirstName()).append(",</p>");
        emailContent.append("<p>Thank you for your order. Below is your order confirmation and invoice:</p>");
// Include customer's name and shipping address
        emailContent.append("<p>Customer Name: ").append(purchase.getCustomer().getFirstName()).append(" ").append(purchase.getCustomer().getLastName()).append("</p>");

        // Retrieve the shipping address from the Purchase object
        Address shippingAddress = purchase.getShippingAddress();
        // Check if the shipping address is not null
        if (shippingAddress != null) {
            // Append a formatted shipping address to the email content
            emailContent.append("<p>Shipping Address:</p>");
            emailContent.append("<p>").append(shippingAddress.getStreet()).append("</p>");
            emailContent.append("<p>").append(shippingAddress.getCity()).append(", ").append(shippingAddress.getState()).append(", ").append(shippingAddress.getCountry()).append(", ").append(shippingAddress.getZipCode()).append("</p>");
        } else {
            // If the shipping address is null, append a message indicating it's not available
            emailContent.append("<p>Shipping Address: Not Available</p>");
        }
        emailContent.append("<div class='card' style='box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);transition: 0.3s;border-radius: 5px; margin: 10px;'><div class='container'>");
        emailContent.append("<table style='width: 100%; border-collapse: collapse;' border='1'>");
        emailContent.append("<tr><th>S.No</th><th>Product Image </th><th>Product</th><th>Price</th><th>Quantity</th><th>Additional Items</th><th>Selected Bread</th></tr>");

// Iterate over order items
        Set<OrderItem> orderItems = purchase.getOrderItems();
        int i = 0;
        for (OrderItem item : orderItems) {
            i += 1;
            emailContent.append("<tr>");
            emailContent.append("<td>").append(i).append("</td>").append("</td>");
            emailContent.append("<td><img src='https://momsdelionline.com/").append(item.getImageUrl()).append("' alt='Product Image' style='max-width: 100px; max-height: 100px;'></td>");
            emailContent.append("<td>").append(item.getProduct().getName()).append("</td>");
            emailContent.append("<td>").append(item.getProduct().getPrice()).append("</td>");
            emailContent.append("<td>").append(item.getQuantity()).append("</td>");

            emailContent.append("<td>");
            for (AdditionalItem additionalItem : item.getSelectedAdditionalItems()) {
                emailContent.append(additionalItem.getName()).append("<br>");
            }
            emailContent.append("</td>");

            emailContent.append("<td>");
            for (BreadOption breadOption : item.getProduct().getBreadOptions()) {
                emailContent.append(breadOption.getName()).append("<br>");
            }
            emailContent.append("</td>");

            emailContent.append("</tr>");
        }
        emailContent.append("<tr><th>Total Price</th>").append("<th>").append(order.getTotalPrice()).append("</th>");
        emailContent.append("</table></div></div>");
        emailContent.append("Total Pay : = $").append(order.getTotalPrice());

// Close HTML body and document
        emailContent.append("</body></html>");

// Now you can use emailContent.toString() to get the complete HTML content for your email.

// Footer
        emailContent.append("<p>For any inquiries or assistance, please contact us at <a href='mailto:momsdeli828@gmail.com'>momsdeli828@gmail.com</a>.</p>");
        emailContent.append("<center><p>Best Regards,<br>Your Mom's Deli Team</p></center");
        emailContent.append("</body></html>");


        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());
        order.setOrderItems(orderItems);
        Customer customer = purchase.getCustomer();
        customer.add(order);
        customerRepository.save(customer);


        // Log the data received from the frontend
        log.info("Received order placement request from customer: {}", customer.getEmail());
        log.info("Order Tracking Number: {}", order.getOrderTrackingNumber());
        log.info("Order Items: {}", orderItems);
        log.info("Billing Address: {}", purchase.getBillingAddress());
        log.info("Shipping Address: {}", purchase.getShippingAddress());
        log.info("Customer Details: {}", customer);

        // Log image URLs
        for (OrderItem item : orderItems) {
            log.info("Product Image URL: {}", item.getImageUrl());
        }

        // Send email confirmation
//        sendOrderConfirmationEmail(customer.getEmail(), orderTrackingNumber, order);

        try {

            // Create MimeMessage and set content
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("momsdeli828@gmail.com");
            helper.setTo(customer.getEmail());
            helper.setSubject("Order Confirmation");
            helper.setText(emailContent.toString(), true);

            // Send email
            emailSender.send(message);
        } catch (Exception ee) {

        }
        // Send SMS
        String smsMessage = formatSMSMessage(order);
        smsService.sendSMS(new SMSRequest(customer.getPhoneNumber(), smsMessage));

        return new PurchaseResponse(order.getOrderTrackingNumber());
    }

    private String generateOrderTrackingNumber() {

        long timestamp = System.currentTimeMillis();
        String randomDigits = String.format("%o4d", new Random().nextInt(10000));
        String sequentialPart = String.format("%04d", orderCounter++);
        return "ORD" + timestamp + sequentialPart + randomDigits;

    }

    public void sendOrderConfirmationEmail(String emailAddress, String orderTrackingNumber, Order order) {
        try {
            String emailTemplate = emailService.loadEmailTemplate();
            String emailContent = populateEmailTemplate(emailTemplate, order.getCustomer().getFirstName(), orderTrackingNumber, generateOrderItemsList(order), order.getTotalPrice());

            // Create MimeMessage and set content
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("momsdeli828@gmail.com");
            helper.setTo(emailAddress);
            helper.setSubject("Order Confirmation");
            helper.setText(emailContent, true);

            // Send email
            emailSender.send(message);

            log.info("Order confirmation email sent successfully to: {}", emailAddress);
        } catch (Exception e) {
            // Log the exception
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

        Set<AdditionalItem> additionalItems = new HashSet<>();
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                additionalItems.addAll(product.getAdditionalItems());
            }
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                BigDecimal productPrice = product.getPrice();
                String priceString = (productPrice != null) ? productPrice.toString() : "N/A";

                smsMessage.append("Product: ")
                        .append(product.getName())
                        .append(", Quantity: ")
                        .append(item.getQuantity())
                        .append(", Product Price: ")
                        .append(priceString);

                // Append selected additional items
                if (!additionalItems.isEmpty()) {
                    StringBuilder additionalItemsStr = new StringBuilder();
                    for (AdditionalItem additionalItem : additionalItems) {
                        if (additionalItem.isSelected()) {
                            additionalItemsStr.append(additionalItem.getName()).append(", ");
                        }
                    }
                    if (additionalItemsStr.length() > 0) {
                        additionalItemsStr.setLength(additionalItemsStr.length() - 2);
                        smsMessage.append(", Additional Items: ").append(additionalItemsStr);
                    }
                }
                smsMessage.append("\n");
            }
        }

        smsMessage.append("Total Price: ").append(order.getTotalPrice().toString());
        return smsMessage.toString();
    }


    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        // Ensure that the amount is greater than or equal to 1
        System.out.println("Total Amount.." + paymentInfo.getAmount());
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

    private String populateEmailTemplate(String template, String customerName, String orderTrackingNumber, String orderItems, BigDecimal totalPrice) {
        // Populate template with actual values
        return template.replace("[Customer Name]", customerName)
                .replace("[Order Tracking Number]", orderTrackingNumber)
                .replace("[Order Items]", orderItems)
                .replace("[Total Price]", totalPrice.toString());
    }

    private String generateOrderItemsList(Order order) {
        StringBuilder orderItemsList = new StringBuilder();
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                BigDecimal productPrice = product.getPrice();
                String priceString = (productPrice != null) ? productPrice.toString() : "N/A";

                // Format each order item on a new line
                orderItemsList.append("Product: ")
                        .append(product.getName())
                        .append(", Quantity: ")
                        .append(item.getQuantity())
                        .append(", Product Price: ")
                        .append(priceString)
                        .append("\n");
            }
        }
        return orderItemsList.toString();
    }

}