package com.tutuka.assessment.reconciliation.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic thread-safe class to hold information passing between filters during workflow execution.
 * @see FilterInput
 * @see FilterOutput
 */
public abstract class FilterData {

    private final Map<String, Object> properties = new HashMap<>();

    public void put(String key, Object value) {
        synchronized (properties) {
            this.properties.put(key, value);
        }
    }

    public Integer getInteger(String key) {
        return getProperty(key, Integer.class);
    }

    public String getString(String key) {
        return getProperty(key, String.class);
    }

    @SuppressWarnings(value = "unchecked")
    private <T> T getProperty(String key, Class<T> clazz) {
        Object value;
        synchronized (properties) {
            value = properties.get(key);
        }
        if (value == null) {
            return null;
        }

        if (clazz.isInstance(value)) {
            return (T) value;
        }
        String msg = String.format("'%s' property value type does not match the specified type. Specified type: %s. " +
            "Existing type: %s", key, clazz, value.getClass().getName());
        throw new IllegalArgumentException(msg);
    }

}
