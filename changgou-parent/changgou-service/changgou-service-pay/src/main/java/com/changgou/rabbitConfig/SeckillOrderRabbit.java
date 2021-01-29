package com.changgou.rabbitConfig;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeckillOrderRabbit {
    /**
 * 定义交换机
 */
@Bean("exchange")
public Exchange myExchange(){
    return ExchangeBuilder.directExchange("seckillOrder_exchange").build();
}
    /**
     * 定义队列
     */
    @Bean("queue")
    public Queue myQueue(){
        return QueueBuilder.durable("seckillOrder_queue").build();
    }
    /**
     * 绑定队列
     */
    @Bean
    public Binding myBinding(@Qualifier("exchange") Exchange exchange, @Qualifier("queue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("pay.#").noargs();
    }
}