package lu.cnfpc.edujobapp.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lu.cnfpc.edujobapp.dto.request.LoginUserRequestDto;
import lu.cnfpc.edujobapp.dto.request.RegisterUserRequestDto;
import lu.cnfpc.edujobapp.dto.response.AuthUserResponseDto;
import lu.cnfpc.edujobapp.entity.Role;
import lu.cnfpc.edujobapp.entity.User;
import lu.cnfpc.edujobapp.entity.enums.ERole;
import lu.cnfpc.edujobapp.exception.EmailAlreadyExistsException;
import lu.cnfpc.edujobapp.exception.UsernameAlreadyExistsException;
import lu.cnfpc.edujobapp.repository.RoleRepository;
import lu.cnfpc.edujobapp.repository.UserRepository;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    public AuthUserResponseDto register(RegisterUserRequestDto dto) {
        // Check if username already exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UsernameAlreadyExistsException(dto.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }
        
        // Find default USER role
        Role role = roleRepository.findByName(ERole.USER.name())
                .orElseThrow(() -> new RuntimeException("Error: Default Role is not found."));
        
        // Create new user
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());
        user.setPhone(dto.getPhone());
        user.setRole(role);
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtService.generateToken(savedUser);
        
        // Return response
        return new AuthUserResponseDto(
            token,
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getRole().getName()
        );
    }
    
    public AuthUserResponseDto login(LoginUserRequestDto dto) {
        // Find user by username
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        
        // Check password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        // Generate JWT token
        String token = jwtService.generateToken(user);
        
        // Return response
        return new AuthUserResponseDto(
            token,
            user.getId(),
            user.getUsername(),
            user.getRole().getName()
        );
    }
}