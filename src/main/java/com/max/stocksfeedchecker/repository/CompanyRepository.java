package com.max.stocksfeedchecker.repository;

import com.max.stocksfeedchecker.model.CompanyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends CrudRepository<CompanyEntity, Long> {
    @Override
    List<CompanyEntity> findAll();

    CompanyEntity getByCompanyName(String name);
    boolean existsByCompanyName(String name);
}
