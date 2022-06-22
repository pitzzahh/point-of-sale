/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pos.service;

import com.pos.entity.Product;
import com.pos.entity.Sales;
import com.pos.repository.SalesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author peter
 */
@Transactional
@Service("salesService")
public class SalesServiceImplementation implements SalesService {
    
    @Autowired
    private SalesRepository salesRepository;
    
    @Override
    public Supplier<List<Sales>> getAllSales() {
        return salesRepository::getAllSales;
    }

    @Override
    public Supplier<List<Double>> getAllProfit() {
        return salesRepository::getAllProfit;
    }

}
