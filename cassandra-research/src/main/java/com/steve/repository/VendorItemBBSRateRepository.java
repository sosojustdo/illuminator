package com.steve.repository;

import com.mysema.query.types.Predicate;
import com.steve.entity.jpa.JPAVendorItemBBSRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;


@Repository
public interface VendorItemBBSRateRepository extends MyJpaRepository<JPAVendorItemBBSRate, Long> {
    @Override
    Page<JPAVendorItemBBSRate> findAll(Predicate predicate, Pageable pageable);

    @Override
    JPAVendorItemBBSRate findOne(Predicate predicate);
}
