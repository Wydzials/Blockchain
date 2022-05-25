package org.example.blockchain.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.Validate;

public class WorkBlockchain extends Blockchain {
  private int difficulty;
  private static final int miningReward = 100;

  public WorkBlockchain(final int difficulty) {
    setDifficulty(difficulty);
    super.getChain().add(Block.createGenesisBlock());
  }

  public void setDifficulty(final int difficulty) {
    Validate.inclusiveBetween(1, 32, difficulty);
    this.difficulty = difficulty;
  }

  public void minePendingTransactions(final String[] miningRewardAddresses) {
    Random random = new Random();
    int chosenIndex = random.nextInt(miningRewardAddresses.length);
    String miningRewardAddress = miningRewardAddresses[chosenIndex];

    Validate.notEmpty(miningRewardAddress);
    final Block newBlock = new Block(getLatestBlock(), super.getPendingTransactions(), difficulty);
    super.getChain().add(newBlock);

    super.getPendingTransactions().clear();
    super.getPendingTransactions().add(new Transaction(null, miningRewardAddress, miningReward));
  }

  @Override
  public String toString() {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Blockchain {\n");

    for (final Block block : super.getChain()) {
      stringBuilder.append(block);
    }

    stringBuilder.append("  pendingTransactions: ")
        .append(super.getPendingTransactions())
        .append("\n}\n");

    return stringBuilder.toString();
  }
}
