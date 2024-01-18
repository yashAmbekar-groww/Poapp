package com.poapp.Poapp.controller;

import com.poapp.Poapp.entity.Holding;
import com.poapp.Poapp.service.PortfolioService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping(path = "/getPortfolio")
    public ResponseEntity<PortfolioResponse> getPortfolio(
            @RequestParam Long userAccId
    ){
        PortfolioResponse response = portfolioService.getUserPortfolio(userAccId);
        HttpStatus httpStatus = response.getStatus().equals("Success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, httpStatus);
    }


    @Data
    public static class PortfolioResponse{
        private List<Holding> holdings;
        private int totalStocksInvested;
        private Float totalBuyPrice;
        private Float totalProfitLoss;
        private String status;
        private String msg;

        public PortfolioResponse(List<Holding> holdings, int totalStocksInvested, Float totalBuyPrice, Float totalProfitLoss, String status, String msg){
            this.holdings= holdings;
            this.totalStocksInvested = totalStocksInvested;
            this.totalBuyPrice = totalBuyPrice;
            this.totalProfitLoss = totalProfitLoss;
            this.status = status;
            this.msg = msg;
        }
    }
}
