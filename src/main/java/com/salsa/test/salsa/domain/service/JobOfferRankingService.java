package com.salsa.test.salsa.domain.service;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.rules.OrderRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JobOfferRankingService {

  private final Map<String, OrderRule> availableRules;
  private final List<String> ruleOrder;

  public JobOfferRankingService(
      ApplicationContext context,
      @Value("${ranking.rules:recent,salary,company}") List<String> ruleOrder
  ) {
    // Carga todos los OrderRule registrados en Spring con su @Component("name")
    this.availableRules = context.getBeansOfType(OrderRule.class);
    this.ruleOrder = ruleOrder;
  }

  public List<JobOffer> rank(List<JobOffer> offers) {
    List<JobOffer> ranked = offers;

    for (String ruleName : ruleOrder) {
      OrderRule rule = availableRules.get(ruleName);
      if (rule != null) {
        ranked = rule.apply(ranked);
      }
    }

    return ranked;
  }
}

