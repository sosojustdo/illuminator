package com.steve.illuminator.core.excpetion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xuh18 on 6/12/16.
 */
public class ExceptionHandler {

    public static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public static void handleException(final Exception cause) {
        if (cause instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        } else {
            logger.error("error occurred while execution job",cause);
            throw new RuntimeException(cause);
        }
    }

}
