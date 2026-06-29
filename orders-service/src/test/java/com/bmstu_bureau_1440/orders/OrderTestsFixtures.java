package com.bmstu_bureau_1440.orders;

import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

import com.bmstu_bureau_1440.orders.model.Order;

public final class OrderTestsFixtures {

    public static final Model<Order> ORDER_MODEL = Instancio.of(Order.class)
            .ignore(field(Order::getId))
            .ignore(field(Order::getCreatedAt))
            .toModel();

}
