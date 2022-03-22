package com.max.stocksfeedchecker;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.service.IEXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StocksFeedCheckerApplication implements CommandLineRunner {

    private final IEXCloudClient iexCloudClient;
    private final IEXService IEXService;

    @Autowired
    public StocksFeedCheckerApplication(IEXCloudClient iexCloudClient, IEXService IEXService) {
        this.iexCloudClient = iexCloudClient;
        this.IEXService = IEXService;
    }

    public static void main(String[] args) {
        SpringApplication.run(StocksFeedCheckerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        iexCloudClient.getCompanyBySymbol("A");
//        iexCloudClient.getAllSymbols();
        IEXService.getAllCompanyData();
    }
}
