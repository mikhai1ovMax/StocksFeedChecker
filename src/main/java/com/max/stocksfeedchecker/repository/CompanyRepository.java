package com.max.stocksfeedchecker.repository;

import com.max.stocksfeedchecker.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    @Override
    List<Company> findAll();
}
