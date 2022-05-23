package org.example.blockchain;

import java.util.List;
import org.example.blockchain.core.Blockchain;

public class Main {

  public static void main(String[] args) {
    final Blockchain blockchain = new Blockchain(2);

    blockchain.createTransaction("user1", "user2", 30);
    blockchain.createTransaction("user1", "user2", 20);
    blockchain.createTransaction("user2", "user1", 10);
    blockchain.minePendingTransactions("user3");

    blockchain.createTransaction("user3", "user1", 70);
    blockchain.createTransaction("user1", "user2", 30);
    blockchain.createTransaction("user2", "user1", 10);
    blockchain.minePendingTransactions("user3");

    System.out.println(blockchain);
    System.out.println("Chain is valid: " + blockchain.isChainValid());

    for (final String user : List.of("user1", "user2", "user3")) {
      System.out.println("Balance of " + user + ": " + blockchain.getBalanceOf(user));
    }
  }
}
