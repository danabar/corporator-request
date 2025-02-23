package com.example.corporaterequests.service;

import com.example.corporaterequests.dto.entity.Attachment;
import com.example.corporaterequests.dto.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AttachmentService {

    /**
     * attachment Repository.
     */
    @Autowired
    private AttachmentRepository attachmentRepository;

    /**
     * Inject the upload directory path from application.properties.
     */
    @Value("${file.upload.directory}")
    private String uploadDirectory;

    /**
     * Upload a file and save its metadata in the database.
     *
     * @param file The file to upload.
     * @return The saved Attachment entity.
     */
    public Attachment uploadAttachment(final MultipartFile file) {
        try {
            // Resolve the upload directory path
            Path fileStorageLocation = Paths.get(uploadDirectory)
                    .toAbsolutePath().normalize();

            // Create the upload directory if it doesn't exist
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            // Generate a unique file name
            String fileName = UUID.randomUUID().toString()
                    + "_" + file.getOriginalFilename();

            // Save the file to the uploads directory
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);

            // Create and save the Attachment entity
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFilePath(targetLocation.toString());

            return attachmentRepository.save(attachment);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file "
                    + file.getOriginalFilename(), ex);
        }
    }

    /**
     * Get an attachment by its ID.
     *
     * @param id The ID of the attachment.
     * @return The Attachment entity.
     */
    public Attachment getAttachmentById(final Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Attachment not found with id: " + id));
    }
}
