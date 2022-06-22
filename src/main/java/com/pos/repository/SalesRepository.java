package com.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Supplier;
import com.pos.entity.Sales;

@Repository("salesRepository")
public interface SalesRepository extends JpaRepository<Sales, Integer> {

    @Query("select s.salesNumber from #{#entityName} s")
    List<Sales> getAllSales();

    @Query("select s.profit from #{#entityName} s")
    List<Double> getAllProfit();
}