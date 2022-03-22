package com.max.stocksfeedchecker.service;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.model.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
public class IEXService {

    private IEXCloudClient iexCloudClient;

    private final ApplicationContext applicationContext;

    @Autowired
    public IEXService(IEXCloudClient iexCloudClient, ApplicationContext applicationContext) {
        this.iexCloudClient = iexCloudClient;
        this.applicationContext = applicationContext;
    }

    public List<Company> getAllCompanyData() throws ExecutionException, InterruptedException {
        List<String> symbols = iexCloudClient.getCompaniesSymbols();

        List<IEXCloudClient> clients = new ArrayList<>();

        for (String symbol :
                symbols) {
            clients.add(applicationContext.getBean(IEXCloudClient.class));
        }


        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Company>> futures = executor.invokeAll(clients);

        List<Company> companies = new ArrayList<>();

        for (Future<Company> companyFuture :
                futures) {
            companies.add(companyFuture.get());
        }

        return companies;
    }
}
