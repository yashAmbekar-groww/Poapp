package com.poapp.Poapp.service;

import com.poapp.Poapp.controller.PortfolioController;
import com.poapp.Poapp.entity.Holding;
import com.poapp.Poapp.entity.Stock;
import com.poapp.Poapp.repository.HoldingRepository;
import com.poapp.Poapp.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PortfolioServiceTest {

    @Mock
    private HoldingRepository holdingRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserPortfolio_Success() {
        // Arrange
        Long userAccId = 123L;
        List<Holding> holdings = createSampleHoldings();
        when(holdingRepository.findByUserAccId(userAccId)).thenReturn(Optional.of(holdings));

        // Mocking stockRepository calls
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(createSampleStock()));

        // Act
        PortfolioController.PortfolioResponse response = portfolioService.getUserPortfolio(userAccId);

        // Assert
        assertEquals("Success", response.getStatus());
        assertEquals("Portfolio Retrieved!", response.getMsg());
        assertEquals(holdings, response.getHoldings());
        assertEquals(holdings.size(), response.getTotalStocksInvested());
        // Add more assertions based on your expectations
    }

    @Test
    public void testGetUserPortfolio_HoldingsNotFound() {
        // Arrange
        Long userAccId = 123L;
        when(holdingRepository.findByUserAccId(userAccId)).thenReturn(Optional.empty());

        // Act
        PortfolioController.PortfolioResponse response = portfolioService.getUserPortfolio(userAccId);

        // Assert
        assertEquals("Failure", response.getStatus());
        assertEquals("Holding not found", response.getMsg());
        assertEquals(0, response.getTotalStocksInvested());
        // Add more assertions based on your expectations
    }

    @Test
    public void testGetUserPortfolio_RuntimeException() {
        // Arrange
        Long userAccId = 123L;
        when(holdingRepository.findByUserAccId(userAccId)).thenThrow(new RuntimeException("Test Exception"));

        // Act
        PortfolioController.PortfolioResponse response = portfolioService.getUserPortfolio(userAccId);

        // Assert
        assertEquals("Failure", response.getStatus());
        assertEquals("Test Exception", response.getMsg());
        assertEquals(0, response.getTotalStocksInvested());
        // Add more assertions based on your expectations
    }

    private List<Holding> createSampleHoldings() {
        List<Holding> holdings = new ArrayList<>();
        // Create sample holdings for testing
        // Ensure that the sample holdings cover different scenarios
        return holdings;
    }

    private Stock createSampleStock() {
        Stock stock = new Stock();
        // Set necessary properties for testing
        return stock;
    }
}


