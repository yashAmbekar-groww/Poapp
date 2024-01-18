package com.poapp.Poapp.repository;

import com.poapp.Poapp.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
//    List<Trade> findByUserAccIdAndTradeTypeAndQuantityAndStock_StockId(
//            Long userAccountId, String tradeType, int quantity, Long stockId);
}

