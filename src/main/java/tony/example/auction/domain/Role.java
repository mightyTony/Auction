package tony.example.auction.domain;

public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }
}
