package com.steve.repository;

import com.mysema.query.types.Predicate;
import com.steve.entity.jpa.JPAVendorItemMessage;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VendorItemMessageRepository extends MyJpaRepository<JPAVendorItemMessage, Long>{

    @Override
    List<JPAVendorItemMessage> findAll(Predicate predicate);

    JPAVendorItemMessage findByVendorItemId(Long vendorItemId);

    @Override
    JPAVendorItemMessage findOne(Predicate predicate);
}
