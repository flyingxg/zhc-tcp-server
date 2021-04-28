package com.zfjt.eletcpserver.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {
     @Bean(name = "zhc")
     public Queue zhcQueue() {
          return new Queue("zhc_queue");
     }

     @Bean
     public TopicExchange exchange() {
          return new TopicExchange("zhc.exchange");
     }

     @Bean
     public Binding bindingExchangeFire(@Qualifier("zhc") Queue queue, TopicExchange exchange) {
          return BindingBuilder.bind(queue).to(exchange).with("zhc_queue_key");
     }
}