package com.mario.sample.reconciliation.matching;

import java.util.List;

/**
 * A revisitor has the opportunity to virtually apply modifications to a record before carrying out an actual matching operation
 * <p>Revisitors must implement both the {@link RecordModifier} and the {@link FieldMatcher} interface.</p>
 */
public class RecordRevisitors {

    public static RecordRevisitor ignoreSpaces() {
        return SimpleRecordRevisitor.simple(RecordModifiers.normalizeSpaces(), FieldMatchers.equals());
    }

    public static RecordRevisitor ignoreCase() {
        return SimpleRecordRevisitor.simple(RecordModifiers.toUpperCase(), FieldMatchers.ignoreCase());
    }

    public static RecordRevisitor abbreviations(List<String> columnsToMatch) {
        return SimpleRecordRevisitor.simple(RecordModifiers.doNothing(), FieldMatchers.abbreviations(columnsToMatch));
    }

    public static RecordRevisitor equal(String columnToMatch) {
        return SimpleRecordRevisitor.simple(RecordModifiers.doNothing(), FieldMatchers.equalColumnValue(columnToMatch));
    }

}

