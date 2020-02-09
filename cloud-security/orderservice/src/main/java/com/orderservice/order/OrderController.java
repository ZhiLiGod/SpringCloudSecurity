package com.orderservice.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

  private RestTemplate restTemplate = new RestTemplate();

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public OrderInfo create(@RequestBody OrderInfo orderInfo) {
    PriceInfo priceInfo = restTemplate.getForObject("http://localhost:8081/prices/" + orderInfo.getProductId(), PriceInfo.class);
    log.info("Price is {}", priceInfo.getPrice());

    return orderInfo;
  }

}