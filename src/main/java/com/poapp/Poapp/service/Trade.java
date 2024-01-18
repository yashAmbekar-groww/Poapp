//package com.poapp.Poapp.service;
//
//import com.poapp.Poapp.controller.TradeController;
//import com.poapp.Poapp.entity.Holding;
//import com.poapp.Poapp.entity.Stock;
//import com.poapp.Poapp.entity.Trade;
//import com.poapp.Poapp.repository.HoldingRepository;
//import com.poapp.Poapp.repository.StockRepository;
//import com.poapp.Poapp.repository.TradeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class TradeService {
//
//    private TradeRepository tradeRepository;
//    private StockRepository stockRepository;
//    private HoldingRepository holdingRepository;
//
//    @Autowired
//    TradeService(TradeRepository tradeRepository, StockRepository stockRepository, HoldingRepository holdingRepository) {
//        this.tradeRepository = tradeRepository;
//        this.stockRepository = stockRepository;
//        this.holdingRepository = holdingRepository;
//    }
//
//    @Transactional
//    public TradeController.TradeResponse executeTrade(Long userAccId, String tradeType, int quantity, Long stockId) {
//
//        try {
//
//            Stock stock = stockRepository.findById(stockId)
//                    .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + stockId));
//
//            try {
//
//                Holding holding = findOrCreateHolding(stock);
//
//                // Validate tradeType (Assuming "Buy" or "Sell" are the only valid values)
//                if (!tradeType.equalsIgnoreCase("Buy") && !tradeType.equalsIgnoreCase("Sell")) {
//                    throw new RuntimeException("Invalid trade type. Allowed values are 'Buy' or 'Sell'.");
//                }
//
//                // Validate quantity (Assuming quantity must be a positive integer)
//                if (quantity <= 0) {
//                    throw new RuntimeException("Invalid quantity. Quantity must be a positive integer.");
//                }
//
//                if (tradeType.equalsIgnoreCase("Buy")) {
//                    executeBuyTrade(holding, quantity);
//                } else {
//                    executeSellTrade(holding, quantity);
//                }
//
//                // Record the trade
//                Trade trade = new Trade();
//                trade.setUserAccId(user.getUserAccId());
//                trade.setTradeType(tradeType);
//                trade.setQuantity(quantity);
//                trade.setStockId(stock.getStockId());
//                trade.setStatus("Success");
//                trade.setMessage("Trade executed successfully");
//
//                tradeRepository.save(trade);
//
//                return new TradeController.TradeResponse("Success", "Trade executed successfully");
//            } catch (Exception e) {
//                Trade trade = new Trade();
//                trade.setUserAccId(user.getUserAccId());
//                trade.setTradeType(tradeType);
//                trade.setQuantity(quantity);
//                trade.setStockId(stock.getStockId());
//                trade.setStatus("Failure");
//                trade.setMessage(e.getMessage());
//
//                tradeRepository.save(trade);
//                return new TradeController.TradeResponse("Failure", e.getMessage());
//            }
//        } catch (Exception e) {
//            Trade trade = new Trade();
//            trade.setTradeType(tradeType);
//            trade.setQuantity(quantity);
//            trade.setStatus("Failure");
//            trade.setMessage(e.getMessage());
//
//            tradeRepository.save(trade);
//            return new TradeController.TradeResponse("Failure", e.getMessage());
//        }
//
//    }
//
//    private Holding findOrCreateHolding(User user, Stock stock) {
//        return user.getHoldings().stream()
//                .filter(holding -> holding.getStockId().equals(stock.getStockId()))
//                .findFirst()
//                .orElseGet(() -> {
//                    Holding newHolding = new Holding();
//                    newHolding.setUserAccId(user.getUserAccId());
//                    newHolding.setStockId(stock.getStockId());
//                    newHolding.setQuantity(0);
//                    newHolding.setGainLoss((float) 0.0);
//                    float currentPrice = stockRepository.findById(stock.getStockId())
//                            .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + stock.getStockId()))
//                            .getCurrPrice();
//
//                    newHolding.setBuyPrice(currentPrice);
//                    user.getHoldings().add(newHolding);
//                    return newHolding;
//                });
//    }
//
//    private void executeBuyTrade(Holding holding, int quantity) {
//        int newQuantity = holding.getQuantity() + quantity;
//        holding.setQuantity(newQuantity);
//        holdingRepository.save(holding);
//    }
//
//    private void executeSellTrade(Holding holding, int quantity) {
//        int availableQuantity = holding.getQuantity();
//        if (quantity > availableQuantity) {
//            throw new RuntimeException("Insufficient stock quantity available for sell.");
//        }
//        int newQuantity = holding.getQuantity() - quantity;
//        holding.setQuantity(newQuantity);
//        holdingRepository.save(holding);
//    }
//
//}
//
//
