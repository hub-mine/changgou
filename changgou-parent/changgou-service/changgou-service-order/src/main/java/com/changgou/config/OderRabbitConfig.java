package com.changgou.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OderRabbitConfig {
    /**
 * 定义交换机
 */
@Bean("exchange")
public Exchange myExchange(){
    return ExchangeBuilder.directExchange("dead_exchange").build();
}
    /**
     * 定义死性队列
     */
    @Bean("dead_queue")
    public Queue myQueue(){
        return QueueBuilder.durable("dead_queue").withArgument("dead_exchange","dead_exchange").withArgument("order.#","order.#").build();
    }
    @Bean("queue")
    public Queue normalQueue(){
        return QueueBuilder.durable("normal_queue").build();
    }
    /**
     * 绑定队列
     */
    @Bean
    public Binding myBinding(@Qualifier("exchange") Exchange exchange, @Qualifier("queue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("order.#").noargs();
    }
}

