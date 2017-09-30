package com.steve.service.datastax;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.steve.entity.datastax.VendorItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author stevexu
 * @Since 9/30/17
 */
@Service
public class CassandraVendorItemService {

    private Session session;

    private MappingManager manager;

    private Mapper<VendorItem> mapper;

    /*public VendorItem findOne(Long vendorItemId) {

    }*/

    public void save(VendorItem vendorItem) {

    }

}
