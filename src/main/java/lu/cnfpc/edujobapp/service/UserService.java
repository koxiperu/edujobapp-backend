package lu.cnfpc.edujobapp.service;

import lu.cnfpc.edujobapp.dto.request.RegisterUserRequestDto;
import lu.cnfpc.edujobapp.dto.response.UserResponse;
import lu.cnfpc.edujobapp.entity.User;
import lu.cnfpc.edujobapp.exception.EmailAlreadyExistsException;
import lu.cnfpc.edujobapp.exception.UsernameAlreadyExistsException;
import lu.cnfpc.edujobapp.mapper.UserMapper;
import lu.cnfpc.edujobapp.repository.UserRepository;
import lu.cnfpc.edujobapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Helper to get currently logged-in user
    public User getCurrentUser() {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
             throw new RuntimeException("Error: User is not authenticated.");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
    }

    public UserResponse getCurrentUserProfile() {
        return userMapper.toUserResponse(getCurrentUser());
    }

    public UserResponse updateUserProfile(RegisterUserRequestDto request) {
        User user = getCurrentUser();

        // If username changes, check availability
        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        // If email changes, check availability
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthDate(request.getBirthDate());
        user.setPhone(request.getPhone());
        
        // Only update password if provided and not empty
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // ADMIN Methods

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toUserResponse(user);
    }
    
    // Admin update user (reusing request DTO for simplicity, though a dedicated AdminUpdateUserDto is often better)
    public UserResponse updateUser(Long id, RegisterUserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

         // Similar checks to profile update
        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
             throw new UsernameAlreadyExistsException(request.getUsername());
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
             throw new EmailAlreadyExistsException(request.getEmail());
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthDate(request.getBirthDate());
        user.setPhone(request.getPhone());
        
        // Admin resetting password if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
