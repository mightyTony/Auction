# API 목록 체크리스트

## 사용자 관리
- [x] **POST** `/api/users/join` - 사용자 등록 (`userId`, `username`, `password`, `name`, `email`, `phoneNumber`)
- [x] **POST** `/api/users/login` - 로그인 및 인증 토큰 발급 (`username`, `password`)
- [x] **POST** `/api/users/logout` - 로그아웃
- [x] **POST** `/api/users/refresh` - 토큰 갱신 ( `refreshToken` )
- [x] **POST** `/api/users/info` - 특정 사용자 정보 조회 (`userId`)
- [x] **PATCH** `/api/users/{id}` - 사용자 정보 수정 (`name`, `email`, `phoneNumber`)
- [x] **DELETE** `/api/users/{id}` - 사용자 삭제 (`{id}`)

---

## 상품 관리
- [X] **POST** `/api/items` - 새로운 상품 등록 (`name`, `description`, `code`)
- [ ] **GET** `/api/items` - 해당 카테고리 상품 목록 조회(검색)
- [ ] **POST** `/api/items/details` - 특정 상품의 상세 정보 검색 (`상품 이름`)
- [ ] **PUT** `/api/items/{id}` - 상품 정보 수정 (`name`, `description`, `code`)
- [ ] **DELETE** `/api/items/{id}` - 상품 삭제 (`{id}`)
- [ ] **GET** `/api/items/{id}` - 특정 상품 상세 정보 조회 (`{id}`)
---

## 상품 옵션 관리
- [ ] **POST** `/api/items/{itemId}/options` - 상품에 옵션 추가 (`color`, `stock`, `price`)
- [ ] **GET** `/api/items/{itemId}/options` - 특정 상품의 옵션 목록 조회

---

## 판매 게시글 관리
- [ ] **POST** `/api/sales` - 판매 게시글 등록 (`itemId`, `userId`, `title`, `content`)
- [ ] **GET** `/api/sales` - 판매 게시글 목록 조회
- [ ] **POST** `/api/sales/details` - 특정 판매 게시글 상세 정보 조회 (`saleId`)
- [ ] **PUT** `/api/sales/{id}` - 판매 게시글 수정 (`title`, `content`)
- [ ] **DELETE** `/api/sales/{id}` - 판매 게시글 삭제 (`{id}`)

---

## 판매글 이미지 관리
- [ ] **POST** `/api/sales/{saleId}/images` - 특정 판매 게시글에 이미지 업로드 (`image`, `isMain`)
- [ ] **DELETE** `/api/sales/{saleId}/images/{imageId}` - 특정 판매 게시글에서 이미지 삭제 (`{saleId}`, `{imageId}`)

---

## 리뷰 관리
- [ ] **POST** `/api/sales/{saleId}/reviews` - 특정 판매 게시글에 리뷰 작성 (`userId`, `rating`, `comment`)
- [ ] **GET** `/api/sales/{saleId}/reviews` - 특정 판매 게시글의 리뷰 목록 조회
- [ ] **DELETE** `/api/reviews/{reviewId}` - 리뷰 삭제 (`{reviewId}`)

---

## 찜 목록 관리
- [ ] **POST** `/api/wishlist` - 사용자가 상품을 찜 목록에 추가 (`userId`, `itemId`)
- [ ] **POST** `/api/wishlist/details` - 특정 사용자의 찜 목록 조회 (`userId`)
- [ ] **DELETE** `/api/wishlist/{userId}/items/{itemId}` - 특정 상품 찜 목록에서 제거 (`{userId}`, `{itemId}`)

---

## 공지사항 관리
- [ ] **POST** `/api/notices` - 공지사항 작성 (`userId`, `title`, `content`, `priority`)
- [ ] **GET** `/api/notices` - 전체 공지사항 목록 조회
- [ ] **POST** `/api/notices/details` - 특정 공지사항 상세 조회 (`noticeId`)
- [ ] **PUT** `/api/notices/{id}` - 공지사항 수정 (`title`, `content`, `priority`)
- [ ] **DELETE** `/api/notices/{id}` - 공지사항 삭제 (`{id}`)

---

## 이벤트 관리
- [ ] **POST** `/api/events` - 이벤트 등록 (`userId`, `title`, `content`, `start_date`, `end_date`)
- [ ] **GET** `/api/events` - 전체 이벤트 목록 조회
- [ ] **POST** `/api/events/details` - 특정 이벤트 상세 조회 (`eventId`)
- [ ] **PUT** `/api/events/{id}` - 이벤트 정보 수정 (`title`, `content`, `start_date`, `end_date`)
- [ ] **DELETE** `/api/events/{id}` - 이벤트 삭제 (`{id}`)


-----
캐시 관리 후보

상품 목록 및 상세 조회 (/api/items, /api/items/details)

판매 게시글 목록 및 상세 조회 (/api/sales, /api/sales/details)

리뷰 목록 조회 (/api/sales/{saleId}/reviews)

공지사항 및 이벤트 목록 및 상세 조회 (/api/notices, /api/notices/details, /api/events, /api/events/details)
