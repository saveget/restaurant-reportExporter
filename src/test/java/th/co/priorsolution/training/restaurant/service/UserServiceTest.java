package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import th.co.priorsolution.training.restaurant.entity.UserEntity;
import th.co.priorsolution.training.restaurant.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1);
        mockUser.setName("john");
        mockUser.setPassword("encodedPassword");
        mockUser.setRole("customer");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // Arrange
        when(userRepository.findByName("john")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = userService.loadUserByUsername("john");

        // Assert
        assertEquals("john", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER")));
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByName("jane")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("jane"));
    }

    @Test
    void registerUser_shouldSaveNewUser_whenUsernameIsUnique() {
        // Arrange
        when(userRepository.findByName("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        UserEntity savedUser = userService.registerUser("john", "password123", "waitress");

        // Assert
        assertEquals("john", savedUser.getName());
        assertEquals("encodedPassword123", savedUser.getPassword());
        assertEquals("waitress", savedUser.getRole());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void registerUser_shouldThrowException_whenUsernameAlreadyExists() {
        // Arrange
        when(userRepository.findByName("john")).thenReturn(Optional.of(mockUser));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser("john", "anyPassword", "chef"));
        assertEquals("Username is already taken", ex.getMessage());
    }
}

