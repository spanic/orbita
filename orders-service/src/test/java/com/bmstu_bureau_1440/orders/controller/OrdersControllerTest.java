package com.bmstu_bureau_1440.orders.controller;

import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.ARCHIVE_ORDER_MODEL;
import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.ARCHIVE_ORDER_REQUEST_MODEL;
import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.MONITORING_ORDER_MODEL;
import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.TASKING_ORDER_MODEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

import com.bmstu_bureau_1440.orders.dto.CreateOrderRequest;
import com.bmstu_bureau_1440.orders.error.OrderNotFoundException;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.service.OrderService;
import com.bmstu_bureau_1440.shared.config.UserIdHeaderProperties;
import com.bmstu_bureau_1440.shared.error.ErrorCodesRegistry;

import tools.jackson.databind.ObjectMapper;

@ExtendWith(InstancioExtension.class)
@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserIdHeaderProperties userIdHeaderProperties;

    @MockitoBean
    OrderService orderService;

    @Test
    void getOrders_returnsAllOrdersAsJson() throws Exception {
        List<Order> orders = Arrays.asList(
                Instancio.create(ARCHIVE_ORDER_MODEL),
                Instancio.create(TASKING_ORDER_MODEL),
                Instancio.create(MONITORING_ORDER_MODEL));

        when(orderService.findAll("test-user-id")).thenReturn(orders);

        assertThat(mvcTester.perform(
                get(OrdersApi.BASE_PATH).header(userIdHeaderProperties.userIdHeader(), "test-user-id")))
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

        when(orderService.findById(orderId, "test-user-id")).thenReturn(order);

        assertThat(mvcTester.perform(get(OrdersApi.BASE_PATH + OrdersApi.ORDER_ID_PATH, orderId)
                .header(userIdHeaderProperties.userIdHeader(), "test-user-id")))
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(Order.class)
                .isEqualTo(order);
    }

    @Test
    void getOrder_returnsNotFound_whenOrderDoesNotExist() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderService.findById(orderId, "test-user-id")).thenThrow(new OrderNotFoundException());

        assertThat(mvcTester.perform(get(OrdersApi.BASE_PATH + OrdersApi.ORDER_ID_PATH, orderId)
                .header(userIdHeaderProperties.userIdHeader(), "test-user-id")))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void getOrder_returnsNotFound_whenOrderBelongsToDifferentUser() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderService.findById(orderId, "other-user-id")).thenThrow(new OrderNotFoundException());

        assertThat(mvcTester.perform(get(OrdersApi.BASE_PATH + OrdersApi.ORDER_ID_PATH, orderId)
                .header(userIdHeaderProperties.userIdHeader(), "other-user-id")))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void createOrder_savesOrderWithRequestingUsersId() throws Exception {
        CreateOrderRequest request = Instancio.create(ARCHIVE_ORDER_REQUEST_MODEL);
        Order order = Instancio.create(ARCHIVE_ORDER_MODEL);

        when(orderService.create(eq("test-user-id"), any(CreateOrderRequest.class))).thenReturn(order);

        assertThat(mvcTester.perform(post(OrdersApi.BASE_PATH)
                .header(userIdHeaderProperties.userIdHeader(), "test-user-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))))
                .hasStatus(HttpStatus.CREATED)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(Order.class)
                .isEqualTo(order);
    }

    @Test
    void getOrders_returnsMissingUserId_whenHeaderAbsent() throws Exception {
        assertThat(mvcTester.perform(get(OrdersApi.BASE_PATH)))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .extractingPath("$.error_code")
                .isEqualTo(ErrorCodesRegistry.MISSING_USER_ID.name());
    }

}
