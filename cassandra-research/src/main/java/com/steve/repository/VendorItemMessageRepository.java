package com.steve.repository;

import com.mysema.query.types.Predicate;
import com.steve.entity.jpa.JPAVendorItemMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VendorItemMessageRepository extends CrudRepository<JPAVendorItemMessage, Long> {

    JPAVendorItemMessage findByVendorItemId(Long vendorItemId);

}
