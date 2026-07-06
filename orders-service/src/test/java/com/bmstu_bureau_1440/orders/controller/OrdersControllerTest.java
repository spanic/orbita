package com.bmstu_bureau_1440.orders.controller;

import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.ARCHIVE_ORDER_MODEL;
import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.MONITORING_ORDER_MODEL;
import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.TASKING_ORDER_MODEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

import com.bmstu_bureau_1440.orders.error.OrderNotFoundException;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.service.OrderService;

@ExtendWith(InstancioExtension.class)
@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @Autowired
    MockMvcTester mvcTester;

    @MockitoBean
    OrderService orderService;

    @Test
    void getOrders_returnsAllOrdersAsJson() throws Exception {
        List<Order> orders = Arrays.asList(
                Instancio.create(ARCHIVE_ORDER_MODEL),
                Instancio.create(TASKING_ORDER_MODEL),
                Instancio.create(MONITORING_ORDER_MODEL));

        when(orderService.findAll()).thenReturn(orders);

        assertThat(mvcTester.perform(get("/orders")))
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(Order[].class)
                .satisfies(array -> assertThat(array)
                        .hasSameSizeAs(orders)
                        .containsExactlyElementsOf(orders));
    }

    @Test
    void getOrder_returnsOrderAsJson_whenOrderExists() throws Exception {
        UUID orderId = UUID.randomUUID();
        Order order = Instancio.create(ARCHIVE_ORDER_MODEL);

        when(orderService.findById(orderId)).thenReturn(order);

        assertThat(mvcTester.perform(get("/orders/{order_id}", orderId)))
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(Order.class)
                .isEqualTo(order);
    }

    @Test
    void getOrder_returnsNotFound_whenOrderDoesNotExist() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderService.findById(orderId)).thenThrow(new OrderNotFoundException());

        assertThat(mvcTester.perform(get("/orders/{order_id}", orderId)))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

}
