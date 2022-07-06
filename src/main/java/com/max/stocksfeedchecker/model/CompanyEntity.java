package com.max.stocksfeedchecker.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "company")
@Data
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
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

    @Column(name = "cost_change")
    private BigDecimal change;

}
