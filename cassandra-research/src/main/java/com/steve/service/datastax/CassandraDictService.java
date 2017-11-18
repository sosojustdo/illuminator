package com.steve.service.datastax;

import brandnormload.entities.ItemBrand;
import brandnormload.entities.RedisBrand;
import brandnormload.entities.RedisCategoryBrand;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.steve.entity.datastax.VendorItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author stevexu
 * @Since 11/4/17
 */
@Service
@Slf4j
public class CassandraDictService {

    private Session session;

    private MappingManager manager;


    @Inject
    private CassandraRepository cassandraRepository;

    @PostConstruct
    public void init() {
        try {
            session = cassandraRepository.getSession();
            manager = new MappingManager(session);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initiate vendoritemService", e);
        }
    }

    public void process() {
        CassandraDictLoadAccessor viAccessor = manager.createAccessor(CassandraDictLoadAccessor.class);
        Result<ItemBrand> itemBrandsRecords = viAccessor.getItemBrands();
        List<ItemBrand> itemBrands = new ArrayList<>();
        for (ItemBrand itemBrand : itemBrandsRecords) {
            itemBrands.add(itemBrand);
        }

        List<ItemBrand> itemBrandList = new ArrayList<>();
        Map<Integer, Set<RedisBrand>> catBrands = new HashMap<>();

        Set<String> brands = new HashSet<>();

        for (ItemBrand itemBrand : itemBrands) {
            brands.add(itemBrand.getOriginalbrand());
        }

        Map<String, Integer> brandMap = new HashMap<>();
        int index = 0;
        for (String brand : brands) {
            brandMap.put(brand, index++);
        }

        for (ItemBrand itemBrand : itemBrands) {
            itemBrandList.add(itemBrand);
            String cateCode = itemBrand.getCategorycode().split(",")[0];
            if (itemBrand.getOriginalbrand() != null) {
                RedisBrand redisBrand = new RedisBrand();
                redisBrand.setName(itemBrand.getOriginalbrand());
                redisBrand.setId(Long.valueOf(brandMap.get(itemBrand.getOriginalbrand())));
                redisBrand.setSynonyms(Sets.newHashSet(itemBrand.getOriginalbrand()));
                if (catBrands.get(cateCode) == null) {
                    catBrands.put(Integer.parseInt(cateCode), Sets.newHashSet(redisBrand));
                } else {
                    catBrands.get(cateCode).add(redisBrand);
                }
            }
        }


        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(200));
        config.setMinIdle(Integer.valueOf(20));
        config.setMaxIdle(Integer.valueOf(40));
        config.setMaxWaitMillis(Long.valueOf(20000));
        JedisPool pool = new JedisPool(config, "127.0.0.1", 16379);

        Jedis jedis = pool.getResource();

        ObjectMapper objectMapper = new ObjectMapper();

        catBrands.entrySet().forEach(c -> {
            RedisCategoryBrand redisCategoryBrand = new RedisCategoryBrand();
            redisCategoryBrand.setId(c.getKey());
            redisCategoryBrand.setBrands(c.getValue());
            try {
                jedis.set("cat:"+c.getKey(), objectMapper.writeValueAsString(redisCategoryBrand));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

}
