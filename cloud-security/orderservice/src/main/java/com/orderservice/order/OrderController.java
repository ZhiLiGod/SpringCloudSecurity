package com.orderservice.order;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

//  @PreAuthorize("#hasRole('ROLE_ADMIN')")
  @PreAuthorize("#oauth2.hasScope('write')")// has to have write in scope
  @GetMapping("/{id}")
  @SentinelResource(value = "getOrder", blockHandler = "getOrderFallback")
  public OrderInfo getById(@PathVariable final Long id, @AuthenticationPrincipal String username) throws InterruptedException {
    log.info("Logged in user: {}", username);
    OrderInfo orderInfo = new OrderInfo();
    PriceInfo priceInfo = restTemplate.getForObject("http://localhost:8081/prices/" + 200, PriceInfo.class);
    orderInfo.setOrderId(id);
    orderInfo.setProductId(priceInfo.getId());
    orderInfo.setPrice(priceInfo.getPrice());
    Thread.sleep(50);

    return orderInfo;
  }

  public OrderInfo getOrderFallback(@PathVariable final Long id, @AuthenticationPrincipal String username, BlockException blockException) {
    log.info("Fallback executing...");
    OrderInfo orderInfo = new OrderInfo();
    orderInfo.setOrderId(id);
    orderInfo.setProductId(20L);
    orderInfo.setPrice(BigDecimal.ONE);

    return orderInfo;
  }

}
