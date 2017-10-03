package com.steve.service.jpa;

import com.steve.entity.jpa.JPAVendorItem;
import com.steve.entity.jpa.JPAVendorItemBBSRate;
import com.steve.repository.VendorItemBBSRateRepository;
import com.steve.repository.VendorItemMessageRepository;
import com.steve.repository.VendorItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author stevexu
 * @Since 10/2/17
 */
@Service
@Slf4j
public class JPAVendorItemService {

    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @Autowired
    @Qualifier("buyboxTransactionManager")
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    VendorItemRepository vendorItemRepository;

    @Autowired
    VendorItemBBSRateRepository vendorItemBBSRateRepository;

    @Autowired
    VendorItemMessageRepository vendorItemMessageRepository;

    public void testJPAVendorItem(int rotation){
        StopWatch watch = new StopWatch();
        watch.start();
        for(int i=0; i<=rotation; i++){
            final int index = i;
            vendorItemMessageRepository.findByVendorItemId(6000000000L + i);
            transactionTemplate.execute(transactionStatus -> {
                JPAVendorItem jpaVendorItem = vendorItemRepository.findOne(6000000000L + index);
                if(jpaVendorItem == null){
                    jpaVendorItem = new JPAVendorItem(6000000000L + index, 5000L, 10000L, "A0000128",
                                                      false, false, false, false, new Date(), new Date(),
                                                      new Date(), new Date());
                    vendorItemRepository.save(jpaVendorItem);
                    JPAVendorItemBBSRate jpaVendorItemBBSRate = new JPAVendorItemBBSRate();
                    jpaVendorItemBBSRate.setVendorItem(jpaVendorItem);
                    jpaVendorItemBBSRate.setFormula(1);
                    jpaVendorItemBBSRate.setRate(new BigDecimal(20000));
                    jpaVendorItemBBSRate.setCreatedAt(new Date());
                    jpaVendorItemBBSRate.setModifiedAt(new Date());
                    vendorItemBBSRateRepository.save(jpaVendorItemBBSRate);
                }
                else{
                    jpaVendorItem.setModifiedAt(new Date());
                    vendorItemRepository.save(jpaVendorItem);
                    JPAVendorItemBBSRate jpaVendorItemBBSRate = vendorItemBBSRateRepository.findByVendorItemAndFormula(jpaVendorItem, 1);
                    jpaVendorItemBBSRate.setRate(new BigDecimal(jpaVendorItemBBSRate.getRate().doubleValue() +1));
                    vendorItemBBSRateRepository.save(jpaVendorItemBBSRate);
                }
                return null;
            });
        }
        watch.stop();
        log.info("process vendor items take {} ms", watch.getTime());
    }

}
