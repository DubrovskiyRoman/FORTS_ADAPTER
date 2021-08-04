package kz.roma.adapter_forts.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfigMarketData {
    public static final String fortsMarketDataExch = "forts_marketData";

    public static final String usdRatesQueue = "usd_rates_queue";
    public static final String tradesQueue = "trades_queue";

    public static final String marketDataRoutingKEY = "FORTS.MARKETDATA.USD_RATES";
    public static final String tradesRoutingKEY = "FORTS.MARKETDATA.trades";

    @Bean
    public Queue usdRatesQueue(){
        return new Queue(usdRatesQueue, false);
    }

    @Bean
    public Queue tradesQueue(){
        return new Queue(tradesQueue, false);
    }

    @Bean
    public TopicExchange fortsMarketDataExch () {
        return new TopicExchange(fortsMarketDataExch, false, false);
    }

    @Bean
    public Binding FortsMarketDataExchBindUsdRatesQueue(Queue usdRatesQueue, TopicExchange fortsMarketDataExch) {
        return BindingBuilder.bind(usdRatesQueue).to(fortsMarketDataExch).with( marketDataRoutingKEY);
    }

    @Bean
    public Binding FortsMarketDataExchBindTradesQueue(Queue tradesQueue, TopicExchange fortsMarketDataExch) {
        return BindingBuilder.bind(tradesQueue).to(fortsMarketDataExch).with(tradesRoutingKEY);
    }



}
