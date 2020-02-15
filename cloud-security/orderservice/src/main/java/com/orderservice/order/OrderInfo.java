package com.orderservice.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

  private Long productId;
  private Long orderId;
  private BigDecimal price;

}
