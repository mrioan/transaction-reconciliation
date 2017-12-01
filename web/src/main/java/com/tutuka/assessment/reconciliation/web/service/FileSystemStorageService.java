package com.tutuka.assessment.reconciliation.web.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.tutuka.assessment.reconciliation.web.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of {@link StorageService} that stores files in the file system.
 */
public class FileSystemStorageService implements StorageService {

    private final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(String storageDir) {
        this.rootLocation = Paths.get(storageDir);
        this.init();
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (filename.contains("..")) {
                // This is a security check
                String msg = "Cannot store file with relative path outside current directory " + filename;
                logger.error(msg);
                throw new StorageException(msg);
            }
            Path filePath = this.rootLocation.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Storing file: " + filePath);
        }
        catch (IOException e) {
            String msg = "Failed to store file " + filename;
            logger.error(msg);
            throw new StorageException(msg, e);
        }
    }

    @Override
    public Path load(String filename) {
        if (filename.contains("..")) {
            // This is a security check
            String msg = "Cannot load file with relative path outside current directory " + filename;
            logger.error(msg);
            throw new StorageException(msg);
        }
        Path filePath = rootLocation.resolve(filename);
        logger.info("Retrieving file path: " + filePath);
        return filePath;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void delete(String filename) {
        try {
            boolean deleted = Files.deleteIfExists(this.load(filename));
            if (deleted) {
                logger.debug("{} has been properly deleted from {}.",  filename, this.rootLocation);
            }
        } catch (IOException e) {
            String msg = "Failed to delete file " + filename;
            logger.error(msg);
            throw new StorageException(msg, e);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            logger.info("{} will be storing files under {}.", this.getClass().getSimpleName(), this.rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}