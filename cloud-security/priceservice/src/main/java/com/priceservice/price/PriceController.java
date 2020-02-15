package com.priceservice.price;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/prices")
public class PriceController {

  @GetMapping("/{id}")
  public PriceInfo getPrice(@PathVariable("id") final Long productId, @AuthenticationPrincipal String username) {
    log.info("Product id is {}", productId);
    log.info("Price Service, logged in user: {}", username);
    PriceInfo priceInfo = new PriceInfo();
    priceInfo.setId(productId);
    priceInfo.setPrice(BigDecimal.TEN);
    return priceInfo;
  }

}
