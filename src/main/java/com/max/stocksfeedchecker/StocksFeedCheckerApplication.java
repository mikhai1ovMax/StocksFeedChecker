package com.max.stocksfeedchecker;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.model.Company;
import com.max.stocksfeedchecker.repository.CompanyRepository;
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
    private final CompanyRepository repository;

    @Autowired
    public StocksFeedCheckerApplication(IEXCloudClient iexCloudClient, IEXService iexService, CompanyRepository repository) {
        this.iexCloudClient = iexCloudClient;
        this.iexService = iexService;
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(StocksFeedCheckerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> symbols = iexCloudClient.getCompaniesSymbols();
        while (true) {
            long time = System.currentTimeMillis();
            List<Company> companies = iexService.getCompaniesData(symbols);
            iexService.printHighestValueStocks(companies);
            iexService.printMostResentCompanies(companies);
            repository.saveAll(companies);
            time = System.currentTimeMillis() - time;
            System.out.println(time);
            if (time < 5000)
                Thread.sleep(5000 - time);
        }
    }
}
