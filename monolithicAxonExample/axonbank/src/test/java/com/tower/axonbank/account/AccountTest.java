package com.tower.axonbank.account;


import com.tower.axonbank.coreapi.AccountCreatedEvent;
import com.tower.axonbank.coreapi.CreateAccountCommand;
import com.tower.axonbank.coreapi.MoneyWithdrawnEvent;
import com.tower.axonbank.coreapi.WithdrawMoneyCommand;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    private FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture(Account.class);
    }

    @Test
    public void testCreateAccount() throws Exception{
        fixture.givenNoPriorActivity()
                .when(new CreateAccountCommand("1234",1000, 0))
                .expectEvents(new AccountCreatedEvent("1234",1000, 0));
    }

    @Test
    public void withdrawReasonableAmount() throws Exception{
        fixture.given(new AccountCreatedEvent("1234",1000, 0))
                .when(new WithdrawMoneyCommand("1234",600))
                .expectEvents(new MoneyWithdrawnEvent("1234",600,400));
    }

    @Test
    public void withdrawAbsurdAmount() throws Exception{
        fixture.given(new AccountCreatedEvent("1234",1000, 0))
                .when(new WithdrawMoneyCommand("1234",1001))
                .expectNoEvents()
                .expectException(OverdraftLimitExceedException.class);
    }

    @Test
    public void withdrawTwice() throws Exception{
        fixture.given(new AccountCreatedEvent("1234",1000, 0),
                new MoneyWithdrawnEvent("1234",1000, 0))
                .when(new WithdrawMoneyCommand("1234",1000))
                .expectNoEvents()
                .expectException(OverdraftLimitExceedException.class);
    }
}