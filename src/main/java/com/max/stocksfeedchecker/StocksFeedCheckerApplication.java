package com.max.stocksfeedchecker;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.service.IEXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class StocksFeedCheckerApplication implements CommandLineRunner {

    private final IEXCloudClient iexCloudClient;
    private final IEXService iexService;

    @Autowired
    public StocksFeedCheckerApplication(IEXCloudClient iexCloudClient, IEXService iexService) {
        this.iexCloudClient = iexCloudClient;
        this.iexService = iexService;
    }

    public static void main(String[] args) {
        SpringApplication.run(StocksFeedCheckerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> symbols = iexCloudClient.getCompaniesSymbols();
        while (true) {
            new Thread(()-> iexService.printHighestValueStocks(iexService.getCompaniesData(symbols))).run();

            Thread.sleep(5000);
        }
    }
}
