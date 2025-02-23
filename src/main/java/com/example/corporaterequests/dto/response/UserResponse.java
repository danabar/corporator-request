package com.example.corporaterequests.dto.response;

import com.example.corporaterequests.dto.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /**
     * user object.
     */
    private User userDto;

    /**
     * warning message.
     */
    private String warningMessage;
}
