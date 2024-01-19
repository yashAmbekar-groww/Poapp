package com.poapp.Poapp.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.poapp.Poapp.entity.Stock;
import com.poapp.Poapp.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

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


        // Arrange
        String csvContent = "MARKET,SERIES,SYMBOL,SECURITY,PREV_CL_PR,OPEN_PRICE,HIGH_PRICE,LOW_PRICE,CLOSE_PRICE,NET_TRDVAL,NET_TRDQTY,CORP_IND,HI_52_WK,LO_52_WK\n" +
                "N,SM,AATMAJ,AATMAJ HEALTHCARE LIMITED,42.55,43.1,43.1,42.3,42.4,1704600,40000,,60,39.2";
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        // Mock behavior of parseCSV method
        when(stockService.parseCSV(file)).thenReturn(List.of(new Stock(/* set necessary properties */)));

        // Act
        stockService.processCSV(file);

        // Assert
        // Verify that saveAll method was called with the expected list of entities
        verify(stockRepository, times(1)).saveAll(List.of(new Stock(/* set necessary properties */)));
    }

    @Test
    public void testParseCSV_ValidFile() throws IOException, CsvValidationException {
        // Arrange
        String csvContent = "MARKET,SERIES,SYMBOL,SECURITY,PREV_CL_PR,OPEN_PRICE,HIGH_PRICE,LOW_PRICE,CLOSE_PRICE,NET_TRDVAL,NET_TRDQTY,CORP_IND,HI_52_WK,LO_52_WK\n" +
                "N,SM,AATMAJ,AATMAJ HEALTHCARE LIMITED,42.55,43.1,43.1,42.3,42.4,1704600,40000,,60,39.2";
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        // Act
        List<Stock> stocks = stockService.parseCSV(file);

        // Assert
        // Verify the expected properties of the Stock object in the list
        assertEquals(1, stocks.size());
        Stock stock = stocks.get(0);

        // Add more assertions based on your expected values
        assertEquals("value3", stock.getStockName()); // Assuming stockName is in column 3
        assertEquals(123.45f, stock.getOpenPrice(), 0.01); // Assuming openPrice is in column 5
        // Add more assertions based on your expected values
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

        String csvContent = "MARKET,SERIES,SYMBOL,SECURITY,PREV_CL_PR,OPEN_PRICE,HIGH_PRICE,LOW_PRICE,CLOSE_PRICE,NET_TRDVAL,NET_TRDQTY,CORP_IND,HI_52_WK,LO_52_WK\n" +
                "N,SM,AATMAJ,AATMAJ HEALTHCARE LIMITED,42.55,43.1,43.1,42.3,42.4,";  // Missing columns
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        // Act and Assert
        // Verify that parsing an invalid CSV throws CsvValidationException
        assertThrows(CsvValidationException.class, () -> stockService.parseCSV(file));
    }

    // Add more tests based on different scenarios, such as invalid CSV content, exceptions, etc.
}


