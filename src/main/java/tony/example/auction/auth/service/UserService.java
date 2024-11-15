package tony.example.auction.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tony.example.auction.auth.domain.User;
import tony.example.auction.auth.domain.dto.request.UserInformationUpdateRequest;
import tony.example.auction.auth.domain.dto.response.UserInformationResponse;
import tony.example.auction.auth.repository.UserRepository;
import tony.example.auction.auth.validator.AuthValidator;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthValidator authValidator;

    /**
     * POST /api/users/details - 특정 사용자 정보 조회 (userId)
     * PUT /api/users/{id} - 사용자 정보 수정 (name, email, phoneNumber)
     * DELETE /api/users/{id} - 사용자 삭제 ({id})
     */

    // 특정 사용자 정보 조회 (userId)
    @Transactional(readOnly = true)
    public UserInformationResponse getUserInformation(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserInformationResponse.from(user);
    }

    // 사용자 정보 수정 (name, email, phoneNumber)
    @Transactional
    public UserInformationResponse updateUserInformation(String userId, UserInformationUpdateRequest request) {
        // 유저 체크
        User user = authValidator.isUserExistReturnUser(userId);
        // 유저 정보 중복 체크
        authValidator.isDuplicateInformation(request.getEmail(), request.getPhoneNumber());

        user.updateInformation(request.getName(), request.getEmail(), request.getPhoneNumber());
        userRepository.save(user);

        return UserInformationResponse.from(user);
    }
}
