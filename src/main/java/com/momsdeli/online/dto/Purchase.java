package com.momsdeli.online.dto;

import com.momsdeli.online.model.Address;
import com.momsdeli.online.model.Customer;
import com.momsdeli.online.model.Order;
import com.momsdeli.online.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}