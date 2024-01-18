package com.poapp.Poapp.controller;

import com.poapp.Poapp.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;

@RestController
@RequestMapping(path = "/api")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping(path = "/trade")
    public ResponseEntity<TradeResponse> getTradesByCriteria(
            @RequestBody TradeRequest tradeRequest
    ) {
        TradeResponse response = tradeService.executeTrade(tradeRequest.userAccId, tradeRequest.tradeType, tradeRequest.quantity, tradeRequest.stockId);
        HttpStatus httpStatus = response.getStatus().equals("Success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, httpStatus);

    }

    @Data
    public static class TradeResponse {
        private String status;
        private Object data;

        public TradeResponse(String status, Object data) {
            this.status = status;
            this.data = data;
        }
    }

    @Data
    public static class TradeRequest {

        private Long userAccId;
        private String tradeType;
        private int quantity;
        private Long stockId;

        public TradeRequest(Long userAccId, String tradeType, int quantity, Long stockId) {
            this.userAccId = userAccId;
            this.tradeType = tradeType;
            this.quantity = quantity;
            this.stockId = stockId;

        }
    }
}

