package org.example.blockchain.core;

import org.apache.commons.lang3.Validate;

public class Transaction {

  public final String fromAddress;
  public final String toAddress;
  public final long amount;

  Transaction(final String fromAddress, final String toAddress, final long amount) {
    Validate.notEmpty(toAddress);
    Validate.inclusiveBetween(1, Long.MAX_VALUE, amount);

    this.fromAddress = fromAddress;
    this.toAddress = toAddress;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return fromAddress + "-(" + amount + ")->" + toAddress;
  }
}
