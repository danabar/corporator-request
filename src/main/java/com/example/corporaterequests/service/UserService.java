package com.example.corporaterequests.service;

import com.example.corporaterequests.dto.entity.Attachment;
import com.example.corporaterequests.dto.entity.User;
import com.example.corporaterequests.dto.repository.UserRepository;
import com.example.corporaterequests.dto.request.UserRequest;
import com.example.corporaterequests.dto.response.UserResponse;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    /**
     * user repository.
     */
    @Autowired
    private UserRepository userRepository;
    /**
     * attachment service.
     */
    @Autowired
    private AttachmentService attachmentService;


    /**
     * create user.
     *
     * @param userRequest user request
     * @return user response
     */
    public UserResponse createUser(final UserRequest userRequest) {
        if (userRequest.getAttachments().size() < 2) {
            throw new
                    IllegalArgumentException(
                    "At least two attachments are required.");
        }
        String warningMessage = checkCivilIdExpiry(userRequest.getUserDto());
        // Upload attachments and add them to the user
        userRequest.getAttachments().forEach(file -> {
            Attachment attachment = attachmentService.uploadAttachment(file);
            Optional.ofNullable(userRequest.getUserDto().
                            getAttachments()).
                    orElse(new ArrayList<>()).add(attachment);
        });
        User savedUser = userRepository.save(userRequest.getUserDto());
        return new UserResponse(savedUser, warningMessage);
    }

    /**
     * check civil id expiry.
     *
     * @param user user
     * @return message warning
     */
    public String checkCivilIdExpiry(final User user) {
        Optional<User> userOptional =
                userRepository.findByCivilId(user.getCivilId());
        if (userOptional.isPresent()) {
            if (userOptional.get().getCivilIdExpiryDate()
                    .isBefore(LocalDate.now())) {
                return "Warning: Civil ID is expired!";
            }
        } else if (user.getCivilIdExpiryDate().isBefore(LocalDate.now())) {
            return "Warning: Civil ID is expired.";
        }
        return "Civil ID is valid.";
    }

    /**
     * get user by id.
     *
     * @param id user id
     * @return user response
     */
    public UserResponse getUser(final Long id) {
        User user = userRepository.findById(String.valueOf(id))
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Resource not found")
                );
        String warningMessage = checkCivilIdExpiry(user);
        return new UserResponse(user, warningMessage);
    }

    /**
     * get all users.
     *
     * @return list of user
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * update user.
     *
     * @param id          user id
     * @param userRequest user request
     * @return user response
     */
    public UserResponse updateUser(
            final Long id, final UserRequest userRequest
    ) {
        User user = userRepository.findById(String.valueOf(id))
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Resource not found")
                );
        user.setName(userRequest.getUserDto().getName());
        user.setCivilId(userRequest.getUserDto().getCivilId());
        user.setCivilIdExpiryDate(
                userRequest.getUserDto().getCivilIdExpiryDate());
        return createUser(userRequest);
    }

    /**
     * delete user.
     *
     * @param civilId civil id
     */
    public void deleteUser(final String civilId) {
        userRepository.deleteById(civilId);
    }

    /**
     * Add attachments to an existing user.
     *
     * @param id          The ID of the user
     * @param userRequest user request
     * @return The updated User entity
     */
    public User addAttachmentsToUser(
            final Long id, final UserRequest userRequest
    ) {
        User user = userRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + id));

        // Upload attachments and add them to the user
        userRequest.getAttachments().forEach(file -> {
            Attachment attachment = attachmentService.uploadAttachment(file);
            if (user.getAttachments() != null) {
                user.getAttachments().add(attachment);
            } else {
                user.setAttachments(new ArrayList<>());
                user.getAttachments().add(attachment);
            }
        });

        return userRepository.save(user);
    }
}
