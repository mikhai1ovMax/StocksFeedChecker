package com.max.stocksfeedchecker.client;

import com.max.stocksfeedchecker.model.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class IEXCloudClient implements Callable<Company> {

    @Value("${iexcloud.URL}")
    private String URL;

    @Value("${iexcloud.token}")
    private String token;

    private String symbol;



    private final RestTemplate restTemplate = new RestTemplate();


    public List<String> getCompaniesSymbols(){
        List<Company> companies = Arrays.asList(restTemplate.getForObject(URL + "ref-data/symbols?token=" + token, Company[].class));
        List<String> symbols = new ArrayList<>();
        for (Company company :
                companies) {
            symbols.add(company.getSymbol());
        }
        return symbols;
    }


    @Override
    public Company call() throws Exception {
        return restTemplate.getForObject(URL + "stock/" + symbol + "/quote?token=" + token, Company.class);
    }
}
//URL + "ref-data/symbols?token="+ token