package com.max.stocksfeedchecker.service;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.model.Company;
import com.max.stocksfeedchecker.repository.CompanyRepository;
import lombok.SneakyThrows;
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
    private final CompanyRepository repository;
    private final ApplicationContext applicationContext;

    Comparator<Company> comparatorByName = Comparator.comparing(Company::getCompanyName);
    Comparator<Company> comparatorByValue = Comparator.comparing(company -> {
        if (company.getVolume() != null)
            return company.getVolume();
        if (company.getPreviousVolume() != null)
            return company.getPreviousVolume();
        return BigDecimal.ZERO;
    });
    Comparator<Company> comparatorByDifferenceInCost = Comparator.comparing(Company::getDifferenceInCost);


    @Autowired
    public IEXService(IEXCloudClient iexCloudClient, CompanyRepository repository, ApplicationContext applicationContext) {
        this.iexCloudClient = iexCloudClient;
        this.repository = repository;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    public List<Company> getCompaniesData(List<String> symbols) {
        List<IEXCloudClient> clients = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 500; i++) {
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
        Collections.sort(companies, comparatorByValue.reversed());
        log.info("top 5 highest value stocks:");
        log.info(companies.get(0).toString());
        companies.remove(0);
        companies = companies.subList(0, 4);
        Collections.sort(companies, comparatorByName);
        for (Company company :
                companies) {
            log.info(company.toString());
        }
    }

    public void printMostResentCompanies(List<Company> newValues) {
        List<Company> oldValues = repository.findAll();

        for (int i = 0; i < newValues.size() - 1; i++) {
            Company newCompanyData = newValues.get(i);
            Company oldCompanyData = oldValues.stream().filter(x -> x.getCompanyName().equals(newCompanyData.getCompanyName())).findFirst().orElse(null);
            if (oldCompanyData != null)
                newCompanyData.setDifferenceInCost(newCompanyData.getLatestPrice().subtract(oldCompanyData.getLatestPrice()));
        }

        try {
            newValues.sort(comparatorByDifferenceInCost.reversed());
        } catch (NullPointerException e) {
            List<Company> companies = new ArrayList<>();
            for (int i = 0; i < newValues.size() - 1; i++) {
                if (newValues.get(i).getDifferenceInCost() != null) {
                    companies.add(newValues.get(i));
                }
            }
            companies.sort(comparatorByDifferenceInCost.reversed());
            if(companies.size() > 0) {
                log.info("companies that have the greatest change percent in stock value:");
                for (int i = 0; i < companies.size()-1; i++) {
                    log.info(companies.get(i).toString());
                }
            }
        }


        if(newValues.size() > 4){
            log.info("companies that have the greatest change percent in stock value:");
            for (int i = 0; i < 5; i++)
                log.info(newValues.get(i).toString());
        } else {
            for (Company company :
                    newValues) {
                log.info(company.toString());
            }
        }
    }

}
