package com.tutuka.assessment.reconciliation.matching;

import com.tutuka.assessment.reconciliation.filter.FilterOutput;
import com.tutuka.assessment.reconciliation.model.Record;

/**
 * Matching records will be pass through a {@link MatchingConsumer} instance in order to decide what should be done with them.
 *
 * @see MatchingConsumers
 */
@FunctionalInterface
public interface MatchingConsumer {

    void accept(Record record1, Record record2, FilterOutput output);

}