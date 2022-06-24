package com.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.pos.entity.Sales;
import java.util.List;

@Repository("salesRepository")
public interface SalesRepository extends JpaRepository<Sales, Integer> {

    @Query("SELECT s FROM #{#entityName} s")
    List<Sales> getAllSales();

    @Query("SELECT s.profit FROM #{#entityName} s")
    List<Double> getAllProfit();
}