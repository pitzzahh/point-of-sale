package com.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.pos.entity.Sales;
import java.util.List;

@Repository("salesRepository")
public interface SalesRepository extends JpaRepository<Sales, Integer> {

    @Query("select s from #{#entityName} s")
    List<Sales> getAllSales();

    @Query("select s.profit from #{#entityName} s")
    List<Double> getAllProfit();
}