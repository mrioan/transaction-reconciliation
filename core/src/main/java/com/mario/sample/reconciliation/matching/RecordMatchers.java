package com.mario.sample.reconciliation.matching;

import java.util.List;

public class RecordMatchers {

    public static RecordMatcher anyOf(List<RecordRevisitor> recordRevisitors) {
        return AnyOf.anyOf(recordRevisitors);
    }

    public static RecordMatcher simple(FieldMatcher fieldMatcher) {
        return SimpleRecordMatcher.simple(fieldMatcher);
    }

}
