package com.example.ordersapi.product.service.impl;

import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.api.dto.UpdateProductDto;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductAlreadyExistsException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.mapper.ProductMapper;
import com.example.ordersapi.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void getAllProducts_should_return_found_products() {
        // given
        Product product1 = new Product();
        product1.setId(1);
        Product product2 = new Product();
        product2.setId(2);

        // when
        List<Product> toReturn = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(toReturn);

        // then
        Set<Product> returnedProducts = productService.getAllProducts();
        assertEquals(toReturn.size(), returnedProducts.size());
    }

    @Test
    void getProductsPage_should_return_a_list_with_page_contents() {
        // given
        int page = 1;
        int size = 10;

        List<Product> products = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Product p = new Product();
            p.setId(i+1);

            products.add(p);
        }

        // when
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        when(productRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(products));

        // then
        List<Product> returnedProducts = productService.getProductsPage(page, size);
        assertThat(returnedProducts.size(), is(equalTo(products.size())));

        verify(productRepository, times(1)).findAll(pageRequest);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getProductsPage_should_return_an_empty_list() {
        // given
        int page = 2;
        int size = Integer.MAX_VALUE;

        // when
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        when(productRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(Collections.emptyList()));

        // then
        List<Product> returnedProducts = productService.getProductsPage(page, size);
        assertThat(returnedProducts.size(), is(equalTo(0)));

        verify(productRepository, times(1)).findAll(pageRequest);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getOneProduct_by_id_should_return_found_product() throws Exception {
        // given
        final int ID = 1;
        Product product = new Product();
        product.setId(ID);

        when(productRepository.findById(ID)).thenReturn(Optional.of(product));

        // then
        Product returnedProduct = productService.getOneProduct(ID);
        assertEquals(ID, returnedProduct.getId());
    }

    @Test
    void getOneProduct_by_id_should_throw_product_not_found_exception() {

        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getOneProduct(1));
    }

    @Test
    void createProduct_should_return_created_product() throws Exception {
        // given
        final Integer ID = 1;
        final String NAME = "Product Name";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        Product mappedProduct = new Product();
        mappedProduct.setName(NAME);
        mappedProduct.setPrice(PRICE);

        Product expectedProduct = new Product();
        expectedProduct.setId(ID);
        expectedProduct.setName(NAME);
        expectedProduct.setPrice(PRICE);

        // when
        when(productMapper.createProductDtoToProduct(productDto)).thenReturn(mappedProduct);
        when(productRepository.saveAndFlush(mappedProduct)).thenReturn(expectedProduct);

        // then
        Product savedProduct = productService.createProduct(productDto);
        assertThat(savedProduct.getId(), equalTo(ID));
        assertThat(savedProduct.getName(), equalTo(NAME));
        assertThat(savedProduct.getPrice(), equalTo(PRICE));
    }

    @Test
    void createProduct_should_throw_product_already_exists_when_product_name_is_already_registered() {
        // given
        CreateProductDto productDto = new CreateProductDto();
        Product mappedProduct = new Product();

        // when
        when(productMapper.createProductDtoToProduct(productDto)).thenReturn(mappedProduct);
        when(productRepository.saveAndFlush(mappedProduct)).thenThrow(DataIntegrityViolationException.class);

        // then
        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(productDto));
    }

    @Test
    void updateProduct_should_return_updated_product() throws Exception {
        //  given
        final Integer ID = 1;
        final String INITIAL_NAME = "My Product";
        final BigDecimal INITIAL_PRICE = BigDecimal.valueOf(2.95);
        final String UPDATED_NAME = "My Product Updated";
        final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(3.60);

        UpdateProductDto productDto = new UpdateProductDto();
        productDto.setName(UPDATED_NAME);
        productDto.setPrice(UPDATED_PRICE);

        Product initialProduct = new Product();
        initialProduct.setId(ID);
        initialProduct.setName(INITIAL_NAME);
        initialProduct.setPrice(INITIAL_PRICE);

        Product updatedProduct = new Product();
        updatedProduct.setId(ID);
        updatedProduct.setName(UPDATED_NAME);
        updatedProduct.setPrice(UPDATED_PRICE);

        // when
        when(productRepository.findById(ID)).thenReturn(Optional.of(initialProduct));
        when(productRepository.saveAndFlush(updatedProduct)).thenReturn(updatedProduct);

        // then
        Product returnedProduct = productService.updateProduct(ID, productDto);
        assertThat(returnedProduct.getName(), equalTo(UPDATED_NAME));
        assertThat(returnedProduct.getPrice(), equalTo(UPDATED_PRICE));

        verify(productRepository, times(1)).findById(ID);
        verify(productRepository, times(1)).saveAndFlush(updatedProduct);
    }

    @Test
    void updateProduct_should_throw_product_not_found_exception_when_product_does_not_exist() {
        //  given
        final Integer ID = 1;
        final UpdateProductDto productDto = new UpdateProductDto();

        // when
        when(productRepository.findById(ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(ID, productDto));
    }

    @Test
    void updateProduct_should_throw_product_already_exists_exception_when_product_name_is_already_in_use() {
        //  given
        final Integer ID = 1;
        final UpdateProductDto productDto = new UpdateProductDto();

        productDto.setName("This Name Already Exists");

        // when
        when(productRepository.findById(ID)).thenReturn(Optional.of(new Product()));
        when(productRepository.saveAndFlush(any())).thenThrow(DataIntegrityViolationException.class);

        // then
        assertThrows(ProductAlreadyExistsException.class, () -> productService.updateProduct(ID, productDto));
    }

    @Test
    void updateProduct_should_not_call_repository_save_when_there_is_nothing_to_update_empty_fields() throws Exception {
        //  given
        final Integer ID = 1;
        final String INITIAL_NAME = "My Product";
        final BigDecimal INITIAL_PRICE = BigDecimal.valueOf(2.95);

        UpdateProductDto productDto = new UpdateProductDto();

        Product initialProduct = new Product();
        initialProduct.setId(ID);
        initialProduct.setName(INITIAL_NAME);
        initialProduct.setPrice(INITIAL_PRICE);

        // when
        when(productRepository.findById(ID)).thenReturn(Optional.of(initialProduct));

        // then
        Product returnedProduct = productService.updateProduct(ID, productDto);
        assertThat(returnedProduct.getName(), equalTo(INITIAL_NAME));
        assertThat(returnedProduct.getPrice(), equalTo(INITIAL_PRICE));

        verify(productRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void updateProduct_should_not_call_repository_save_when_there_is_nothing_to_update_equal_property_values()
            throws Exception {

        //  given
        final Integer ID = 1;
        final String INITIAL_NAME = "My Product";
        final BigDecimal INITIAL_PRICE = BigDecimal.valueOf(2.95);

        UpdateProductDto productDto = new UpdateProductDto();
        productDto.setName(INITIAL_NAME);
        productDto.setPrice(INITIAL_PRICE);

        Product initialProduct = new Product();
        initialProduct.setId(ID);
        initialProduct.setName(INITIAL_NAME);
        initialProduct.setPrice(INITIAL_PRICE);

        // when
        when(productRepository.findById(ID)).thenReturn(Optional.of(initialProduct));

        // then
        Product returnedProduct = productService.updateProduct(ID, productDto);
        assertThat(returnedProduct.getName(), equalTo(INITIAL_NAME));
        assertThat(returnedProduct.getPrice(), equalTo(INITIAL_PRICE));

        verify(productRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void deleteProduct_should_return_deleted_product() throws Exception {
        // given
        final Integer ID = 1;
        final Product product = new Product();
        product.setId(ID);

        // when
        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        // Don't need to mock productRepository.delete(product)

        // then
        Product returnedProduct = productService.deleteProduct(ID);
        assertThat(returnedProduct.getId(), is(equalTo(product.getId())));

        verify(productRepository, times(1)).findById(ID);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProduct_should_throw_product_not_found_exception_when_id_does_not_match_a_product() {
        // given
        final Integer ID = 1;

        // when
        when(productRepository.findById(ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(ID));

        verify(productRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getOneProduct_should_return_found_product_when_product_with_name_exists() throws Exception {
        // given
        final String NAME = "Product Name";
        final Product product = new Product();
        product.setName(NAME);

        // when
        when(productRepository.findByName(NAME)).thenReturn(Optional.of(product));

        // then
        Product returnedProduct = productService.getOneProduct(NAME);

        assertThat(returnedProduct, is(equalTo(product)));

        verify(productRepository, times(1)).findByName(NAME);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getOneProduct_should_throw_product_not_found_exception_when_product_with_name_does_not_exist() {

        // given
        final String NAME = "Name For Product That Does Not Exist";

        // when
        when(productRepository.findByName(NAME)).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.getOneProduct(NAME));

        verify(productRepository, times(1)).findByName(NAME);
        verifyNoMoreInteractions(productRepository);
    }

}