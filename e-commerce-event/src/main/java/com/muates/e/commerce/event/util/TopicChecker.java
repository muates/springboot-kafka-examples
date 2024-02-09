package com.muates.e.commerce.event.util;

import org.apache.kafka.clients.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class TopicChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicChecker.class);

    public static void checkTopic(AdminClient adminClient, String topicName) {
        DescribeTopicsResult topicsResult = adminClient.describeTopics(Collections.singletonList(topicName),
                new DescribeTopicsOptions().timeoutMs(5000));

        try {
            TopicDescription topicDescription = topicsResult.values().get(topicName).get();
            LOGGER.info("Topic '{}' exists! Description: {}", topicName, topicDescription);
        } catch (Exception e) {
            LOGGER.warn("Topic '{}' does not exist!", topicName);
        }
    }
}
