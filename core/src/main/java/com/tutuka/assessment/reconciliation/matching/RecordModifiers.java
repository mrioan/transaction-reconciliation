package com.tutuka.assessment.reconciliation.matching;

import com.tutuka.assessment.reconciliation.config.ConfigProperties;
import com.tutuka.assessment.reconciliation.exception.HeaderNotFoundException;
import com.tutuka.assessment.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Utility factory class to construct {@link RecordModifier RecordModifiers}
 */
public class RecordModifiers {

    private static final Logger logger = LoggerFactory.getLogger(RecordModifiers.class);

    protected final static String keyColumn = ConfigProperties.getStringProperty("key.column");
    protected final static List<String> caseSensitiveColumns = Collections.unmodifiableList(ConfigProperties.getListProperty("case_sensitive.columns"));

    public static RecordModifier normalizeSpaces() {
        return (record) -> {
            Map<String, Integer> headers = record.getHeader();
            String[] newValues = new String[headers.size()];
            record.getValues().toArray(newValues);
            for (Map.Entry<String, Integer> entry : headers.entrySet()) {
                String key = entry.getKey();
                if (!key.equals(keyColumn)) { //we will not change the case for the primary key
                    newValues[entry.getValue()] = newValues[entry.getValue()].replaceAll("\\s{2,}", " ").trim();
                }
            }
            return new Record(record.getRow(), Arrays.asList(newValues), record.getHeader());
        };
    }

    public static RecordModifier toUpperCase() {
        return (record) -> {
            Map<String, Integer> headers = record.getHeader();
            String[] newValues = new String[headers.size()];
            record.getValues().toArray(newValues);
            for (Map.Entry<String, Integer> entry : headers.entrySet()) {
                String key = entry.getKey();
                //we will not change the case for the primary key or for case sensitive columns
                if (!caseSensitiveColumns.contains(key) && !key.equals(keyColumn)) {
                    newValues[entry.getValue()] = newValues[entry.getValue()].toUpperCase();
                }
            }
            return new Record(record.getRow(), Arrays.asList(newValues), record.getHeader());
        };
    }

    public static RecordModifier addBlankValues() {
        return (record) -> {
            Map<String, Integer> headers = record.getHeader();
            String[] newValues = new String[headers.size()];
            record.getValues().toArray(newValues);
            for (Map.Entry<String, Integer> entry : headers.entrySet()) {
                try {
                    record.get(entry.getKey());
                } catch (HeaderNotFoundException | IllegalStateException e) {
                    newValues[entry.getValue()] = ""; //let's add a blank value for the missing keu
                }
            }
            return new Record(record.getRow(), Arrays.asList(newValues), record.getHeader());
        };
    }

    public static RecordModifier doNothing() {
        return (record) -> record;
    }

}
