package com.example.ordersapi.product.mapper;

import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product createProductDtoToProduct(CreateProductDto productDto);
}
