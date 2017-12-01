package com.tutuka.assessment.reconciliation.matching;

import com.tutuka.assessment.reconciliation.model.Record;

/**
 * Carries out specific changes to the given record. The changes are not really applied to the Record since it is immutable.
 * A new instances is returned.
 */
@FunctionalInterface
public interface RecordModifier {

    /**
     * Given a <val>record</val>, creates a new copy, modifies it and finally is returned.
     * @param record the record whose data will be virtually modified.
     * @return a copied modified version of <val>record</val>
     */
    Record modify(Record record);

}
