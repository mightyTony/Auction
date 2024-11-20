package tony.example.auction.common;

import java.util.Set;

public class Constant {
    public static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    public static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "price", "createdDate","soldCount");
}
