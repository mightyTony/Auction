package tony.example.auction.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ActiveProfiles;
import tony.example.auction.auth.domain.User;
import tony.example.auction.auth.domain.dto.request.JoinRequest;
import tony.example.auction.auth.domain.dto.request.UserInformationUpdateRequest;
import tony.example.auction.auth.domain.dto.response.UserInformationResponse;
import tony.example.auction.auth.repository.UserRepository;
import tony.example.auction.auth.service.AuthService;
import tony.example.auction.auth.service.UserService;
import tony.example.auction.auth.validator.AuthValidator;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;

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

    @Test
    @DisplayName("사용자 정보 수정 테스트")
    void updateUserInformation() {
        // given
        String userId = "user1";
        UserInformationResponse userInformation = userService.getUserInformation(userId);
        Optional<User> user = userRepository.findByUserId(userId);

        UserInformationUpdateRequest request = new UserInformationUpdateRequest(
                "NewName1",
                "NewEmail1@naver.com",
                "010-1234-1234");

        // when
        UserInformationResponse updatedUserInformation = userService.updateUserInformation(userId, request);
        Optional<User> updatedUser = userRepository.findByUserId(userId);

        // then
        log.info("[사용자 정보 수정]");
        Assertions.assertEquals(request.getName(), updatedUser.get().getName());

    }

    @Test
    @DisplayName("사용자 정보 수정 테스트/ 중복 된 정보 일 경우")
    void updateUserInformationDuplicate() {
        // given
        String userId = "user1"; // 테스트 대상 사용자 ID
        UserInformationUpdateRequest request = new UserInformationUpdateRequest(
                "UpdatedName",           // 새 이름
                "user2@example.com",     // 중복된 이메일 (user2가 이미 사용 중)
                "010-1234-5672"          // 중복된 전화번호 (user2가 이미 사용 중)
        );

        log.info("[사용자 정보 수정/ 중복 된 정보 일 경우]");
        // when & then
        Assertions.assertThrows(CustomException.class, () -> {
            userService.updateUserInformation(userId, request);
        });
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void deleteUser() {
        // given
        String userId = "user1";
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // when
        userService.deleteUser(userId);
        Optional<User> deletedUser = userRepository.findByUserId(userId);
        // then
//        log.info("[사용자 삭제] deleted : {}", user.getDeleted());
//        Assertions.assertEquals(true, user.getDeleted());

        // 삭제되어도 위에서 조회해서 객체가 영속성 컨텍스트엔 남아있어서 null이 아님
        Assertions.assertTrue(deletedUser.isEmpty());
    }
}

