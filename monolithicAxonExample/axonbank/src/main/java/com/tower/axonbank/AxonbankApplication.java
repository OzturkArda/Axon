package com.tower.axonbank;

import com.tower.axonbank.coreapi.CreateAccountCommand;
import com.tower.axonbank.coreapi.WithdrawMoneyCommand;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.spring.config.EnableAxon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@EnableAxon
@SpringBootApplication
public class AxonbankApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext config = SpringApplication.run(AxonbankApplication.class, args);
		CommandBus commandBus = config.getBean(CommandBus.class);


		commandBus.dispatch(asCommandMessage(new CreateAccountCommand("1234",1000, 0)));
		commandBus.dispatch(asCommandMessage(new CreateAccountCommand("4321",1000, 0)));
		commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("1234",500)));
		commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("1234",500)));
		commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321",501)));
	/*	commandBus.dispatch(asCommandMessage(new CreateAccountCommand("4321", 1000, 0)),
				new  CommandCallback<Object, Object>() {
					@Override
					public void onSuccess(CommandMessage<?> commandMessage, Object o) {
						commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321",500)));
						commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321",500)));
					}

					@Override
					public void onFailure(CommandMessage<?> commandMessage, Throwable throwable) {

					}
				});*/
	}

	@Bean
	public EventStorageEngine eventStorageEngine() {
		return new InMemoryEventStorageEngine();
	}

	@Bean
	public CommandBus commandBus() {
		return new AsynchronousCommandBus();
	}
}
