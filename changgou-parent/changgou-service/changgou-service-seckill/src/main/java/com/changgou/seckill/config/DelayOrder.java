package com.changgou.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DelayOrder {
    /**
     * 秒杀交换机
     * @return
     */
    @Bean("exchange")
    public Exchange myExchange(){
        return ExchangeBuilder.directExchange("delayOrder_exchange").build();
    }

    /**
     * 定义队列
     * @return
     */
    @Bean("queue")
    public Queue myQueue(){
        return QueueBuilder.durable("normalOrder_queue").build();
    }
    @Bean("delayQueue")
    public Queue DelayQueue(){
        return QueueBuilder.durable("delayOrder_queue").withArgument("delayOrder_exchange","delayOrder_exchange").build();
    }
    @Bean
    public Binding myBinding(@Qualifier(value = "queue") Queue queue, @Qualifier(value = "exchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("seckill.goods").noargs();
    }
}
