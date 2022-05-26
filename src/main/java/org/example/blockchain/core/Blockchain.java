package org.example.blockchain.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;

public abstract class Blockchain {

  private static final long miningReward = 100;

  private final List<Transaction> pendingTransactions = new ArrayList<>();
  private final List<Block> chain = new ArrayList<>();

  public void createTransaction(final String fromAddress, final String toAddress, final long amount) {
    Validate.notEmpty(fromAddress);
    Validate.notEmpty(toAddress);
    Validate.inclusiveBetween(1, Long.MAX_VALUE, amount);

    final Transaction newTransaction = new Transaction(fromAddress, toAddress, amount);
    pendingTransactions.add(newTransaction);
  }

  public boolean isChainValid() {
    for (int i = 1; i < chain.size(); i++) {
      final Block currentBlock = chain.get(i);
      final Block previousBlock = chain.get(i - 1);

      if (!currentBlock.isHashValid()) {
        System.out.println("Invalid hash of block #" + i);
        return false;
      }

      if (!currentBlock.isPreviousHashValid(previousBlock)) {
        System.out.println("Hash of block #" + (i - 1) + "does not match previousHash field of block #" + i);
        return false;
      }
    }
    return true;
  }

  public long getBalanceOf(final String address) {
    return chain.stream()
        .mapToLong(block -> block.getBalanceChangeOf(address))
        .sum();
  }

  public Block getLatestBlock() {
    return chain.get(chain.size() - 1);
  }

  @Override
  public String toString() {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Blockchain {\n");

    for (final Block block : getChain()) {
      stringBuilder.append(block);
    }

    stringBuilder.append("  pendingTransactions: ")
        .append(getPendingTransactions())
        .append("\n}\n");

    return stringBuilder.toString();
  }

  List<Block> getChain() {
    return chain;
  }

  List<Transaction> getPendingTransactions() {
    return pendingTransactions;
  }

  public static long getMiningReward() {
    return miningReward;
  }
}
