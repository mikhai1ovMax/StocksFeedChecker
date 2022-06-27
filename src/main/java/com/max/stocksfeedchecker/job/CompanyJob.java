package com.max.stocksfeedchecker.job;

import com.max.stocksfeedchecker.model.CompanyEntity;
import com.max.stocksfeedchecker.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CompanyJob {

    private final CompanyRepository repository;
    Comparator<CompanyEntity> comparatorByName = Comparator.comparing(CompanyEntity::getCompanyName);
    Comparator<CompanyEntity> comparatorByValue = Comparator.comparing(company -> {
        if (company.getVolume() != null)
            return company.getVolume();
        if (company.getPreviousVolume() != null)
            return company.getPreviousVolume();
        return BigDecimal.ZERO;
    });
    Comparator<CompanyEntity> comparatorByDifferenceInCost = Comparator.comparing(CompanyEntity::getDifferenceInCost);

    //    @Scheduled(cron = "5, *, *, *, *, *")
    public void saveCompanies(List<CompanyEntity> companies) {
        for (int i = 0; i < companies.size(); i++) {
            if (repository.existsByCompanyName(companies.get(i).getCompanyName())) {
                companies.get(i).setId(repository.getByCompanyName(companies.get(i).getCompanyName()).getId());
            }
            repository.save(companies.get(i));

        }

    }

    public void printData() {

    }

    public void printHighestValueStocks(List<CompanyEntity> companies) {
        Collections.sort(companies, comparatorByValue.reversed());
        System.out.println("top 5 highest value stocks:");
        System.out.println(companies.get(0).toString());

        List<CompanyEntity> companies1 = new ArrayList<>(companies);
        companies1.remove(0);
        Collections.sort(companies1, comparatorByName);
        companies1 = companies1.subList(0, 4);
        for (CompanyEntity company :
                companies1) {
            System.out.println(company.toString());
        }
    }

    public void printMostResentCompanies(List<CompanyEntity> companies) {

        List<CompanyEntity> newValues = new ArrayList<>(companies);
        List<CompanyEntity> oldValues = repository.findAll();

        for (int i = 0; i < newValues.size() - 1; i++) {
            CompanyEntity newCompanyData = newValues.get(i);
            CompanyEntity oldCompanyData = oldValues.stream().filter(x -> x.getCompanyName().equals(newCompanyData.getCompanyName())).findFirst().orElse(null);
            if (oldCompanyData != null) {
                BigDecimal differenceInCost = newCompanyData.getLatestPrice().subtract(oldCompanyData.getLatestPrice());
                if (differenceInCost.compareTo(BigDecimal.ZERO) < 0)
                    differenceInCost = differenceInCost.multiply(BigDecimal.valueOf(0 - 1));
                newValues.get(i).setDifferenceInCost(differenceInCost);
            }
        }

        for (int i = 0; i < newValues.size(); i++) {
            if (newValues.get(i).getDifferenceInCost() == null) {
                newValues.remove(i);
                i--;
            }
        }

        if (newValues.size() > 1)
            newValues.sort(comparatorByDifferenceInCost.reversed());

        System.out.println("companies that have the greatest change percent in stock value:");
        if (newValues.size() > 4) {
            for (int i = 0; i < 5; i++)
                System.out.println(newValues.get(i).toString());
        } else {
            for (CompanyEntity company :
                    newValues) {
                System.out.println(company.toString());
            }
        }
    }
}
