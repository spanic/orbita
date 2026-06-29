package com.bmstu_bureau_1440.orders.controller;

import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(InstancioExtension.class)
@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    OrderRepository orderRepository;

    @Test
    void getOrders_returnsAllOrdersAsJson() throws Exception {
        List<Order> orders = Instancio.ofList(Order.class).size(2).create();
        when(orderRepository.findAll()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(orders.get(0).getId().toString()))
                .andExpect(jsonPath("$[1].id").value(orders.get(1).getId().toString()));
    }

    @Test
    void createOrder_returnsCreatedWithSavedOrder() throws Exception {
        Order saved = Instancio.create(Order.class);
        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        mockMvc.perform(post("/orders"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(saved.getId().toString()))
                .andExpect(jsonPath("$.createdAt").exists());
    }

}
