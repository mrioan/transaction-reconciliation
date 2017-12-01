package com.mario.sample.reconciliation.processing;

import com.mario.sample.reconciliation.model.Record;

/**
 * A data holder where records can be added, removed and retrieved.
 */
public interface RecordStore extends Iterable<Record> {

    Record remove(Record record);

    Record get(Record record);

    int getSize();

    /**
     * This is an optional operation. Some instances may not support it.
     * @param record the record to add to this RecordStore.
     * @return the same record that was just added.
     * @throws UnsupportedOperationException if the <tt>add</tt> operation
     *         is not supported by the specific RecordStore implementation.
     */
    Record add(Record record);

}
