package com.mario.sample.reconciliation.filter;

import com.mario.sample.reconciliation.config.ConfigProperties;
import com.mario.sample.reconciliation.exception.ReconciliationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CSVProcessingWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(CSVProcessingWorkflow.class);

    private final List<Filter> filters;

    private final static String keyColumn = ConfigProperties.getStringProperty("key.column");
    private final static List<String> secondaryColumns = ConfigProperties.getListProperty("secondary.columns");

    CSVProcessingWorkflow(List<Filter> filters) {
        this.filters = Collections.unmodifiableList(filters);
        logger.info("CSV processing workflow is configured to run with the following filters {}.",
                filters.stream()
                .map( f -> f.getClass().getSimpleName() )
                .collect( Collectors.joining( ", " ) ));
    }

    public static CSVProcessingWorkflow instantiate() {
        //not public code
        return null;
    }

    public FilterOutput process(File file1, File file2) throws ReconciliationException {
        //not public code
        return null;
    }

}
