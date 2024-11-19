package tony.example.auction.api.product.item.service;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tony.example.auction.api.product.item.repository.ItemRepository;

@SpringBootTest
@ActiveProfiles("local")
class ItemServiceTest {
    @Autowired
    private ItemRepository itemRepository;

    @AfterEach
    public void tearDown() {
        itemRepository.deleteAll();
    }


}