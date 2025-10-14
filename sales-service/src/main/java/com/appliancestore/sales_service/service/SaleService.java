package com.appliancestore.sales_service.service;

import com.appliancestore.sales_service.repository.ISaleRepository;
import org.springframework.stereotype.Service;

@Service
public class SaleService implements ISaleService{
    private ISaleRepository saleRepo;

}
