package com.steve.entity.cassandra;

/**
 * @author Malcolm Qian
 * @since 2015.09.17
 */
public interface QueryHelper {
    interface Constants {
        String CONSISTENCY_LEVEL = "consistency.level";
    }

    interface NamedQueries {
        String CASSANDRA_FIND_ITEM_BY_ITEM_ID = "Cassandra.findItemByItemId";
        String CASSANDRA_FIND_VENDOR_ITEM_BY_VENDOR_ITEM_ID = "Cassandra.findVendorItemByVendorItemId";
        String CASSANDRA_FIND_VENDOR_ITEMS_BY_VENDOR_ID = "Cassandra.findVendorItemsByVendorId";
        String CASSANDRA_FIND_VENDOR_ITEMS_STARTED_IN_PERIOD = "Cassandra.findVendorItemsStartedInPeriod";
        String CASSANDRA_FIND_VENDOR_ITEMS_ENDED_IN_PERIOD = "Cassandra.findVendorItemsEndedInPeriod";
        String CASSANDRA_FIND_VENDOR_BY_VENDOR_ID = "Cassandra.findVendorByVendorId";
        String CASSANDRA_FIND_VARIABLE_BY_NAME = "Cassandra.findVariableByName";
        String CASSANDRA_FIND_CALCULATOR_DETAILS_BY_VENDOR_ITEM_ID = "Cassandra.findCalculatorDetailsByVendorItemId";
        String CASSANDRA_FIND_CALCULATOR_DETAILS_BY_ITEM_ID = "Cassandra.findCalculatorDetailsByItemId";
        String CASSANDRA_FIND_CALCULATOR_DETAILS_BY_VENDOR_ID = "Cassandra.findCalculatorDetailsByVendorId";
        String CASSANDRA_FIND_RECOMMENDATIONS_BY_ITEM_ID = "Cassandra.findRecommendationsByItemId";
        String CASSANDRA_FIND_RECOMMENDATION_BY_PK = "Cassandra.findRecommendationByPK";
    }

    interface Parameters {
        String VENDOR_ITEM_ID = "vendorItemId";
        String VENDOR_ID = "vendorId";
        String ITEM_ID = "itemId";
        String PERIOD_START = "periodStart";
        String PERIOD_END = "periodEnd";
        String VARIABLE_NAME = "varName";
    }
}