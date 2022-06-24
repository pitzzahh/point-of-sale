package com.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import com.pos.entity.Product;
import java.time.LocalDate;
import java.util.List;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM #{#entityName} p")
    List<Product> getAllProducts();

    @Query("SELECT p FROM #{#entityName} p WHERE p.id = ?1")
    Product getProductById(int id);

    @Query("SELECT p FROM #{#entityName} p WHERE p.name = ?1")
    Product getProductByName(String name);

    @Query("SELECT p.stocks from #{#entityName} p WHERE id = ?1")
    int getProductStocksById(int id);

    @Query("SELECT p.expirationDate FROM #{#entityName} p")
    List<LocalDate> getAllProductsExpirationDate();

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} p SET p.expired = ?1 WHERE p.id = ?2")
    void updateProductExpiredStatusById(boolean isExpired, int id);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} p SET p.price = ?1 WHERE p.id = ?2")
    void updateProductPriceById(double newPrice, int id);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} p SET p.stocks = ?1 WHERE p.name = ?2")
    void updateProductStocksByName(int newStocks, String name);


    @Modifying
    @Transactional
    @Query("update #{#entityName} p set p.discount = ?1 where p.id = ?2")
    void updateProductDiscountById(double newDiscount, int id);

    @Modifying
    @Transactional
    @Query("delete from #{#entityName} p where p.id = ?1")
    void deleteProductById(int id);
    
    @Modifying
    @Transactional
    @Query("delete from #{#entityName} p where p.expired = TRUE")
    void deleteAllExpiredProducts();
}