package com.max.stocksfeedchecker.service;

import com.max.stocksfeedchecker.client.IEXCloudClient;
import com.max.stocksfeedchecker.dto.CompanyDto;
import com.max.stocksfeedchecker.model.CompanyEntity;
import com.max.stocksfeedchecker.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IEXService {

    private final IEXCloudClient iexCloudClient;
    private final CompanyRepository repository;


    @SneakyThrows
    public List<CompanyEntity> getCompaniesData(List<String> symbols) {
        symbols = symbols.subList(0, 1000);
        List<CompanyEntity> companies = new ArrayList<>();
        symbols.forEach(x -> iexCloudClient.getCompanyDetails(x).thenAccept(y -> companies.add(y.toCompany())));
        return companies;
    }

    public void saveCompanies(List<CompanyEntity> companies) {

        companies.forEach(x -> repository.saveOrUpdate(x));
    }





}
