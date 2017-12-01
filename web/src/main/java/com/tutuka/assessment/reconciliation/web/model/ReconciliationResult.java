package com.tutuka.assessment.reconciliation.web.model;

import com.tutuka.assessment.reconciliation.model.RecordPair;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

public class ReconciliationResult {

    private final int matchingRecords;
    private final int totalRecordsFile1;
    private final int unmatchedRecordsFile1;
    private final int totalRecordsFile2;
    private final int unmatchedRecordsFile2;

    public int getMatchingRecords() {
        return matchingRecords;
    }

    public int getTotalRecordsFile2() {
        return totalRecordsFile2;
    }

    public int getTotalRecordsFile1() {
        return totalRecordsFile1;
    }

    public int getUnmatchedRecordsFile1() {
        return unmatchedRecordsFile1;
    }

    public int getUnmatchedRecordsFile2() {
        return unmatchedRecordsFile2;
    }

    public List<RecordPair> getRecordPairs() {
        return recordPairs;
    }

    private final List<RecordPair> recordPairs;

    private ReconciliationResult(ReconciliationResult.Builder builder) {
        this.matchingRecords = builder.getMatchingRecords();
        this.totalRecordsFile1 = builder.getTotalRecordsFile1();
        this.unmatchedRecordsFile1 = builder.getUnmatchedRecordsFile1();
        this.totalRecordsFile2 = builder.getTotalRecordsFile2();
        this.unmatchedRecordsFile2 = builder.getUnmatchedRecordsFile2();
        this.recordPairs = Collections.unmodifiableList(builder.getRecordPairs());
    }

    //Builder Class
    public static final class Builder {

        private int matchingRecords;
        private int totalRecordsFile1;
        private int unmatchedRecordsFile1;
        private int totalRecordsFile2;
        private int unmatchedRecordsFile2;

        private List<RecordPair> recordPairs = Collections.emptyList();

        public int getMatchingRecords() {
            return matchingRecords;
        }

        public Builder setMatchingRecords(int matchingRecords) {
            this.matchingRecords = matchingRecords;
            return this;
        }

        public int getTotalRecordsFile1() {
            return totalRecordsFile1;
        }

        public Builder setTotalRecordsFile1(int totalRecordsFile1) {
            this.totalRecordsFile1 = totalRecordsFile1;
            return this;
        }

        public int getTotalRecordsFile2() {
            return totalRecordsFile2;
        }

        public Builder setTotalRecordsFile2(int totalRecordsFile2) {
            this.totalRecordsFile2 = totalRecordsFile2;
            return this;
        }

        public int getUnmatchedRecordsFile1() {
            return unmatchedRecordsFile1;
        }

        public Builder setUnmatchedRecordsFile1(int unmatchedRecordsFile1) {
            this.unmatchedRecordsFile1 = unmatchedRecordsFile1;
            return this;
        }

        public int getUnmatchedRecordsFile2() {
            return unmatchedRecordsFile2;
        }

        public Builder setUnmatchedRecordsFile2(int unmatchedRecordsFile2) {
            this.unmatchedRecordsFile2 = unmatchedRecordsFile2;
            return this;
        }

        public List<RecordPair> getRecordPairs() {
            return recordPairs;
        }

        public Builder setRecordPairs(List<RecordPair> recordPairs) {
            Assert.notNull(recordPairs, "recordPairs cannot be null or empty");
            this.recordPairs = recordPairs;
            return this;
        }

        public ReconciliationResult build() {
            return new ReconciliationResult(this);
        }

    }

}
