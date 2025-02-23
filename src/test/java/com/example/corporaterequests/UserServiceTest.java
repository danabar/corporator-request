package com.example.corporaterequests;

import com.example.corporaterequests.dto.entity.Attachment;
import com.example.corporaterequests.dto.entity.User;
import com.example.corporaterequests.dto.repository.UserRepository;
import com.example.corporaterequests.dto.request.UserRequest;
import com.example.corporaterequests.dto.response.UserResponse;
import com.example.corporaterequests.service.AttachmentService;
import com.example.corporaterequests.service.UserService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttachmentService attachmentService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for creating a user successfully.
     */
    @Test
    void testCreateUser_Success() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        User user = new User();
        user.setName("John Doe");
        user.setCivilId("123456");
        user.setCivilIdExpiryDate(LocalDate.now().plusYears(1));
        userRequest.setUserDto(user);
        userRequest.setAttachments(Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class)));

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(attachmentService.uploadAttachment(any(MultipartFile.class))).thenReturn(new Attachment());

        // Act
        UserResponse response = userService.createUser(userRequest);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", response.getUserDto().getName());
        assertEquals("123456", response.getUserDto().getCivilId());
        assertEquals("Civil ID is valid.", response.getWarningMessage());
    }

    /**
     * Test case for creating a user with fewer than two attachments.
     */
    @Test
    void testCreateUser_FewerThanTwoAttachments() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        User user = new User();
        user.setName("John Doe");
        user.setCivilId("123456");
        user.setCivilIdExpiryDate(LocalDate.now().plusYears(1));
        userRequest.setUserDto(user);
        userRequest.setAttachments(Collections.singletonList(mock(MultipartFile.class)));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userRequest);
        });
        assertEquals("At least two attachments are required.", exception.getMessage());
    }

    /**
     * Test case for creating a user with an expired civil ID.
     */
    @Test
    void testCreateUser_ExpiredCivilId() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        User user = new User();
        user.setName("John Doe");
        user.setCivilId("123456");
        user.setCivilIdExpiryDate(LocalDate.now().minusYears(1)); // Expired civil ID
        userRequest.setUserDto(user);
        userRequest.setAttachments(Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class)));

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(attachmentService.uploadAttachment(any(MultipartFile.class))).thenReturn(new Attachment());

        // Act
        UserResponse response = userService.createUser(userRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Warning: Civil ID is expired.", response.getWarningMessage());
    }

    /**
     * Test case for getting a user by ID successfully.
     */
    @Test
    void testGetUser_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setCivilId("123456");
        user.setCivilIdExpiryDate(LocalDate.now().plusYears(1));

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Act
        UserResponse response = userService.getUser(1L);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", response.getUserDto().getName());
        assertEquals("123456", response.getUserDto().getCivilId());
        assertEquals("Civil ID is valid.", response.getWarningMessage());
    }

    /**
     * Test case for getting a user by ID that does not exist.
     */
    @Test
    void testGetUser_NotFound() {
        // Arrange
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getUser(1L);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Resource not found", exception.getReason());
    }

    /**
     * Test case for updating a user successfully.
     */
    @Test
    void testUpdateUser_Success() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        User user = new User();
        user.setName("Jane Doe");
        user.setCivilId("654321");
        user.setCivilIdExpiryDate(LocalDate.now().plusYears(1));
        userRequest.setUserDto(user);
        userRequest.setAttachments(Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class)));

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(attachmentService.uploadAttachment(any(MultipartFile.class))).thenReturn(new Attachment());

        // Act
        UserResponse response = userService.updateUser(1L, userRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Jane Doe", response.getUserDto().getName());
        assertEquals("654321", response.getUserDto().getCivilId());
    }

    /**
     * Test case for updating a user that does not exist.
     */
    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        User user = new User();
        user.setName("Jane Doe");
        user.setCivilId("654321");
        user.setCivilIdExpiryDate(LocalDate.now().plusYears(1));
        userRequest.setUserDto(user);
        userRequest.setAttachments(Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class)));

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.updateUser(1L, userRequest);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Resource not found", exception.getReason());
    }

    /**
     * Test case for deleting a user successfully.
     */
    @Test
    void testDeleteUser_Success() {
        // Arrange
        doNothing().when(userRepository).deleteById(anyString());

        // Act
        userService.deleteUser("123456");

        // Assert
        verify(userRepository, times(1)).deleteById("123456");
    }

    /**
     * Test case for adding attachments to an existing user.
     */
    @Test
    void testAddAttachmentsToUser_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setCivilId("123456");
        user.setCivilIdExpiryDate(LocalDate.now().plusYears(1));

        UserRequest userRequest = new UserRequest();
        userRequest.setAttachments(Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class)));

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(attachmentService.uploadAttachment(any(MultipartFile.class))).thenReturn(new Attachment());

        // Act
        User updatedUser = userService.addAttachmentsToUser(1L, userRequest);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(2, updatedUser.getAttachments().size());
    }

    /**
     * Test case for adding attachments to a user that does not exist.
     */
    @Test
    void testAddAttachmentsToUser_NotFound() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setAttachments(Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class)));
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.addAttachmentsToUser(1L, userRequest);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
    }
}