package com.max.stocksfeedchecker.client;

import com.max.stocksfeedchecker.dto.CompanyDto;
import com.max.stocksfeedchecker.model.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class IEXCloudClient {

    @Value("${iexcloud.URL}")
    private String URL;

    @Value("${iexcloud.token}")
    private String token;

    private final RestTemplate restTemplate;


    public List<String> getCompaniesSymbols(){
        List<CompanyEntity> companies = Arrays.asList(restTemplate.getForObject(URL + "ref-data/symbols?token=" + token, CompanyEntity[].class));
        return companies.stream().map(CompanyEntity::getSymbol).collect(Collectors.toList());
    }


    public CompletableFuture<CompanyDto> getCompanyDetails(String companySymbol) {
        return CompletableFuture.completedFuture(restTemplate.getForObject(URL + "stock/" + companySymbol + "/quote?token=" + token, CompanyDto.class));
    }

}
