package com.xsh.userservice.service;

import com.xsh.userservice.model.User;
import com.xsh.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        UUID id = UUID.randomUUID();
        User expectedUser = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.getUserById(id);

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userRepository).findById(id);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.getUserById(id));
        verify(userRepository).findById(id);
    }

    @Test
    void getUserByHandle_WhenUserExists_ShouldReturnUser() {
        // Arrange
        String handle = "testHandle";
        User expectedUser = new User();
        when(userRepository.findByHandle(handle)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.getUserByHandle(handle);

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userRepository).findByHandle(handle);
    }

    @Test
    void getUserByHandle_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        String handle = "testHandle";
        when(userRepository.findByHandle(handle)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.getUserByHandle(handle));
        verify(userRepository).findByHandle(handle);
    }

    @Test
    void createUser_WhenUserHasNoId_ShouldSaveUser() {
        // Arrange
        User user = new User();
        User savedUser = new User();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User actualUser = userService.createUser(user);

        // Assert
        assertEquals(savedUser, actualUser);
        verify(userRepository).save(user);
    }

    @Test
    void createUser_WhenUserHasId_ShouldThrowException() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        // Arrange
        UUID id = UUID.randomUUID();
        User user = new User();
        User updatedUser = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User actualUser = userService.updateUser(id, user);

        // Assert
        assertEquals(updatedUser, actualUser);
        verify(userRepository).findById(id);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(id, user));
        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));

        // Act
        userService.deleteUser(id);

        // Assert
        verify(userRepository).findById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(id));
        verify(userRepository).findById(id);
        verify(userRepository, never()).deleteById(any());
    }
}