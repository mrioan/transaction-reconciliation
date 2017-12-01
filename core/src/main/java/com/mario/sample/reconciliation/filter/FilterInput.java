package com.mario.sample.reconciliation.filter;

import com.mario.sample.reconciliation.config.ConfigProperties;

/**
 * Holder for data that will be passed as input to the processing workflow.
 */
public class FilterInput extends FilterData {

    public static final String DEFAULT_KEY_COLUMN_NAME = ConfigProperties.getStringProperty("key.column");

    public static final String FILE1_PATH = "FILE1_PATH";
    public static final String FILE2_PATH = "FILE2_PATH";

    public static final String KEY_COLUMN_NAME = "KEY_COLUMN_NAME";

    public FilterInput() {
        put(KEY_COLUMN_NAME, DEFAULT_KEY_COLUMN_NAME);
    }

}
