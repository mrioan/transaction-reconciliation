package com.tutuka.assessment.reconciliation.matching;

import com.tutuka.assessment.reconciliation.model.Record;

/**
 * Records can undergo a wide variety of matching operations. Such operations must implement this interface in order to be
 * supported by the processing workflow.
 */
@FunctionalInterface
public interface RecordMatcher {

    /**
     * Executes the actual matching operation against the given records.
     * @param recordOne one of the records to be evaluated.
     * @param recordTwo the other record to evaluate.
     * @return <code>true</code> if <var>recordOne</var> matches <var>recordTwo</var>, otherwise <code>false</code>.
     */
    boolean matches(Record recordOne, Record recordTwo);

}
