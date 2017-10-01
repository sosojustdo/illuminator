package com.steve.service.datastax;

import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.steve.entity.datastax.VendorItem;

/**
 * @author stevexu
 * @Since 10/1/17
 */
@Accessor
public interface CassandraVendorItemAccessor {

    @Query("select * from buyboxtest.vendor_items WHERE vendoritemid = :vendoritemid")
    VendorItem getVendorItem(@Param("vendoritemid") long vendoritemid);

}
