package com.upwork.tinyurl.service;

import com.upwork.tinyurl.model.Range;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SharedRangeService {

    private CuratorFramework client;
    private DistributedAtomicLong count;

    @PostConstruct
    public void postConstruct() {
        client = CuratorFrameworkFactory.newClient(
                zooServer,
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();
        count = new DistributedAtomicLong(
                client, counterPath,
                new RetryNTimes(10, 10)
        );
    }

    @Bean("range")
    public Range getUniqueRange() {
        try {
            if (client == null) {
                postConstruct();
            }
            AtomicValue<Long> atmVal = count.increment();
            if (atmVal.succeeded()) {
                long start = atmVal.preValue() * (span != null ? span : 10);
                return new Range(start, start + (span != null ? span : 10) - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String zooServer = "localhost:2181,localhost:2182,localhost:2183";
    private static final String counterPath = "/url-shortener/counter";
    private static final Long span = 10L;

}
