package com.example.ordersapi.product.controller;

import com.example.ordersapi.product.api.ProductAPI;
import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.api.dto.UpdateProductDto;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductAlreadyExistsException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.service.ProductService;
import com.example.ordersapi.testutils.SecurityEnabledTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest extends SecurityEnabledTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Disabled
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
    void getAllProducts_should_return_all_available_products() throws Exception {
        // given
        final int PRODUCT_COUNT = 1000;
        List<Product> products = new ArrayList<>(PRODUCT_COUNT);
        for (int i = 0; i < PRODUCT_COUNT; i++) {
            Product p = new Product();
            p.setId(i+1);

            products.add(p);
        }

        int page = 1;
        int size = Integer.MAX_VALUE;

        // when
        when(productService.getProductsPage(page, size)).thenReturn(products);

        // then
        MvcResult response = mockMvc.perform(get("{baseURL}", ProductAPI.BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBodyJson = response.getResponse().getContentAsString();
        List responseProducts = jsonMapper.readValue(responseBodyJson, List.class);

        assertThat(responseProducts.size(), is(equalTo(products.size())));

        verify(productService, times(1)).getProductsPage(page, size);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void getAllProducts_should_return_a_page_of_products() throws Exception {
        // given
        int page = 5;
        int size = 10;

        // then
        mockMvc.perform(get("{baseURL}", ProductAPI.BASE_URL)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(productService, times(1)).getProductsPage(page, size);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void getAllProducts_should_return_400_when_page_number_is_less_than_one() throws Exception {
        mockMvc.perform(get("{baseURL}", ProductAPI.BASE_URL)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void getAllProducts_should_return_400_when_page_size_is_less_than_one() throws Exception {
        mockMvc.perform(get("{baseURL}", ProductAPI.BASE_URL)
                .param("page", "10")
                .param("size", "0"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
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
        when(productService.getOneProduct(id)).thenThrow(new ProductNotFoundException(id));

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

    @Test
    void createProduct_should_return_created_product_and_201_when_input_is_okay() throws Exception {
        // given
        final String NAME = "Product Name";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        Product expectedProduct = new Product();
        expectedProduct.setName(NAME);
        expectedProduct.setPrice(PRICE);

        // when
        when(productService.createProduct(productDto)).thenReturn(expectedProduct);

        // then
        MvcResult response = mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBodyJson = response.getResponse().getContentAsString();
        Product responseProduct = jsonMapper.readValue(responseBodyJson, Product.class);

        assertThat(responseProduct.getName(), is(equalTo(productDto.getName())));
        assertThat(responseProduct.getPrice(), is(equalTo(productDto.getPrice())));
        assertThat(responseProduct, is(equalTo(expectedProduct)));
    }

    @Test
    void createProduct_should_return_409_when_input_is_okay_but_product_name_already_exists() throws Exception {
        // given
        final String NAME = "Product Name";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        // when
        when(productService.createProduct(any())).thenThrow(new ProductAlreadyExistsException(NAME));

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void createProduct_should_return_400_when_missing_product_name() throws Exception {
        // given
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setPrice(PRICE);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_should_return_400_when_product_name_is_empty() throws Exception {
        // given
        final String NAME = "";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_should_return_400_when_product_name_is_blank() throws Exception {
        // given
        final String NAME = "        ";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_should_return_400_when_product_name_is_above_80_characters() throws Exception {
        // given
        final String NAME = "012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_should_return_400_when_product_price_is_null() throws Exception {
        // given
        final String NAME = "My Product";

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_should_return_400_when_product_price_has_more_than_2_decimal_places() throws Exception {
        // given
        final String NAME = "My Product";
        final BigDecimal PRICE = BigDecimal.valueOf(2.956);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_should_return_400_when_product_price_is_negative_value() throws Exception {
        // given
        final String NAME = "My Product";
        final BigDecimal PRICE = BigDecimal.valueOf(-2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_should_return_400_when_product_image_url_is_not_a_url() throws Exception {
        // given
        final String NAME = "My Product";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);
        final String IMAGE_URL = "this-is-not-a-url.com";

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);
        productDto.setImageUrl(IMAGE_URL);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_should_return_400_when_product_description_is_too_big() throws Exception {
        // given
        final String NAME = "My Product";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);
        final String DESCRIPTION = "I should not be testing this further. I should not be testing this further. " +
                "I should not be testing this further. I should not be testing this further. " +
                "I should not be testing this further. I should not be testing this further. " +
                "I should not be testing this further.";

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);
        productDto.setDescription(DESCRIPTION);

        // when

        // then
        mockMvc.perform(post(ProductAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void updateProduct_should_return_updated_product_and_200_when_product_is_successfully_updated() throws Exception {
        // given
        final Integer ID = 1;
        final String NAME = "My Product";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        UpdateProductDto productDto = new UpdateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        Product updatedProduct = new Product();
        updatedProduct.setId(ID);
        updatedProduct.setName(NAME);
        updatedProduct.setPrice(PRICE);

        // when
        when(productService.updateProduct(ID, productDto)).thenReturn(updatedProduct);

        // then
        MvcResult response = mockMvc.perform(patch("{baseURL}/{id}", ProductAPI.BASE_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBodyJson = response.getResponse().getContentAsString();
        Product responseProduct = jsonMapper.readValue(responseBodyJson, Product.class);

        assertThat(responseProduct.getName(), is(equalTo(productDto.getName())));
        assertThat(responseProduct.getPrice(), is(equalTo(productDto.getPrice())));

        verify(productService, times(1)).updateProduct(ID, productDto);
    }

    @Test
    void updateProduct_should_return_404_when_product_cannot_be_found() throws Exception {
        // given
        final Integer ID = 1;
        final  UpdateProductDto productDto = new UpdateProductDto();

        // when
        when(productService.updateProduct(ID, productDto)).thenThrow(new ProductNotFoundException(ID));

        // then
        mockMvc.perform(patch("{baseURL}/{id}", ProductAPI.BASE_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(ID, productDto);
    }

    @Test
    void updateProduct_should_return_409_when_update_product_name_already_exists() throws Exception {
        // given
        final Integer ID = 1;
        final  UpdateProductDto productDto = new UpdateProductDto();

        // when
        when(productService.updateProduct(ID, productDto)).thenThrow(new ProductAlreadyExistsException("Product Name"));

        // then
        mockMvc.perform(patch("{baseURL}/{id}", ProductAPI.BASE_URL, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(productDto)))
                .andExpect(status().isConflict());

        verify(productService, times(1)).updateProduct(ID, productDto);
    }

    @Test
    void deleteProduct_should_return_deleted_product_and_200_when_product_is_found() throws Exception {
        // given
        final Integer ID = 1;
        final Product product = new Product();
        product.setId(ID);

        // when
        when(productService.deleteProduct(ID)).thenReturn(product);

        // then
        MvcResult response = mockMvc.perform(delete("{baseURL}/{id}", ProductAPI.BASE_URL, ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBodyJson = response.getResponse().getContentAsString();
        Product responseProduct = jsonMapper.readValue(responseBodyJson, Product.class);

        assertThat(responseProduct.getId(), is(equalTo(product.getId())));

        verify(productService, times(1)).deleteProduct(ID);
    }

    @Test
    void deleteProduct_should_return_404_when_product_is_not_found() throws Exception {
        // given
        final Integer ID = 1;

        // when
        when(productService.deleteProduct(ID)).thenThrow(new ProductNotFoundException(ID));

        // then
        mockMvc.perform(delete("{baseURL}/{id}", ProductAPI.BASE_URL, ID))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteProduct(ID);
    }

    @Test
    void deleteProduct_should_return_400_when_id_is_invalid() throws Exception {
        // given
        final String INVALID_ID = "id-not-valid";

        // when

        // then
        mockMvc.perform(delete("{baseURL}/{id}", ProductAPI.BASE_URL, INVALID_ID))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void getProductById_should_return_found_product_and_200_when_product_with_name_exists() throws Exception {
        // given
        final String NAME = "Product Name";
        final Product product = new Product();
        product.setName(NAME);

        // when
        when(productService.getOneProduct(NAME)).thenReturn(product);

        // then
        MvcResult response = mockMvc.perform(get("{baseURL}/name/{name}", ProductAPI.BASE_URL, NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBodyJson = response.getResponse().getContentAsString();
        Product responseProduct = jsonMapper.readValue(responseBodyJson, Product.class);

        assertThat(responseProduct, is(equalTo(product)));
        verify(productService, times(1)).getOneProduct(NAME);
    }

    @Test
    void getProductById_should_throw_product_not_found_exception_when_product_with_name_does_not_exist()
            throws Exception {

        // given
        final String NAME = "Name For Product That Does Not Exist";

        // when
        when(productService.getOneProduct(NAME)).thenThrow(new ProductNotFoundException(NAME));

        // then
        mockMvc.perform(get("{baseURL}/name/{name}", ProductAPI.BASE_URL, NAME))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getOneProduct(NAME);
    }

}