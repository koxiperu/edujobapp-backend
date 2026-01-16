package lu.cnfpc.edujobapp.mapper;

import lu.cnfpc.edujobapp.dto.response.UserResponse;
import lu.cnfpc.edujobapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getPhone(),
                user.getRole().getName()
        );
    }
}
