package com.poapp.Poapp.controller;

import com.poapp.Poapp.entity.Stock;
import com.poapp.Poapp.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @PostMapping(path = "/addStocks")
    public ResponseEntity<String> addStocks(
            @RequestBody Stock stock
    ){
        try {
            Stock existingStock = stockRepository.findByStockName(stock.getStockName())
                    .orElse(null);

            if (existingStock == null) {
                stockRepository.save(stock);
            } else {
                existingStock.setCurrPrice(stock.getCurrPrice());
                existingStock.setClosePrice(stock.getClosePrice());
                existingStock.setOpenPrice(stock.getOpenPrice());
                existingStock.setLowPrice(stock.getLowPrice());
                existingStock.setHighPrice(stock.getHighPrice());

                stockRepository.save(existingStock);
            }

            return ResponseEntity.ok("Stocks added/updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding/updating stock: " + e.getMessage());
        }
    }


}
