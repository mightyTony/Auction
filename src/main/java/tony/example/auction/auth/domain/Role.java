package tony.example.auction.auth.domain;

public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_SELLER("ROLE_SELLER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }
}
