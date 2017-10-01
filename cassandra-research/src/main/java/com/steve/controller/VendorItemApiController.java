package com.steve.controller;

import com.steve.entity.datastax.VendorItem;
import com.steve.service.datastax.CassandraVendorItemService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author stevexu
 * @Since 10/1/17
 */
@Service
@RestController
@RequestMapping("/api")
public class VendorItemApiController {

    @Inject
    CassandraVendorItemService cassandraVendorItemService;

    @RequestMapping(value = "/vendoritem/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<VendorItem> searchStudentById(@PathVariable long id) {
        VendorItem vendorItem = cassandraVendorItemService.findOneByAccessor(id);
        return new ResponseEntity<>(vendorItem, HttpStatus.OK);
    }
}
