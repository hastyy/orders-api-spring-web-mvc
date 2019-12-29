package com.example.ordersapi.product.mapper;

import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

class ProductMapperTest {

    public static final String NAME = "Product Name";
    public static final String DESCRIPTION = "Product Description";
    public static final String IMAGE_URL = "http://orders-api-product-image.xyz/";
    public static final BigDecimal PRICE = BigDecimal.valueOf(2.95);

    @Test
    void createProductDtoToProduct() {
        // given
        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setDescription(DESCRIPTION);
        productDto.setImageUrl(IMAGE_URL);
        productDto.setPrice(PRICE);

        // when
        Product mappedProduct = ProductMapper.INSTANCE.createProductDtoToProduct(productDto);

        // then
        assertThat(mappedProduct, is(not(nullValue())));
        assertThat(mappedProduct.getName(), equalTo(NAME));
        assertThat(mappedProduct.getDescription(), equalTo(DESCRIPTION));
        assertThat(mappedProduct.getImageUrl(), equalTo(IMAGE_URL));
        assertThat(mappedProduct.getPrice(), equalTo(PRICE));
    }
}