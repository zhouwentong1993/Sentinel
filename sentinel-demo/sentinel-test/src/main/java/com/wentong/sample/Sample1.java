package com.wentong.sample;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Sample1 {

    public static void main(String[] args) {
        // 配置规则.
        initFlowRules();

        while (true) {
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性
            try (Entry entry = SphU.entry("HelloWorld")) {
                // 被保护的逻辑
                System.out.println("hello world");
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (BlockException ex) {
                // 处理被流控的逻辑
                System.out.println("blocked!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            helloWorld();
        }
    }

    @SentinelResource(value = "HelloWorld", blockHandler = "handleException")
    public static void helloWorld() {
        // 资源中的逻辑
        System.out.println("hello world");
    }

    // 规则（规则定义，规则所属）
    // 存储，随处可用。（Map）
    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(20);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
