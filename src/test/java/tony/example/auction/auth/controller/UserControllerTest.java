package tony.example.auction.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tony.example.auction.auth.domain.User;
import tony.example.auction.auth.domain.dto.request.JoinRequest;
import tony.example.auction.auth.domain.dto.response.UserInformationResponse;
import tony.example.auction.auth.repository.UserRepository;
import tony.example.auction.auth.service.AuthService;
import tony.example.auction.auth.service.UserService;
import tony.example.auction.auth.validator.AuthValidator;

import java.util.Optional;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
class UserControllerTest {
    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthValidator authValidator;
    @Autowired
    private AuthController authController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void init() {
        for (int i=0; i<10; i++) {
            JoinRequest joinRequest = new JoinRequest(
                    "user" + i,       // userId
                    "username" + i,   // username
                    "password" + i,   // password
                    "name" + i,       // name
                    "user" + i + "@example.com", // email
                    "010-1234-567" + i); // pho
            authService.join(joinRequest);
        }
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 정보 조회 테스트")
    void getUserInformation() {

        // given
        String userId = "user1";

        // when
        UserInformationResponse userInformation = userService.getUserInformation(userId);
        Optional<User> user = userRepository.findByUserId(userId);
        // then
        log.info("[사용자 정보 조회]");
        Assertions.assertEquals(user.get().getPhoneNumber(), userInformation.getPhoneNumber());
    }
}