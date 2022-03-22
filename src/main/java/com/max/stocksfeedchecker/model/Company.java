package com.max.stocksfeedchecker.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Company {
    private String symbol;
    private String companyName;
    private String volume;
    private String previousVolume;
    private String latestPrice;

    @Override
    public String toString() {
        return "Company{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", volume='" + volume + '\'' +
                ", previousVolume='" + previousVolume + '\'' +
                ", latestPrice='" + latestPrice + '\'' +
                '}';
    }
}
