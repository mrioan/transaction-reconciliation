package com.mario.sample.reconciliation.web.mapper;

import com.mario.sample.reconciliation.filter.FilterOutput;
import com.mario.sample.reconciliation.web.model.ReconciliationResult;

public class FilterOutputMapper {

    public static ReconciliationResult toReconciliationResult(FilterOutput filterOutput) {
        ReconciliationResult.Builder builder = new ReconciliationResult.Builder();

        builder.setMatchingRecords(filterOutput.getInteger(FilterOutput.MATCHING_RECORDS))
                .setTotalRecordsFile1(filterOutput.getInteger(FilterOutput.TOTAL_RECORDS_FILE1))
                .setTotalRecordsFile2(filterOutput.getInteger(FilterOutput.TOTAL_RECORDS_FILE2))
                .setUnmatchedRecordsFile1(filterOutput.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1))
                .setUnmatchedRecordsFile2(filterOutput.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2))
                .setRecordPairs(filterOutput.getRecordPairs());

        return builder.build();
    }

}
