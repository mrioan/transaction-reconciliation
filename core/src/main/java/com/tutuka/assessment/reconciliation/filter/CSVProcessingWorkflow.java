package com.tutuka.assessment.reconciliation.filter;

import com.tutuka.assessment.reconciliation.config.ConfigProperties;
import com.tutuka.assessment.reconciliation.exception.FilterException;
import com.tutuka.assessment.reconciliation.exception.ReconciliationException;
import com.tutuka.assessment.reconciliation.matching.RecordMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.tutuka.assessment.reconciliation.matching.RecordRevisitors.*;

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
