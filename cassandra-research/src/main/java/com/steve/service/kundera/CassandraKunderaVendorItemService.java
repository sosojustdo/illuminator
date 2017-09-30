package com.steve.service.kundera;

/**
 * @author stevexu
 * @Since 9/27/17
 */

import com.impetus.client.cassandra.common.CassandraConstants;
import com.steve.entity.kundera.QueryHelper;
import com.steve.entity.kundera.VendorItem;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceProperty;
import java.util.List;

@Service
public class CassandraKunderaVendorItemService {

    @PersistenceContext(
            unitName = "buybox_cassandra",
            type = PersistenceContextType.EXTENDED,
            properties = {
                    @PersistenceProperty(name = CassandraConstants.CQL_VERSION, value = CassandraConstants.CQL_VERSION_3_0),
                    @PersistenceProperty(name = QueryHelper.Constants.CONSISTENCY_LEVEL, value = "ALL")
            })
    private EntityManager entityManagerConsistencyAll;

    @PersistenceContext(
            unitName = "buybox_cassandra",
            type = PersistenceContextType.EXTENDED,
            properties = {
                    @PersistenceProperty(name = CassandraConstants.CQL_VERSION, value = CassandraConstants.CQL_VERSION_3_0),
                    @PersistenceProperty(name = QueryHelper.Constants.CONSISTENCY_LEVEL, value = "ONE")
            })
    private EntityManager entityManagerConsistencyOne;

    @PersistenceContext(
            unitName = "buybox_cassandra",
            type = PersistenceContextType.EXTENDED,
            properties = {
                    @PersistenceProperty(name = CassandraConstants.CQL_VERSION, value = CassandraConstants.CQL_VERSION_3_0),
                    @PersistenceProperty(name = QueryHelper.Constants.CONSISTENCY_LEVEL, value = "QUORUM")
            })
    private EntityManager entityManagerConsistencyQuroum;

    public VendorItem findOne(Long vendorItemId) {
        List<VendorItem> cvis = entityManagerConsistencyQuroum
                .createNamedQuery(QueryHelper.NamedQueries.CASSANDRA_FIND_VENDOR_ITEM_BY_VENDOR_ITEM_ID, VendorItem.class)
                .setParameter(QueryHelper.Parameters.VENDOR_ITEM_ID, vendorItemId)
                .getResultList();
        if(cvis.size() > 0){
            return cvis.get(0);
        }
        return null;
    }

    public void save(VendorItem vendorItem) {
        entityManagerConsistencyQuroum.persist(vendorItem);
    }


}

