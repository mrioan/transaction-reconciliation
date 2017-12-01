package com.mario.sample.reconciliation.web.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * MultipartFile storage.
 */
public interface StorageService {

    void init();

    void store(MultipartFile file);

    Path load(String filename);

    void delete(String filename);

    void deleteAll();

}