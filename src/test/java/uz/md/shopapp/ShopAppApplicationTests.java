package uz.md.shopapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ShopAppApplicationTests {

    @Autowired
    @Qualifier("getRestTemplate")
    private RestTemplate restTemplate;

    @Test
    void contextLoads() {

        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                "https://delivery-app-production-c2af.up.railway.app/api/v1/shop/institutionType",
                String.class
        );

    }

}
