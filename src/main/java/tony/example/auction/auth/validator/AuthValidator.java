package tony.example.auction.auth.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tony.example.auction.auth.domain.Role;
import tony.example.auction.auth.domain.User;
import tony.example.auction.auth.domain.dto.request.JoinRequest;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;
import tony.example.auction.auth.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthValidator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void validate(JoinRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        if(userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        log.info("[AuthValidator] validate - 회원가입 유효성 검사 완료");
    }

    // 아이디, 비번 체크
    public void authCheck(String userId, String rawPassword) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    // 본인 인지 확인
    public void isYou(String userId) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // 예: `userId`를 `username`으로 저장했을 경우

        log.info("[AuthValidator] isYou - 현재 유저 아이디: {}, 파라미터 아이디 : {}", currentUserId, userId);
        if(!currentUserId.equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    public Role checkUserRoleReturnRole(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Role role = user.getRole();

        return role;
    }

    // 유저 정보 중복 체크
    public void isDuplicateInformation(String email, String phoneNumber) {
        if(userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new CustomException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    // 유저 존재 체크
    public void isUserExist(String userId) {
        if(!userRepository.existsByUserId(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    // 유저 존재 체크, 유저 반환
    public User isUserExistReturnUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
