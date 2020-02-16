package com.itemservice.controller;

import com.itemservice.model.ItemInfo;
import com.itemservice.model.PriceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {

  @Autowired
  private OAuth2RestTemplate oAuth2RestTemplate;

  @GetMapping("/{id}")
  public ItemInfo getById(@PathVariable Long id) {
    ItemInfo itemInfo = new ItemInfo();
    itemInfo.setItemId(id);
    PriceInfo priceInfo = oAuth2RestTemplate.getForObject("http://price-server:8081/prices/1", PriceInfo.class);
    itemInfo.setItemPrice(priceInfo.getPrice());
    itemInfo.setItemName("Test Item");

    return itemInfo;
  }

  @GetMapping("/no-auth")
  public String noAuth() {
    return "NO AUTH";
  }

}
