package com.example.corporaterequests.dto.request;

import com.example.corporaterequests.dto.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    /**
     * request name.
     */
    private String requestName;

    /**
     * request status.
     */
    @NotNull
    private String status;

    /**
     * user object.
     */
    @NotNull
    private User userDto;

    /**
     * user request attachments.
     */
    @Size(min = '2', max = '4',
            message = "List must contain between 2 and 4 items")
    private List<MultipartFile> attachments;
}
