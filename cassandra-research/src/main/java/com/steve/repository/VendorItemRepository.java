package com.steve.repository;

import com.mysema.query.types.Predicate;
import com.steve.entity.jpa.JPAVendorItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author stevexu
 * @Since 10/2/17
 */
@Repository
public interface VendorItemRepository extends MyJpaRepository<JPAVendorItem, Long>{
    @Override
    Page<JPAVendorItem> findAll(Predicate predicate, Pageable pageable);

    @Override
    List<JPAVendorItem> findAll(Predicate predicate);
}

