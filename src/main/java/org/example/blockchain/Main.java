package org.example.blockchain;

import org.example.blockchain.core.Blockchain;
import org.example.blockchain.core.StakeBlockchain;
import org.example.blockchain.core.WorkBlockchain;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    //testProofOfWork();
    testStakeOfWork();
  }
  static void testStakeOfWork() {
    final Blockchain stakeBlockchain = new StakeBlockchain(2);
    insertFirstBlockData(stakeBlockchain);
    stakeBlockchain.minePendingTransactions(new String[]{"user1", "user2", "user3"});
    insertSecondBlockData(stakeBlockchain);
    stakeBlockchain.minePendingTransactions(new String[]{"user1", "user2", "user3"});
    printResult(stakeBlockchain);
  }
  static void testProofOfWork() {
    final Blockchain workBlockchain = new WorkBlockchain(2);
    insertFirstBlockData(workBlockchain);
    workBlockchain.minePendingTransactions(new String[]{"user3"});
    insertSecondBlockData(workBlockchain);
    workBlockchain.minePendingTransactions(new String[]{"user3"});
    printResult(workBlockchain);
  }
  static void insertFirstBlockData(Blockchain workBlockchain) {
    workBlockchain.createTransaction("user1", "user2", 30);
    workBlockchain.createTransaction("user1", "user2", 20);
    workBlockchain.createTransaction("user2", "user3", 10);
  }

  static void insertSecondBlockData(Blockchain workBlockchain) {
    workBlockchain.createTransaction("user3", "user1", 70);
    workBlockchain.createTransaction("user1", "user2", 30);
    workBlockchain.createTransaction("user2", "user1", 10);
  }
  static void printResult(Blockchain workBlockchain) {
    System.out.println(workBlockchain);
    System.out.println("Chain is valid: " + workBlockchain.isChainValid());

    for (final String user : List.of("user1", "user2", "user3")) {
      System.out.println("Balance of " + user + ": " + workBlockchain.getBalanceOf(user));
    }
  }
}

