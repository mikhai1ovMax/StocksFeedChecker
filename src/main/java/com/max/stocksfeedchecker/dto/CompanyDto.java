package com.max.stocksfeedchecker.dto;

import com.max.stocksfeedchecker.model.CompanyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;
    private String symbol;
    private String companyName;
    private BigDecimal volume;
    private BigDecimal previousVolume;
    private BigDecimal latestPrice;
    private BigDecimal differenceInCost;

    public static CompanyDto fromCompanyEntity(CompanyEntity companyEntity){
        return CompanyDto.builder()
                .id(companyEntity.getId())
                .symbol(companyEntity.getSymbol())
                .companyName(companyEntity.getCompanyName())
                .volume(companyEntity.getVolume())
                .previousVolume(companyEntity.getPreviousVolume())
                .latestPrice(companyEntity.getLatestPrice())
                .differenceInCost(companyEntity.getDifferenceInCost())
                .build();
    }

    public CompanyEntity toCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setId(id);
        company.setSymbol(symbol);
        company.setCompanyName(companyName);
        company.setVolume(volume);
        company.setPreviousVolume(previousVolume);
        company.setLatestPrice(latestPrice);
        company.setDifferenceInCost(differenceInCost);
        return company;
    }
}
