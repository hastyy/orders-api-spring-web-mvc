package com.example.ordersapi.order.controller;

import com.example.ordersapi.authentication.model.Principal;
import com.example.ordersapi.order.api.OrderAPI;
import com.example.ordersapi.order.api.dto.OrderDto;
import com.example.ordersapi.order.api.dto.OrderEntryDto;
import com.example.ordersapi.order.service.OrderService;
import com.example.ordersapi.testutils.SecurityEnabledTest;
import com.example.ordersapi.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@Import({Principal.class, User.class})
class OrderControllerTest extends SecurityEnabledTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @Disabled
    @Test
    @WithMockUser
    void create_should_return_order_and_created_when_user_is_authenticated_and_request_is_valid() throws Exception {
        // given
        OrderEntryDto orderEntryDto = new OrderEntryDto();
        orderEntryDto.setProduct("Product 1");
        orderEntryDto.setQuantity(3);

        OrderDto orderDto = new OrderDto();
        orderDto.setProducts(List.of(orderEntryDto));

        // when
        when(orderService.createOrder(any(), orderDto)).thenReturn(null);

        // then
        MvcResult response = mockMvc.perform(post(OrderAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(response);
    }

}