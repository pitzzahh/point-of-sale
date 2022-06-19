package com.pos.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.pos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import com.pos.entity.Product;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Service("productService")
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Supplier<Integer> getExpiredProductsCount() {
        return () -> (int) productRepository.getAllProductsExpirationDate()
                .stream().filter(localDate -> localDate.isBefore(LocalDate.now()))
                .count();
    }

    @Override
    public Consumer<Product> saveProduct() {
        return productRepository::save;
    }

    @Override
    public Consumer<List<Product>> saveAllProducts() {
        return productRepository::saveAll;
    }

    @Override
    public Function<Integer, Product> getProductById() {
        return productRepository::getProductById;
    }

    @Override
    public Supplier<List<Product>> getAllProducts() {
        return productRepository::getAllProducts;
    }

    @Override
    public BiConsumer<LocalDate, Integer> updateProductExpiredStatusById() {
        return (newExpirationDate, id) -> productRepository.updateProductExpiredStatusById(false, id);
    }

    @Override
    public Consumer<Integer> deleteProductById() {
        return productRepository::deleteProductById;
    }


    /**
     * checks all the products' expiration date and updates the expired column if a product is expired.
     */
    public void updateAllProductsExpirationDate() {
        productRepository
                .getAllProducts()
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getExpirationDate))
                .forEach(
                        (id, expirationDate) ->
                                productRepository.updateProductExpiredStatusById(expirationDate.isBefore(LocalDate.now()), id));

    }
}

