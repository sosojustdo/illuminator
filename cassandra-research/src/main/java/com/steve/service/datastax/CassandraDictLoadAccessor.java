package com.steve.service.datastax;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.QueryParameters;

import brandnormload.entities.ItemBrand;


/**
 * @author stevexu
 * @Since 11/3/17
 */
@Accessor
public interface CassandraDictLoadAccessor {

    @Query("select * from buyboxtest.item_brand")
    @QueryParameters(fetchSize = 10000)
    Result<ItemBrand> getItemBrands();

}
