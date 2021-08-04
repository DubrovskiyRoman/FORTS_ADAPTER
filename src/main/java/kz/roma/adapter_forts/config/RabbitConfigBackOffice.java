package kz.roma.adapter_forts.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfigBackOffice {
    public static final String fortsBackOfficeExch = "forts_backOffice";

    public static final String instrQueue = "instrument_queue";
    public static final String ordersQueue = "order_queue";
    public static final String dealsQueue = "deals_queue";

    public static final String instrRoutingKEY = "FORTS.BACKOFFICE.INSTRUMENTS";
    public static final String ordersRoutingKEY = "FORTS.BACKOFFICE.ORDERS";
    public static final String dealsRoutingKEY = "FORTS.BACKOFFICE.DEALS";

    @Bean
    public Queue instrQueue() {
        return new Queue(instrQueue, false);
    }

    @Bean
    public Queue ordersQueue() {
        return new Queue(ordersQueue, false);
    }

    @Bean
    public Queue dealsQueue() {
        return new Queue(dealsQueue, false);
    }


    @Bean
    public TopicExchange fortsBackOfficeExch() {
        return new TopicExchange(fortsBackOfficeExch, false, false);
    }

    @Bean
    public Binding FortsBackOfficeExchBindInstrQueue(Queue instrQueue, TopicExchange fortsBackOfficeExch) {
        return BindingBuilder.bind(instrQueue).to(fortsBackOfficeExch).with(instrRoutingKEY);
    }

    @Bean
    public Binding FortsBackOfficeExchBindOrdersQueue(Queue ordersQueue, TopicExchange fortsBackOfficeExch) {
        return BindingBuilder.bind(ordersQueue).to(fortsBackOfficeExch).with(ordersRoutingKEY);
    }

    @Bean
    public Binding FortsBackOfficeExchBindDealsQueue(Queue dealsQueue, TopicExchange fortsBackOfficeExch) {
        return BindingBuilder.bind(dealsQueue).to(fortsBackOfficeExch).with(dealsRoutingKEY);

    }
}
