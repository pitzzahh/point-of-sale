package com.pos.service;

import java.util.function.Consumer;
import java.util.function.Supplier;
import com.pos.entity.Sales;
import java.util.List;

/**
 *
 * @author peter
 */
public interface SalesService {
    
    Supplier<List<Sales>>  getAllSales();
    
    Consumer<Sales> saveSales();
    
    Supplier<List<Double>> getAllProfit();
    
}
