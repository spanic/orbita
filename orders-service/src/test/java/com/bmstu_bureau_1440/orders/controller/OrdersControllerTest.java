package com.bmstu_bureau_1440.orders.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;

import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;

@ExtendWith(InstancioExtension.class)
@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @Autowired
    MockMvcTester mvcTester;

    @MockitoBean
    OrderRepository orderRepository;

    @Test
    void getOrders_returnsAllOrdersAsJson() throws Exception {
        List<Order> orders = Instancio.ofList(Order.class).size(2).create();

        when(orderRepository.findAll()).thenReturn(orders);

        assertThat(mvcTester.perform(get("/orders")))
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(Order[].class)
                .satisfies(array -> assertThat(array)
                        .hasSameSizeAs(orders)
                        .extracting(Order::getId)
                        .containsExactly(orders.get(0).getId(), orders.get(1).getId()));
    }

    @Test
    void createOrder_returnsCreatedWithSavedOrder() throws Exception {
        Order order = Instancio.create(Order.class);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        assertThat(mvcTester.perform(post("/orders")))
                .hasStatus(HttpStatus.CREATED)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(Order.class)
                .returns(order.getId(), Order::getId)
                .returns(order.getCreatedAt(), Order::getCreatedAt);
    }

}
