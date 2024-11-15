package tony.example.auction.auth.domain.dto.request;

import lombok.Getter;

@Getter
public class UserInformationUpdateRequest {

        private String name;

        private String email;

        private String phoneNumber;

        public UserInformationUpdateRequest(String name, String email, String phoneNumber) {
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }
}
