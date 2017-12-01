package com.mario.sample.reconciliation.filter;

import com.mario.sample.reconciliation.config.ConfigProperties;
import com.mario.sample.reconciliation.matching.RecordMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mario.sample.reconciliation.matching.RecordRevisitors.abbreviations;
import static com.mario.sample.reconciliation.matching.RecordRevisitors.ignoreCase;
import static com.mario.sample.reconciliation.matching.RecordRevisitors.ignoreSpaces;

/**
 * Custom workflow to test different configurations other than the default one.
 */
public class CustomCSVProcessingWorkflow extends CSVProcessingWorkflow {

    private CustomCSVProcessingWorkflow(List<Filter> filters) {
        super(filters);
    }

    public static CustomCSVProcessingWorkflow omitResemblingAndAbreviationMatcher() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new CSVLoadingFilter());
        filters.add(new IdenticalRecordRemoverFilter());
        filters.add(new SimilarRecordFilter(RecordMatchers.anyOf(Arrays.asList(ignoreSpaces(), ignoreCase()))));
        return new CustomCSVProcessingWorkflow(filters);
    }

    public static CustomCSVProcessingWorkflow omitResembling() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new CSVLoadingFilter());
        filters.add(new IdenticalRecordRemoverFilter());
        filters.add(new SimilarRecordFilter(RecordMatchers.anyOf(Arrays.asList(ignoreSpaces(), ignoreCase(), abbreviations(ConfigProperties.getListProperty("secondary.columns"))))));
        filters.add(new ReportDataOrganizationFilter());
        return new CustomCSVProcessingWorkflow(filters);
    }
}