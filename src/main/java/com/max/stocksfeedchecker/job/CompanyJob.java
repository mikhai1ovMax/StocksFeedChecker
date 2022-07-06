package com.max.stocksfeedchecker.job;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.model.CompanyEntity;
import com.max.stocksfeedchecker.repository.CompanyRepository;
import com.max.stocksfeedchecker.service.IEXService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyJob {

    private final IEXCloudClient iexCloudClient;
    private final IEXService iexService;
    private final CompanyRepository repository;

    Comparator<CompanyEntity> comparatorByName = Comparator.comparing(CompanyEntity::getCompanyName);
    Comparator<CompanyEntity> comparatorByValue = Comparator.comparing(company -> {
        if (company.getVolume() != null)
            return company.getVolume();
        if (company.getPreviousVolume() != null)
            return company.getPreviousVolume();
        return BigDecimal.ZERO;
    });
//    Comparator<CompanyEntity> comparatorByChange = Comparator.comparing(CompanyEntity::getChange);
    Comparator<CompanyEntity> comparatorByChange = Comparator.comparing(
            company -> {
                if(company.getChange() != null)
                    return company.getChange();
                return BigDecimal.ZERO;
            }
);


    @Scheduled(cron = "0/5 * * * * *")
    public void start() {
        List<String> symbols = iexCloudClient.getCompaniesSymbols();
        List<CompanyEntity> companies = iexService.getCompaniesData(symbols);
        printHighestValueStocks(companies);
        printMostResentCompanies(companies);
        iexService.saveCompanies(companies);

    }




    public void printHighestValueStocks(List<CompanyEntity> companies) {
        companies.sort(comparatorByValue.reversed());
        log.info("top 5 highest value stocks:");
        log.info(companies.get(0).toString());
        companies.stream().skip(1).limit(4).sorted(comparatorByName).forEach(x -> log.info(x.toString()));
    }

    public void printMostResentCompanies(List<CompanyEntity> companies) {

        log.info("companies that have the greatest change percent in stock value:");
        companies.stream().sorted(comparatorByChange.reversed()).limit(5).forEach(x -> log.info(x.toString()));
        log.info("");
    }
}
