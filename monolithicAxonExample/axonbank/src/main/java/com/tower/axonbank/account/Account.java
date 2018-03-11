package com.tower.axonbank.account;

import com.tower.axonbank.coreapi.AccountCreatedEvent;
import com.tower.axonbank.coreapi.CreateAccountCommand;
import com.tower.axonbank.coreapi.MoneyWithdrawnEvent;
import com.tower.axonbank.coreapi.WithdrawMoneyCommand;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
public class Account {

    @AggregateIdentifier
    private String accountId;
    private int balance;
    private int overdraftLimit;

    @CommandHandler
    public Account(CreateAccountCommand command){
        apply(new AccountCreatedEvent(command.getAccountId(), command.getBalance(), command.getOverDraftLimit()));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command) throws OverdraftLimitExceedException {
        if(balance + overdraftLimit >= command.getAmount()){
            apply(new MoneyWithdrawnEvent(command.getAccountId(), command.getAmount(), balance - command.getAmount()));
        }else {
            throw new OverdraftLimitExceedException();
        }
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId = event.getAccountId();
        this.balance = event.getBalance();
        this.overdraftLimit = event.getOverDraftLimit();
    }

    @EventSourcingHandler
    public void  on(MoneyWithdrawnEvent event){
        this.balance = event.getBalance();
    }
}
