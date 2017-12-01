package com.mario.sample.reconciliation.filter;

import com.mario.sample.reconciliation.matching.RecordMatcher;
import com.mario.sample.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.mario.sample.reconciliation.matching.MatchingConsumers.same;

/**
 * A specific {@link Filter} used to try to detect records that although not really identical they are so similar that can
 * safely be considered as equal. This implementation relies on {@link RecordMatcher} in order to use different strategies
 * to match records.
 * @see com.mario.sample.reconciliation.matching.RecordMatchers
 * @see com.mario.sample.reconciliation.matching.RecordRevisitors
 */
public class SimilarRecordFilter extends RecordMatcherConsumerFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(SimilarRecordFilter.class);

    private final RecordMatcher recordMatcher;

    public SimilarRecordFilter(RecordMatcher recordMatcher) {
        if (recordMatcher == null) {
            throw new IllegalArgumentException("recordMatcher cannot be null");
        }
        this.recordMatcher = recordMatcher;
    }

    @Override
    public void accept(Record record1, Record record2, FilterOutput output) {
        same().accept(record1, record2, output);
    }

    @Override
    public boolean matches(Record recordOne, Record recordTwo) {
        return recordMatcher.matches(recordOne, recordTwo);
    }

}
