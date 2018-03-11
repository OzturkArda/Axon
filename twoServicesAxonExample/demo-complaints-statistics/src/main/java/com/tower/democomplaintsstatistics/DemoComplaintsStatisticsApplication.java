package com.tower.democomplaintsstatistics;

import com.rabbitmq.client.Channel;
import com.tower.democomplaints.ComplaintFiledEvent;
import org.axonframework.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class DemoComplaintsStatisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoComplaintsStatisticsApplication.class, args);
	}

	@ProcessingGroup("statistics")
	@RestController
	public static class StatisticsUpdater{

		private final ConcurrentMap<String, AtomicLong> statistics =  new ConcurrentHashMap<>();

		@EventHandler
		public void handle(ComplaintFiledEvent event){
			int a = 10;
			statistics.computeIfAbsent(event.getCompany(), k -> new AtomicLong()).incrementAndGet();
		}

		@GetMapping
		public ConcurrentMap<String, AtomicLong> getStatistics(){
			return statistics;
		}
	}

	@Bean
	public SpringAMQPMessageSource statisticsSource(Serializer serializer){
		return new SpringAMQPMessageSource(new DefaultAMQPMessageConverter(serializer)){

			@RabbitListener(queues = "ComplaintEvents")
			@Override
			public void onMessage(Message message, Channel channel) throws Exception {
				super.onMessage(message, channel);
			}
		};
	}
}
