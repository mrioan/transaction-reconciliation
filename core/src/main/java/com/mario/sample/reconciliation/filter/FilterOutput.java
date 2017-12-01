package com.mario.sample.reconciliation.filter;

import com.mario.sample.reconciliation.model.RecordPair;
import com.mario.sample.reconciliation.processing.RecordStore;
import com.mario.sample.reconciliation.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Holder for data that will be passed as output to the processing workflow.
 */
public class FilterOutput extends FilterData {

    public static final String MATCHING_RECORDS = "MATCHING_RECORDS";

    public static final String TOTAL_RECORDS_FILE1 = "TOTAL_RECORDS_FILE1";
    public static final String UNMATCHED_RECORDS_FILE1 = "UNMATCHED_RECORDS_FILE1";

    public static final String TOTAL_RECORDS_FILE2 = "TOTAL_RECORDS_FILE2";
    public static final String UNMATCHED_RECORDS_FILE2 = "UNMATCHED_RECORDS_FILE2";

    public static final String RECORD_PAIRS = "RECORD_PAIRS";

    private RecordStore file1RecordStore;
    private RecordStore file2RecordStore;
    private List<RecordPair> recordPairs = new ArrayList<>();

    public FilterOutput() {
        put(MATCHING_RECORDS, 0);
        put(UNMATCHED_RECORDS_FILE1, 0);
        put(UNMATCHED_RECORDS_FILE2, 0);
        put(RECORD_PAIRS, new ArrayList<RecordPair>());
    }

    public RecordStore getFile1RecordStore() {
        return file1RecordStore;
    }

    public void setFile1RecordStore(RecordStore file1RecordStore) {
        this.file1RecordStore = file1RecordStore;
    }

    public RecordStore getFile2RecordStore() {
        return file2RecordStore;
    }

    public void setFile2RecordStore(RecordStore file2RecordStore) {
        this.file2RecordStore = file2RecordStore;
    }

    public synchronized void addRecordPair(Record record1, Record record2) {
        this.recordPairs.add(new RecordPair(record1, record2));
    }

    /**
     * Returns a copy.
     */
    public List<RecordPair> getRecordPairs() {
        return new ArrayList<>(recordPairs);
    }

    public void setRecordPairs(List<RecordPair> recordPairs) {
        this.recordPairs = recordPairs;
    }

}
