package com.max.stocksfeedchecker.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "company")
@Data
@RequiredArgsConstructor
@ToString
public class CompanyEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "volume")
    private BigDecimal volume;

    @Column(name = "previous_volume")
    private BigDecimal previousVolume;

    @Column(name = "latest_price")
    private BigDecimal latestPrice;

    @Column(name = "difference_in_cost")
    private BigDecimal differenceInCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity company = (CompanyEntity) o;
        return Objects.equals(symbol, company.symbol) && Objects.equals(companyName, company.companyName) && Objects.equals(volume, company.volume) && Objects.equals(previousVolume, company.previousVolume) && Objects.equals(latestPrice, company.latestPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, companyName, volume, previousVolume, latestPrice);
    }

}
