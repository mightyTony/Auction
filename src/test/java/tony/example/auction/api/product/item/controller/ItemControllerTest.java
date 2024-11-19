package tony.example.auction.api.product.item.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tony.example.auction.api.product.item.domain.Item;
import tony.example.auction.api.product.item.domain.ItemCategory;
import tony.example.auction.api.product.item.domain.dto.request.ItemCreateRequest;
import tony.example.auction.api.product.item.repository.ItemRepository;
import tony.example.auction.api.product.item.service.ItemService;
import tony.example.auction.auth.domain.User;
import tony.example.auction.auth.domain.dto.request.JoinRequest;
import tony.example.auction.auth.repository.UserRepository;
import tony.example.auction.auth.service.AuthService;
import tony.example.auction.auth.service.UserService;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("local")
class ItemControllerTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    ItemService itemService;

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

//        for (int i=0; i<10; i++){
//            ItemCreateRequest itemCreateRequest = ItemCreateRequest.builder()
//                    .name("item 이름" + i)
//                    .code("item 코드" + i)
//                    .category(ItemCategory.ETC)
//                    .price(1000)
//                    .quantity(100)
//                    .imageUrl("item 이미지 URL" + i)
//                    .build();
//            itemService.createItem(itemCreateRequest, userRepository.findByUserId("user" + i).get());
//        }
    }

//    @AfterEach
//    public void clear(){
//        userRepository.deleteAll();
//        itemRepository.deleteAll();
//    }

    @Test
    @DisplayName("상품 등록 테스트")
    @Transactional
    void createItem() {
        // given
        ItemCreateRequest itemCreateRequest = ItemCreateRequest.builder()
                .name("item 이름")
                .code("item 코드")
                .category(ItemCategory.ETC)
                .price(1000)
                .quantity(100)
                .imageUrl("item 이미지 URL")
                .build();
        // when
        Optional<User> user = userRepository.findByUserId("user1");
        itemService.createItem(itemCreateRequest, user.get());
        // then
        Item savedItem = itemRepository.findByCode("item 코드")
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_ITEM_CODE));

        Assertions.assertEquals(itemCreateRequest.getName(), savedItem.getName());
        Assertions.assertEquals(itemCreateRequest.getCode(), savedItem.getCode());
        Assertions.assertEquals(itemCreateRequest.getCategory(), savedItem.getCategory());
        Assertions.assertEquals(itemCreateRequest.getPrice(), savedItem.getPrice());
        Assertions.assertEquals(itemCreateRequest.getQuantity(), savedItem.getQuantity());
        Assertions.assertEquals(itemCreateRequest.getImageUrl(), savedItem.getImageUrl());

        // 트랜잭션 내에서만 조회 가능 (영속성 컨텍스트)
        Assertions.assertEquals(user.get().getUserId(), savedItem.getSeller().getUserId());

    }

}