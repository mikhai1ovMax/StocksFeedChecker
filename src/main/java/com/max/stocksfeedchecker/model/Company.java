package com.max.stocksfeedchecker.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "company")
@Data
@RequiredArgsConstructor
public class Company {

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

    @Column(name = "avg_total_volume")
    private BigDecimal avgTotalVolume;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(symbol, company.symbol) && Objects.equals(companyName, company.companyName) && Objects.equals(volume, company.volume) && Objects.equals(previousVolume, company.previousVolume) && Objects.equals(avgTotalVolume, company.avgTotalVolume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, companyName, volume, previousVolume, avgTotalVolume);
    }

    @Override
    public String toString() {
        return "Company{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", volume=" + volume +
                ", previousVolume=" + previousVolume +
                ", avgTotalVolume=" + avgTotalVolume +
                '}';
    }
}
