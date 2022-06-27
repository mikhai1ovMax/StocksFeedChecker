package com.max.stocksfeedchecker;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.job.CompanyJob;
import com.max.stocksfeedchecker.model.CompanyEntity;
import com.max.stocksfeedchecker.repository.CompanyRepository;
import com.max.stocksfeedchecker.service.IEXService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class StocksFeedCheckerApplication {

    private final IEXCloudClient iexCloudClient;
    private final IEXService iexService;
    private final CompanyRepository repository;
    private final CompanyJob companyJob;



    public static void main(String[] args) {
        SpringApplication.run(StocksFeedCheckerApplication.class, args);
    }


    @Scheduled(fixedRate = 5000)
    public void start() {
        List<String> symbols = iexCloudClient.getCompaniesSymbols();
        List<CompanyEntity> companies = iexService.getCompaniesData(symbols);
        companyJob.printHighestValueStocks(companies);
        companyJob.printMostResentCompanies(companies);
        companyJob.saveCompanies(companies);

    }


}
