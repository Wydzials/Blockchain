package org.example.blockchain.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;

public class Blockchain {

  private final List<Block> chain = new ArrayList<>();
  private final List<Transaction> pendingTransactions = new ArrayList<>();
  private int difficulty;
  private static final int miningReward = 100;

  public Blockchain(final int difficulty) {
    setDifficulty(difficulty);
    chain.add(Block.createGenesisBlock());
  }

  public void createTransaction(final String fromAddress, final String toAddress, final long amount) {
    Validate.notEmpty(fromAddress);
    Validate.notEmpty(toAddress);
    Validate.inclusiveBetween(1, Long.MAX_VALUE, amount);

    final Transaction newTransaction = new Transaction(fromAddress, toAddress, amount);
    pendingTransactions.add(newTransaction);
  }

  public void minePendingTransactions(final String miningRewardAddress) {
    Validate.notEmpty(miningRewardAddress);

    final Block newBlock = new Block(getLatestBlock(), pendingTransactions, difficulty);
    chain.add(newBlock);

    pendingTransactions.clear();
    pendingTransactions.add(new Transaction(null, miningRewardAddress, miningReward));
  }

  public long getBalanceOf(final String address) {
    return chain.stream()
        .mapToLong(block -> block.getBalanceChangeOf(address))
        .sum();
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

  public void setDifficulty(final int difficulty) {
    Validate.inclusiveBetween(1, 32, difficulty);

    this.difficulty = difficulty;
  }

  @Override
  public String toString() {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Blockchain {\n");

    for (final Block block : chain) {
      stringBuilder.append(block);
    }

    stringBuilder.append("  pendingTransactions: ")
        .append(pendingTransactions)
        .append("\n}\n");

    return stringBuilder.toString();
  }

  private Block getLatestBlock() {
    return chain.get(chain.size() - 1);
  }
}
