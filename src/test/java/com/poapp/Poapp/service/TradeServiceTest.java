package com.poapp.Poapp.service;

import com.poapp.Poapp.controller.TradeController;
import com.poapp.Poapp.entity.Holding;
import com.poapp.Poapp.entity.Stock;
import com.poapp.Poapp.entity.Trade;
import com.poapp.Poapp.repository.HoldingRepository;
import com.poapp.Poapp.repository.StockRepository;
import com.poapp.Poapp.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private HoldingRepository holdingRepository;

    @InjectMocks
    private TradeService tradeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecuteBuyTrade() {
        Holding holding = new Holding();
        holding.setQuantity(10);
        holding.setBuyPrice(100.0f);

        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(new Stock()));
        when(holdingRepository.save(any(Holding.class))).thenReturn(holding);

        tradeService.executeBuyTrade(holding, 5, 1L);

        assertEquals(15, holding.getQuantity());
        // Adjust this assertion based on your actual calculation in executeBuyTrade
        assertEquals(210f, holding.getBuyPrice());
    }

    @Test
    public void testExecuteSellTrade() {
        Holding holding = new Holding();
        holding.setQuantity(10);

        when(holdingRepository.save(any(Holding.class))).thenReturn(holding);

        tradeService.executeSellTrade(holding, 5);

        assertEquals(5, holding.getQuantity());
    }

    @Test
    public void testExecuteTrade_Success() {
        when(holdingRepository.findByUserAccIdAndStockId(anyLong(), anyLong())).thenReturn(Optional.of(new Holding()));
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(new Stock()));

        TradeController.TradeResponse response = tradeService.executeTrade(123L, "Buy", 5, 1L);

        assertEquals("Success", response.getStatus());
        assertEquals("Trade executed successfully", response.getData());

        // Verify that the tradeRepository.save method is called
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }

    @Test
    public void testExecuteTrade_InvalidTradeType() {
        assertThrows(RuntimeException.class, () -> tradeService.executeTrade(123L, "InvalidType", 5, 1L));
    }

    @Test
    public void testExecuteTrade_InvalidQuantity() {
        assertThrows(RuntimeException.class, () -> tradeService.executeTrade(123L, "Buy", -5, 1L));
    }

    @Test
    public void testExecuteTrade_Failure() {
        when(holdingRepository.findByUserAccIdAndStockId(anyLong(), anyLong())).thenReturn(Optional.of(new Holding()));
        when(stockRepository.findById(anyLong())).thenThrow(new RuntimeException("Stock not found"));

        TradeController.TradeResponse response = tradeService.executeTrade(1L, "Buy", 5, 1L);

        assertEquals("Failure", response.getStatus());
        assertEquals("Stock not found", response.getData());

        // Verify that the tradeRepository.save method is called
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }
}

