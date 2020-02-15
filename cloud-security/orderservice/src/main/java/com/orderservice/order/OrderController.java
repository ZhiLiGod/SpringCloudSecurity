package com.orderservice.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OAuth2RestTemplate restTemplate;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public OrderInfo create(@RequestBody OrderInfo orderInfo) {
    PriceInfo priceInfo = restTemplate.getForObject("http://price-server:8081/prices/" + orderInfo.getProductId(), PriceInfo.class);
    log.info("Price is {}", priceInfo.getPrice());

    return orderInfo;
  }

  @GetMapping("/{id}")
  public OrderInfo getById(@PathVariable final Long id, @AuthenticationPrincipal String username) {
    log.info("Logged in user: {}", username);
    OrderInfo orderInfo = new OrderInfo();
    PriceInfo priceInfo = restTemplate.getForObject("http://localhost:8081/prices/" + 200, PriceInfo.class);
    orderInfo.setOrderId(id);
    orderInfo.setProductId(priceInfo.getId());
    orderInfo.setPrice(priceInfo.getPrice());

    return orderInfo;
  }

}
