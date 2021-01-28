package com.changgou.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 秒杀抢单的队列配置
 */
@Configuration
public class SeckillOrderConfig {
    /**
     * 秒杀交换机
     * @return
     */
    @Bean("exchange")
 public Exchange myExchange(){
     return ExchangeBuilder.directExchange("seckill_exchange").build();
 }

    /**
     * 定义队列
     * @return
     */
    @Bean("queue")
    public Queue myQueue(){
        return QueueBuilder.durable("seckill_queue").build();
    }
    @Bean
    public Binding myBinding(@Qualifier(value = "queue") Queue queue,@Qualifier(value = "exchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("seckill.goods").noargs();
    }
}

