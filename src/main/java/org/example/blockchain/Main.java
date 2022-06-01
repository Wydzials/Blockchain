package org.example.blockchain;

import java.util.List;
import org.example.blockchain.core.Blockchain;
import org.example.blockchain.core.StakeBlockchain;
import org.example.blockchain.core.WorkBlockchain;

public class Main {

  public static void main(String[] args) {
    testProofOfWork();
    testProofOfStake();
  }

  static void testProofOfStake() {
    final StakeBlockchain stakeBlockchain = new StakeBlockchain();
    insertFirstBlockData(stakeBlockchain);
    stakeBlockchain.validatePendingTransactions(new String[]{"adres1", "adres2", "adres3"});
    insertSecondBlockData(stakeBlockchain);
    stakeBlockchain.validatePendingTransactions(new String[]{"adres1", "adres2", "adres3"});
    insertThirdBlockData(stakeBlockchain);
    stakeBlockchain.validatePendingTransactions(new String[]{"adres1", "adres2", "adres3"});
    printResult(stakeBlockchain, "Proof of Stake");
  }

  static void testProofOfWork() {
    final WorkBlockchain workBlockchain = new WorkBlockchain(2);
    insertFirstBlockData(workBlockchain);
    workBlockchain.minePendingTransactions("adres1");
    insertSecondBlockData(workBlockchain);
    workBlockchain.minePendingTransactions("adres2");
    insertThirdBlockData(workBlockchain);
    workBlockchain.minePendingTransactions("adres3");
    printResult(workBlockchain, "Proof of Work");
  }

  static void insertFirstBlockData(Blockchain workBlockchain) {
    workBlockchain.createTransaction("adres1", "adres2", 30);
    workBlockchain.createTransaction("adres2", "adres3", 20);
    workBlockchain.createTransaction("adres2", "adres3", 10);
  }

  static void insertSecondBlockData(Blockchain workBlockchain) {
    workBlockchain.createTransaction("adres2", "adres1", 70);
    workBlockchain.createTransaction("adres1", "adres2", 30);
    workBlockchain.createTransaction("adres2", "adres1", 10);
  }

  static void insertThirdBlockData(Blockchain workBlockchain) {
    workBlockchain.createTransaction("adres1", "adres2", 90);
    workBlockchain.createTransaction("adres2", "adres3", 30);
    workBlockchain.createTransaction("adres3", "adres2", 20);
  }



  static void printResult(Blockchain workBlockchain, String typeOfBlockchain) {
    System.out.println("\nType of blockchain: " + typeOfBlockchain);
    System.out.println(workBlockchain);
    System.out.println("Chain is valid: " + workBlockchain.isChainValid());

    for (final String user : List.of("adres1", "adres2", "adres3")) {
      System.out.println("Balance of " + user + ": " + workBlockchain.getBalanceOf(user));
    }
  }
}

