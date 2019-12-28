package com.example.ordersapi.product.controller;

import com.example.ordersapi.product.api.ProductAPI;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllProducts_should_return_found_products() throws Exception {
        // given
        Product product1 = new Product();
        product1.setId(1);
        Product product2 = new Product();
        product2.setId(2);

        Set<Product> expectedProducts = new HashSet<>();
        expectedProducts.add(product1);
        expectedProducts.add(product2);

        String expectedProductsJson = jsonMapper.writeValueAsString(expectedProducts);

        // when
        when(productService.getAllProducts()).thenReturn(expectedProducts);

        // then
        mockMvc.perform(get(ProductAPI.BASE_URL))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(expectedProductsJson));
    }

    @Test
    void getProductById_should_return_found_product() throws Exception {
        // given
        int id = 1;
        Product expectedProduct = new Product();
        expectedProduct.setId(id);

        String expectedProductJson = jsonMapper.writeValueAsString(expectedProduct);

        // when
        when(productService.getOneProduct(id)).thenReturn(expectedProduct);

        // then
        mockMvc.perform(get(ProductAPI.BASE_URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedProductJson));
    }

    @Test
    void getProductById_should_throw_when_there_are_no_products() throws Exception {
        // given
        int id = 1;

        // when
        when(productService.getOneProduct(any())).thenThrow(new ProductNotFoundException(id));

        // then
        mockMvc.perform(get(ProductAPI.BASE_URL + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductById_should_return_bad_request_when_id_is_invalid() throws Exception {
        // given
        String invalidId = "Id should be an Integer and thus this String will not be parsed :D";

        // when

        // then
        mockMvc.perform(get(ProductAPI.BASE_URL + "/" + invalidId))
                .andExpect(status().isBadRequest());
    }

}