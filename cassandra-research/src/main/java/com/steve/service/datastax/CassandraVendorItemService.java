package com.steve.service.datastax;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.steve.entity.datastax.VendorItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.Date;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

/**
 * @author stevexu
 * @Since 9/30/17
 */
@Service
@Slf4j
public class CassandraVendorItemService {

    private Session session;

    private MappingManager manager;

    private Mapper<VendorItem> mapper;

    @Inject
    private CassandraRepository cassandraRepository;

    @PostConstruct
    public void init() {
        try {
            session = cassandraRepository.getSession();
            manager = new MappingManager(session);
            mapper = manager.mapper(VendorItem.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initiate vendoritemService", e);
        }
    }

    public VendorItem findOne(Long vendorItemId) {
        Result<VendorItem> result;
        Statement statement = QueryBuilder
                .select()
                .from("buyboxtest", "vendor_items")
                .where(eq("vendoritemid", vendorItemId));
        statement.setConsistencyLevel(ConsistencyLevel.QUORUM);
        try {
            ResultSet resultSet = session.execute(statement);
            result = mapper.map(resultSet);
            return result.one();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(VendorItem vendorItem) {
        mapper.save(vendorItem);
    }

    public void testDatastaxCassandra(int rotation){
        StopWatch watch = new StopWatch();
        watch.start();
        for(int i=0; i<=rotation; i++){
           /* VendorItem vendorItem = new VendorItem(3000000000L + i, 5000L, 10000L, "A00010028", new Date(), new Date(), false,
                                                                                                     false, new BigDecimal(30000.00), false, null, false, new Date(), new Date(),
                                                                                                     new Date(), false);
            save(vendorItem);*/
            findOne(3000000000L+i);
        }
        watch.stop();
        log.info("process vendor items take {} ms", watch.getTime());
    }

}
