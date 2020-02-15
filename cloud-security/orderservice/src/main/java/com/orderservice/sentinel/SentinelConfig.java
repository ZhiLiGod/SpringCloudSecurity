package com.orderservice.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SentinelConfig implements ApplicationListener<ContextRefreshedEvent> {

  // execute when application ready
  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    FlowRule rule = new FlowRule();
    rule.setResource("getOrder");
    rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
    rule.setCount(1);
    List<FlowRule> rules = new ArrayList<>();
    rules.add(rule);
    FlowRuleManager.loadRules(rules);
  }

}
