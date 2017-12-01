package com.mario.sample.reconciliation.matching;

import com.mario.sample.reconciliation.filter.FilterOutput;

/**
 * Utility factory class to construct {@link MatchingConsumer MatchingConsumers}
 */
public class MatchingConsumers {

    /**
     * This {@link MatchingConsumer} is used when the matching functionality has managed to find 2 records which were considered as <b>similar</b>.
     * This means that we need to sum 1 to the matching count and we must also decrease 1 from the unmatched count for both file #1 and file #2.
     * @return a {@link MatchingConsumer} that will manipulate the output to reflect that a matching operation has succeeded.
     */
    public static MatchingConsumer same() {
        return (record1, record2, output) -> {
            output.put(FilterOutput.MATCHING_RECORDS, output.getInteger(FilterOutput.MATCHING_RECORDS) + 1);
            output.put(FilterOutput.UNMATCHED_RECORDS_FILE1, output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE1) - 1);
            output.put(FilterOutput.UNMATCHED_RECORDS_FILE2, output.getInteger(FilterOutput.UNMATCHED_RECORDS_FILE2) - 1);
        };
    }

    /**
     * This {@link MatchingConsumer} indicates that the matching functionality has managed to find 2 records which were considered as <b>resembling</b>.
     * This means that these 2 records need to be linked so they can be manually reconciled later on.
     * @return a {@link MatchingConsumer} that will link 2 records in order to denote that they resemble and therefore they should be manually
     * compared.
     */
    public static MatchingConsumer resembling() {
        return (record1, record2, output) -> output.addRecordPair(record1, record2);
    }

}
