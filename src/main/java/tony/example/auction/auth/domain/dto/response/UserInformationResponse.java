package tony.example.auction.auth.domain.dto.response;

import lombok.Data;
import lombok.Getter;
import tony.example.auction.auth.domain.Role;
import tony.example.auction.auth.domain.User;

@Data
public class UserInformationResponse {

    private String userId;

    private String username;

    private String name;

    private String email;

    private String phoneNumber;

    private Role role;

    private UserInformationResponse(String userId, String username, String name, String email, String phoneNumber, Role role) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public static UserInformationResponse from(User user) {
        return new UserInformationResponse(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole());
    }
}
