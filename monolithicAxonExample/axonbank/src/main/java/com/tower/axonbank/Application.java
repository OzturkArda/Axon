package com.tower.axonbank;

import com.tower.axonbank.account.Account;
import com.tower.axonbank.coreapi.CreateAccountCommand;
import com.tower.axonbank.coreapi.WithdrawMoneyCommand;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.config.Configuration;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

public class Application {

    public static void main(String[] args) {

        Configuration configuration = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(Account.class)
                .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
                .configureCommandBus( c -> new AsynchronousCommandBus())
                .buildConfiguration();

        configuration.start();
        configuration.commandBus()
                .dispatch(asCommandMessage(new CreateAccountCommand("1234",1000, 0)));
        configuration.commandBus().dispatch(asCommandMessage(new WithdrawMoneyCommand("1234",500)));
        configuration.commandBus().dispatch(asCommandMessage(new WithdrawMoneyCommand("1234",501)));

    }
}
