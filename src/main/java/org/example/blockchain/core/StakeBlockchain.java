package org.example.blockchain.core;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Random;

public class StakeBlockchain extends Blockchain {
  private int difficulty;
  private static final int miningReward = 100;

  public StakeBlockchain(final int difficulty) {
    setDifficulty(difficulty);
    super.getChain().add(Block.createGenesisBlock());
  }

  public void setDifficulty(final int difficulty) {
    Validate.inclusiveBetween(1, 32, difficulty);
    this.difficulty = difficulty;
  }

  public void minePendingTransactions(final String[] miningRewardAddresses) {
    HashMap<Integer, Double> usersMoney = new HashMap<>();
    long sumOfBalance = 0;

    for (int i = 0; i < miningRewardAddresses.length; i++) {
      long money = super.getBalanceOf(miningRewardAddresses[i]);
      if (money < 0)
        money = 0;
      usersMoney.put(i, (double) money);
      sumOfBalance += money;
    }

    String miningRewardAddress = null;
    if (sumOfBalance == 0)
      miningRewardAddress = chooseMinerInProofOfWorkManner(miningRewardAddresses);
    else
      miningRewardAddress = chooseMinerInProofOfStakeManner(miningRewardAddresses, usersMoney, sumOfBalance);

    final Block newBlock = new Block(getLatestBlock(), super.getPendingTransactions(), difficulty);
    super.getChain().add(newBlock);

    super.getPendingTransactions().clear();
    super.getPendingTransactions().add(new Transaction(null, miningRewardAddress, miningReward));
  }

  private String chooseMinerInProofOfWorkManner(final String[] miningRewardAddresses) {
    Random random = new Random();
    int chosenIndex = random.nextInt(miningRewardAddresses.length);
    return miningRewardAddresses[chosenIndex];
  }

  private String chooseMinerInProofOfStakeManner(final String[] miningRewardAddresses, HashMap<Integer, Double> usersMoney, long sumOfBalance) {
    double random = Math.random();
    double valueOfProbability = 0;
    int chosenIndex = 0;

    for (int i = 0; i < miningRewardAddresses.length; i++) {
      valueOfProbability += usersMoney.get(i)/sumOfBalance;
      if (random < valueOfProbability){
        chosenIndex = i;
        break;
      }
    }
    return miningRewardAddresses[chosenIndex];
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
