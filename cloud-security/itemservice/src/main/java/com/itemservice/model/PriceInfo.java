package com.itemservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceInfo {

  private Long id;
  private BigDecimal price;

}
