package com.salsa.test.salsa.domain.service;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.rules.CompanyPopularityRule;
import com.salsa.test.salsa.domain.rules.HighSalaryRule;
import com.salsa.test.salsa.domain.rules.OrderRule;
import com.salsa.test.salsa.domain.rules.RecentPostsRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobOfferRankingService {

  private final Map<String, OrderRule> availableRules;
  private final List<String> ruleOrder;

  public JobOfferRankingService(@Value("${ranking.rules:recent,salary,company}") List<String> ruleOrder) {
    this.availableRules = Map.of(
        "recent", new RecentPostsRule(),
        "salary", new HighSalaryRule(),
        "company", new CompanyPopularityRule()
    );
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
