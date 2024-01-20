package com.poapp.Poapp.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.poapp.Poapp.entity.Stock;
import com.poapp.Poapp.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessCSV_ValidFile() throws IOException, CsvValidationException {
        StockService stockService = new StockService(stockRepository);

        String csvContent = "MARKET,SERIES,SYMBOL,SECURITY,PREV_CL_PR,OPEN_PRICE,HIGH_PRICE,LOW_PRICE,CLOSE_PRICE,NET_TRDVAL,NET_TRDQTY,CORP_IND,HI_52_WK,LO_52_WK\n" +
                "N,SM,AATMAJ,AATMAJ HEALTHCARE LIMITED,42.55,43.1,43.1,42.3,42.4,1704600,40000,,60,39.2";
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        Stock actualStock = new Stock();
        actualStock.setStockName("AATMAJ HEALTHCARE LIMITED");
        actualStock.setOpenPrice(43.1f);
        actualStock.setClosePrice(42.4f);
        actualStock.setHighPrice(43.1f);
        actualStock.setLowPrice(42.3f);
        actualStock.setCurrPrice(42.4f);

        ArgumentCaptor<List<Stock>> stockListCaptor = ArgumentCaptor.forClass(List.class);

        stockService.processCSV(file);

        verify(stockRepository, times(1)).saveAll(stockListCaptor.capture());

        List<Stock> capturedStockList = stockListCaptor.getValue();
        assertEquals(1, capturedStockList.size());

        Stock capturedStock = capturedStockList.get(0);
        assertEquals(actualStock.getStockName(), capturedStock.getStockName());
        assertEquals(actualStock.getOpenPrice(), capturedStock.getOpenPrice());
        assertEquals(actualStock.getClosePrice(), capturedStock.getClosePrice());
        assertEquals(actualStock.getHighPrice(), capturedStock.getHighPrice());
        assertEquals(actualStock.getLowPrice(), capturedStock.getLowPrice());
        assertEquals(actualStock.getCurrPrice(), capturedStock.getCurrPrice());

    }

    @Test
    public void testParseCSV_ValidFile() throws IOException, CsvValidationException {

        String csvContent = "MARKET,SERIES,SYMBOL,SECURITY,PREV_CL_PR,OPEN_PRICE,HIGH_PRICE,LOW_PRICE,CLOSE_PRICE,NET_TRDVAL,NET_TRDQTY,CORP_IND,HI_52_WK,LO_52_WK\n" +
                "N,SM,AATMAJ,AATMAJ HEALTHCARE LIMITED,42.55,43.1,43.1,42.3,42.4,1704600,40000,,60,39.2";
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        List<Stock> stocks = stockService.parseCSV(file);

        assertEquals(1, stocks.size());
        Stock stock = stocks.get(0);

        assertEquals("AATMAJ HEALTHCARE LIMITED", stock.getStockName());
        assertEquals(43.1f, stock.getOpenPrice(), 0.01);
        assertEquals(42.4f, stock.getClosePrice(), 0.01);
        assertEquals(43.1f, stock.getHighPrice(), 0.01);
        assertEquals(42.3f, stock.getLowPrice(), 0.01);
        assertEquals(42.4f, stock.getCurrPrice(), 0.01);
    }

    @Test
    public void testParseCSV_EmptyFile() throws IOException, CsvValidationException {

        String csvContent = "";
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        List<Stock> stocks = stockService.parseCSV(file);

        assertEquals(0, stocks.size());
    }

    @Test
    public void testParseCSV_InvalidContent() throws IOException, CsvValidationException {

        String csvContent = "MARKET,SERIES,SYMBOL,SECURITY,PREV_CL_PR,OPEN_PRICE,HIGH_PRICE,LOW_PRICE,CLOSE_PRICE,NET_TRDVAL,CORP_IND,HI_52_WK,LO_52_WK\n" +
                "N,SM,AATMAJ,AATMAJ HEALTHCARE LIMITED,42.55,43.1,43.1,42.3,42.4,";  // Missing columns
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        assertThrows(CsvValidationException.class, () -> stockService.parseCSV(file));
    }

}


