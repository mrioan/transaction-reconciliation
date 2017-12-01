package com.mario.sample.reconciliation.matching;

import com.mario.sample.reconciliation.filter.FilterOutput;
import com.mario.sample.reconciliation.model.Record;

/**
 * Matching records will be pass through a {@link MatchingConsumer} instance in order to decide what should be done with them.
 *
 * @see MatchingConsumers
 */
@FunctionalInterface
public interface MatchingConsumer {

    void accept(Record record1, Record record2, FilterOutput output);

}