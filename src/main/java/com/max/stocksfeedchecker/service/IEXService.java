package com.max.stocksfeedchecker.service;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.model.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
public class IEXService {

    private final IEXCloudClient iexCloudClient;

    private final ApplicationContext applicationContext;

    @Autowired
    public IEXService(IEXCloudClient iexCloudClient, ApplicationContext applicationContext) {
        this.iexCloudClient = iexCloudClient;
        this.applicationContext = applicationContext;
    }

    public List<Company> getCompaniesData(List<String> symbols) throws InterruptedException {
        List<IEXCloudClient> clients = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < symbols.size() - 1; i++) {
            clients.add(applicationContext.getBean(IEXCloudClient.class));
            clients.get(i).setSymbol(symbols.get(i));
        }

        List<Future<Company>> futureCompanies = executor.invokeAll(clients);

        List<Company> companies = new ArrayList<>();
        int notDownloaded = 0;

        for (Future<Company> futureCompany :
                futureCompanies) {
            try {
                companies.add(futureCompany.get());
            } catch (Exception e) {
                notDownloaded++;
            }
        }
        log.info("not downloaded " + notDownloaded + " companies");
        log.info("downloaded " + companies.size() + " companies");

        return companies;
    }

    public void printHighestValueStocks(List<Company> companies) {
        Comparator<Company> comparatorByName = (c1, c2) -> c1.getCompanyName().compareTo(c2.getCompanyName());
        Comparator<Company> comparatorByValue = Comparator.comparing(company -> {
            if (company.getVolume() != null)
                return company.getVolume();
            if (company.getPreviousVolume() != null)
                return company.getPreviousVolume();
            return BigDecimal.ZERO;
        });

        Collections.sort(companies, comparatorByValue.reversed());
        log.info("top 5 highest value stocks");
        log.info(companies.get(0).toString());
        companies.remove(0);
        companies = companies.subList(0, 4);
        Collections.sort(companies, comparatorByName);
        for (Company company :
                companies) {
            log.info(company.toString());
        }
    }


}
