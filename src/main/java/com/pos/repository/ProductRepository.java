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

    @Query("select p from #{#entityName} p")
    List<Product> getAllProducts();

    @Query("select p from #{#entityName} p where p.id = ?1")
    Product getProductById(int id);

    @Query("select p from #{#entityName} p where p.name = ?1")
    Product getProductByName(String name);

    @Query("select p.stocks from #{#entityName} p where id = ?1")
    int getProductStocksById(int id);

    @Query("select p.expirationDate from #{#entityName} p")
    List<LocalDate> getAllProductsExpirationDate();

    @Modifying
    @Transactional
    @Query("update #{#entityName} p set p.expired = ?1 where p.id = ?2")
    void updateProductExpiredStatusById(boolean isExpired, int id);

    @Modifying
    @Transactional
    @Query("delete from #{#entityName} p where p.id = ?1")
    void deleteProductById(int id);
    
    @Modifying
    @Transactional
    @Query("delete from #{#entityName} p where p.expired = TRUE")
    void deleteAllExpiredProducts();
}