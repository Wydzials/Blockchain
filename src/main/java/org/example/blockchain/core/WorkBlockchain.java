package org.example.blockchain.core;

import org.apache.commons.lang3.Validate;

public class WorkBlockchain extends Blockchain {

  private int difficulty;

  public WorkBlockchain(final int difficulty) {
    setDifficulty(difficulty);
    getChain().add(Block.createGenesisBlock());
  }

  public void setDifficulty(final int difficulty) {
    Validate.inclusiveBetween(1, 32, difficulty);
    this.difficulty = difficulty;
  }

  public void minePendingTransactions(final String miningRewardAddress) {
    Validate.notEmpty(miningRewardAddress);

    final Block newBlock = new Block(getLatestBlock(), super.getPendingTransactions(), difficulty);
    getChain().add(newBlock);

    getPendingTransactions().clear();
    getPendingTransactions().add(new Transaction(null, miningRewardAddress, getMiningReward()));
  }
}
