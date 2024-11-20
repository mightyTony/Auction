package tony.example.auction.auth.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInformationUpdateRequest {
    //TODO Validation 나중에 빡시게 하기
        private String name;

        private String email;

        private String phoneNumber;

        public UserInformationUpdateRequest(String name, String email, String phoneNumber) {
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }
}
