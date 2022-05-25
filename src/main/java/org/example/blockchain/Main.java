package org.example.blockchain;

import org.example.blockchain.core.Blockchain;
import org.example.blockchain.core.StakeBlockchain;
import org.example.blockchain.core.WorkBlockchain;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    final Blockchain workBlockchain = new WorkBlockchain(2);
    final Blockchain stakeBlockchain = new StakeBlockchain(2);
    testProofOfWork(workBlockchain);

  }

  static void testProofOfWork(Blockchain workBlockchain){
    workBlockchain.createTransaction("user1", "user2", 30);
    workBlockchain.createTransaction("user1", "user2", 20);
    workBlockchain.createTransaction("user2", "user1", 10);
    workBlockchain.minePendingTransactions("user3");

    workBlockchain.createTransaction("user3", "user1", 70);
    workBlockchain.createTransaction("user1", "user2", 30);
    workBlockchain.createTransaction("user2", "user1", 10);
    workBlockchain.minePendingTransactions("user3");

    System.out.println(workBlockchain);
    System.out.println("Chain is valid: " + workBlockchain.isChainValid());

    for (final String user : List.of("user1", "user2", "user3")) {
      System.out.println("Balance of " + user + ": " + workBlockchain.getBalanceOf(user));
    }
  }
}
