package com.github.pitzzahh.dao;

import com.github.pitzzahh.entity.Product;
import com.github.pitzzahh.entity.Sales;
import com.github.pitzzahh.mapper.ProductMapper;
import com.github.pitzzahh.mapper.SalesMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import static com.github.pitzzahh.enums.Status.*;

import java.util.*;
import java.util.function.Supplier;

import com.github.pitzzahh.enums.Status;
import java.time.LocalDate;

// TODO create a test for this.
public class Queries {

    public static Supplier<List<Optional<Product>>> getAllProductsQuery(JdbcTemplate jdbc) {
        return () -> jdbc.query("SELECT * FROM products", new ProductMapper());
    }

    public static Optional<Long> getExpiredProductsCountQuery(JdbcTemplate jdbc) {
        return Optional.of(getAllProductsQuery(jdbc).get().stream().filter(p -> p.get().getExpirationDate().isBefore(LocalDate.now())).count());
    }

    public static Optional<Product> getProductByIdQuery(int id, JdbcTemplate jdbc) {
        try {
            return jdbc.queryForObject("SELECT * FROM products WHERE product_id = ?", new Object[]{id}, new ProductMapper());
        } catch (EmptyResultDataAccessException ignored) {}
        return Optional.empty();
    }

    public static OptionalDouble getProductPriceByIdQuery(int id, JdbcTemplate jdbc) {
        try {
            return OptionalDouble.of(Objects.requireNonNull(jdbc.queryForObject("SELECT price FROM products WHERE product_id = ?", new Object[]{id}, new ProductMapper())).get().getPrice());
        } catch (EmptyResultDataAccessException ignored) {}
        return OptionalDouble.empty();
    }

    public static Optional<Product> getProductByNameQuery(String productName, JdbcTemplate jdbc) {
        try {
            return jdbc.queryForObject("SELECT * FROM products WHERE name = ?", new Object[]{productName}, new ProductMapper());
        } catch (EmptyResultDataAccessException ignored) {}
        return Optional.empty();
    }

    public static OptionalInt getProductStocksByIdQuery(int id, JdbcTemplate jdbc) {
        try {
            return OptionalInt.of(Objects.requireNonNull(jdbc.queryForObject("SELECT stocks_left FROM products WHERE product_id = ?", new Object[]{id}, new ProductMapper())).get().getStocks());
        } catch (EmptyResultDataAccessException ignored) {}
        return OptionalInt.empty();
    }

    public static Status updateProductPriceByIdQuery(double price, int id, JdbcTemplate jdbc) {
        return jdbc.update("UPDATE products SET price = ? WHERE product_id = ?", price, id) > 0 ? PRICE_UPDATED_SUCCESSFULLY : ERROR_UPDATING_PRICE;
    }

    public static Status updateProductStocksByNameQuery(int stocks, String name, JdbcTemplate jdbc) {
        return jdbc.update("UPDATE products SET stocks_left = ? WHERE name = ?", stocks, name) > 0 ? STOCKS_UPDATED_SUCCESSFULLY : ERROR_UPDATING_STOCKS;
    }

    public static Status updateProductDiscountByIdQuery(double discount, int id, JdbcTemplate jdbc) {
        return jdbc.update("UPDATE products SET discount = ? WHERE product_id = ?", discount, id) > 0 ? DISCOUNT_UPDATED_SUCCESSFULLY : ERROR_UPDATING_DISCOUNT;
    }

    public static Status deleteProductByIdQuery(int id, JdbcTemplate jdbc) {
        return jdbc.update("DELETE FROM products WHERE product_id = ?", id) > 0 ? PRODUCT_REMOVED_SUCCESSFULLY : ERROR_REMOVING_PRODUCT;
    }

    public static Supplier<Status> deleteAllExpiredProductsQuery(JdbcTemplate jdbc) {
         getAllProductsQuery(jdbc).get().stream().filter(p -> p.get().getExpirationDate().isBefore(LocalDate.now())).forEach(p -> deleteProductByIdQuery(p.get().getId(), jdbc));
         return () -> SUCCESS;
    }

    public static Supplier<List<Sales>> getAllSalesQuery(JdbcTemplate jdbc) {
        return () -> jdbc.query("SELECT * FROM sales", new SalesMapper());
    }

    public static Status saveSalesQuery(Sales sales, JdbcTemplate jdbc) {
        final String QUERY = "INSERT INTO sales(date_processed, profit) VALUES (?, ?)";
        int result = jdbc.update(QUERY, sales.getDateProcessed(), sales.getProfit());
        return result > 0 ? SALES_SAVED_SUCCESSFULLY : ERROR_SAVING_SALES;
    }

}
