package com.mario.sample.reconciliation.model;

/**
 * The pair of records. Used by {@link com.mario.sample.reconciliation.filter.ResemblingRecordFilter} in order to
 * link records that resemble.
 */
public class RecordPair {

    private final Record record1;
    private final Record record2;

    public RecordPair(Record record1, Record record2) {
        if (record1 == null && record2 == null){
            throw new IllegalArgumentException("one of record1 or record2 must not be null.");
        }
        this.record1 = record1;
        this.record2 = record2;
    }

    public Record getRecord1() {
        return record1;
    }

    public Record getRecord2() {
        return record2;
    }
}
