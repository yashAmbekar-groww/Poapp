package com.poapp.Poapp.service;

import com.poapp.Poapp.controller.PortfolioController;
import com.poapp.Poapp.entity.Holding;
import com.poapp.Poapp.repository.HoldingRepository;

import com.poapp.Poapp.repository.StockRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private HoldingRepository holdingRepository;
    @Autowired
    private StockRepository stockRepository;

    @Transactional
    public PortfolioController.PortfolioResponse getUserPortfolio(Long userAccId){

        try{
            List<Holding> holdings = holdingRepository.findByUserAccId(userAccId);

            int totalStocksInvested = holdings.size();

            Totals totals = getTotals(holdings);

            return new PortfolioController.PortfolioResponse(
                    holdings,
                    totalStocksInvested,
                    totals.totalBuyPrice,
                    totals.totalProfitLoss,
                    "Success",
                    "Portfolio Retrieved!"
            );


        }catch (Exception e){
            List<Holding> emptyHolding = new ArrayList<>(0);
            return new PortfolioController.PortfolioResponse(
                    emptyHolding,
                    0,
                    0f,
                    0f,
                    "Failure",
                    e.getMessage()
            );
        }


    }

    private Totals getTotals(List<Holding>holdings){
        Totals totals = new Totals();

        Float totalBuyPrice = (float) 0;
        float totalProfitLoss = (float) 0;
        for(Holding i :holdings){
            totalBuyPrice += i.getBuyPrice();
            Float currentPrice = stockRepository.findById(i.getStockId())
                    .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + i.getStockId()))
                    .getCurrPrice();
            Float gainLoss = i.getBuyPrice() - currentPrice;
            i.setGainLoss(gainLoss);
            totalProfitLoss += gainLoss;

        }

        totals.setTotalBuyPrice(totalBuyPrice);
        totals.setTotalProfitLoss(totalProfitLoss);
        return totals;
    }


    @Data
    private static class Totals{
        private int totalStocksInvested;
        private Float totalBuyPrice;
        private Float totalProfitLoss;

    }


    // Add methods for calculating total PL, updating holdings, etc.
}

