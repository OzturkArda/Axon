package com.tower.axonbank.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

class CreateAccountCommand(val accountId : String, val balance : Int, val overDraftLimit : Int)
class WithdrawMoneyCommand(@TargetAggregateIdentifier val accountId: String, val amount : Int)

class AccountCreatedEvent(val accountId: String, val balance: Int, val overDraftLimit: Int)
class MoneyWithdrawnEvent(val accountId: String, val amount: Int, val balance: Int)