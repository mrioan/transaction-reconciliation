package com.tutuka.assessment.reconciliation.filter;

import com.tutuka.assessment.reconciliation.matching.RecordMatcher;
import com.tutuka.assessment.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tutuka.assessment.reconciliation.matching.MatchingConsumers.resembling;

/**
 * A specific {@link Filter} used to try to find records that somehow resemble. This implementation relies on
 * {@link RecordMatcher} in order to use different strategies to match records. Once two records match for resemblance
 * then they are linked so they can later be manually reconciled by the user.
 */
public class ResemblingRecordFilter extends RecordMatcherConsumerFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResemblingRecordFilter.class);

    private final RecordMatcher recordMatcher;

    public ResemblingRecordFilter(RecordMatcher recordMatcher) {
        if (recordMatcher == null) {
            throw new IllegalArgumentException("recordMatcher cannot be null");
        }
        this.recordMatcher = recordMatcher;
    }

    @Override
    public void accept(Record record1, Record record2, FilterOutput output) {
        resembling().accept(record1, record2, output);
    }

    @Override
    public boolean matches(Record recordOne, Record recordTwo) {
        return recordMatcher.matches(recordOne, recordTwo);
    }
}
