package com.max.stocksfeedchecker.repository;

import com.max.stocksfeedchecker.model.CompanyEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CompanyRepository extends CrudRepository<CompanyEntity, Long> {
    @Override
    List<CompanyEntity> findAll();

    CompanyEntity getByCompanyName(String name);

    boolean existsByCompanyName(String name);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO `stocks_feed_checker`.`company` (`company_name`, `cost_change`, `latest_price`, " +
                    "`previous_volume`, `symbol`, `volume`) " +
                    "VALUES (:#{#company.companyName}, :#{#company.change}, :#{#company.latestPrice}, " +
                    ":#{#company.previousVolume}, :#{#company.symbol}, :#{#company.volume}) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "`cost_change` = :#{#company.change}, `latest_price` = :#{#company.latestPrice}, " +
                    "`previous_volume` = :#{#company.previousVolume}, `volume` = :#{#company.volume} "
                    )
    void saveOrUpdate(@Param("company") CompanyEntity company);
}
