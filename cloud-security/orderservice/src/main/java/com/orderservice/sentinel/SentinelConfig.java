package com.orderservice.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
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
    rule.setCount(10); // 10 requests in 1s
    List<FlowRule> rules = new ArrayList<>();
    rules.add(rule);
    FlowRuleManager.loadRules(rules);

    DegradeRule degradeRule = new DegradeRule();
    degradeRule.setResource("getOrder");
    degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
    degradeRule.setCount(10); // if response time greater than 10ms
    degradeRule.setTimeWindow(10); // circuit break time (10s)
    List<DegradeRule> degradeRules = new ArrayList<>();
    degradeRules.add(degradeRule);
    DegradeRuleManager.loadRules(degradeRules);
  }

}
