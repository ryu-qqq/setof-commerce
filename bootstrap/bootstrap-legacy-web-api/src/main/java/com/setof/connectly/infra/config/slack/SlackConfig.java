package com.setof.connectly.infra.config.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    @Value(value = "${slack.token}")
    String token;

    @Bean
    public MethodsClient slackClient() {
        return Slack.getInstance().methods(token);
    }
}
