package com.itemservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemInfo {

  private Long itemId;
  private String itemName;
  private BigDecimal itemPrice;

}
