/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pos.service;

import com.pos.entity.Sales;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author peter
 */
@Transactional
@Service("salesService")
public class SalesServiceImplementation implements SalesService {

    @Override
    public List<Sales> getAllSales() {
        return new ArrayList<>();
    }
    
}
