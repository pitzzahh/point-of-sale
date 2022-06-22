/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pos.service;

import com.pos.entity.Product;
import com.pos.entity.Sales;
import java.util.List;
import java.util.function.Supplier;

/**
 *
 * @author peter
 */
public interface SalesService {

    Supplier<List<Sales>>  getAllSales();

    Supplier<List<Double>> getAllProfit();
}
