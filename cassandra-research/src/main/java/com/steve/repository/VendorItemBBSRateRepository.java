package com.steve.repository;

import com.mysema.query.types.Predicate;
import com.steve.entity.jpa.JPAVendorItem;
import com.steve.entity.jpa.JPAVendorItemBBSRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;


@Repository
public interface VendorItemBBSRateRepository extends CrudRepository<JPAVendorItemBBSRate, Long> {

    Page<JPAVendorItemBBSRate> findAll(Pageable pageable);

    JPAVendorItemBBSRate findByVendorItemAndFormula(JPAVendorItem jpaVendorItem, int formula);
}
