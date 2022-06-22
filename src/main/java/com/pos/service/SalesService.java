/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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
