package com.pos.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pos.repository.SalesRepository;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.pos.entity.Sales;
import java.util.List;

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

    @Override
    public Consumer<Sales> saveSales() {
        return sales -> salesRepository.save(sales);
    }

}
