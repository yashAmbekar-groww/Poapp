package com.poapp.Poapp.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.poapp.Poapp.entity.Stock;
import com.poapp.Poapp.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void processCSV(MultipartFile file) {
        try {
            List<Stock> entities = parseCSV(file);
            stockRepository.saveAll(entities);
        } catch (IOException | CsvValidationException e) {
            // Handle the exception (e.g., log it or return an error response)
            e.printStackTrace();
        }
    }

    public List<Stock> parseCSV(MultipartFile file) throws IOException, CsvValidationException {
        List<Stock> stocks = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            boolean headerSkipped = false;

            while ((nextLine = reader.readNext()) != null) {
                if (!headerSkipped) {
                    // Skip the header line
                    headerSkipped = true;
                    continue;
                }

                Stock stock = new Stock();
                try{
                    if(
                        nextLine.length == 14 &&
                        !nextLine[3].isEmpty() &&
                        !nextLine[5].isEmpty() &&
                        !nextLine[6].isEmpty() &&
                        !nextLine[7].isEmpty() &&
                        !nextLine[8].isEmpty()
                    ){
                        stock.setStockName(nextLine[3]);
                        stock.setOpenPrice(Float.parseFloat(nextLine[5]));
                        stock.setHighPrice(Float.parseFloat(nextLine[6]));
                        stock.setLowPrice(Float.parseFloat(nextLine[7]));
                        stock.setClosePrice(Float.parseFloat(nextLine[8]));
                        stock.setCurrPrice(Float.parseFloat(nextLine[8]));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                stocks.add(stock);
            }
        }

        return stocks;
    }
}

