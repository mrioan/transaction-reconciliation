package com.mario.sample.reconciliation.processing;

import com.mario.sample.reconciliation.exception.HeaderNotFoundException;
import com.mario.sample.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The main placeholder for the records while they are being processed. This implementation is thread-safe, having locks
 * to critical sections.
 */
public abstract class AbstractCSVRecordStore implements RecordStore {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCSVRecordStore.class);

    private final RecordMap recordMap = new RecordMap();
    final String key;
    private AtomicInteger size = new AtomicInteger(0);
    final Lock readLock;
    final Lock writeLock;

    public AbstractCSVRecordStore(String keyColumn) {
        if (keyColumn == null ){
            throw new IllegalArgumentException("keyColumn cannot be null.");
        }
        this.key = keyColumn;
        ReadWriteLock rwl = new ReentrantReadWriteLock();
        this.readLock = rwl.readLock();
        this.writeLock = rwl.writeLock();
    }

    @Override
    public Record get(Record record) {
        String keyValue = getKeyValue(record);
        readLock.lock();
        try {
            List<Record> records = recordMap.get(keyValue);
            if (records == null) {
                return null;
            }
            for (Record r : records) {
                if (r.equals(record)) {
                    return record;
                }
            }
        } finally {
            readLock.unlock();
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.size.get();
    }

    @Override
    public Record remove(Record record) {
        String keyValue = getKeyValue(record);
        writeLock.lock();
        try {
            List<Record> records = this.recordMap.get(keyValue);
            if (records != null) {
                records.remove(record);
                size.decrementAndGet();
                if (records.size() == 0) {
                    this.recordMap.remove(keyValue);
                }
                return record;
            }
        } finally {
            writeLock.unlock();
        }
        return null;
    }

    @Override
    public Record add(Record record) {
        String keyValue = getKeyValue(record);
        writeLock.lock();
        try {
            List<Record> records = this.recordMap.computeIfAbsent(keyValue, (recordKey) -> new ArrayList<>());
            records.add(record);
            this.recordMap.put(keyValue, records);
            this.size.incrementAndGet();
            return record;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Iterator<Record> iterator() {
        List<Record> records = new ArrayList<>();
        readLock.lock();
        try {
            //we need to flatten the multi value map
            Iterator<List<Record>> recordList = this.recordMap.values().iterator();
            while(recordList.hasNext()) {
                records.addAll(recordList.next());
            }
        } finally {
            readLock.unlock();
        }
        return records.iterator();
    }

    private String getKeyValue(Record record) {
        if (record == null) {
            throw new IllegalArgumentException("record cannot be null.");
        }
        String keyValue;
        try {
            keyValue = record.get(key);
        } catch (HeaderNotFoundException | IllegalStateException e) {
            String message = String.format("The required '%s' is not present in record '%s'.", this.key, record);
            logger.debug(message);
            throw new IllegalArgumentException(message);
        }
        return keyValue;
    }

}
