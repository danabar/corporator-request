package com.example.corporaterequests.dto.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attachments")
public class Attachment {

    /**
     * attachment id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * file name.
     */
    @NotBlank(message = "file name cannot be blank")
    private String fileName;

    /**
     * file path.
     */
    @NotBlank(message = "file path cannot be blank")
    private String filePath;
}
