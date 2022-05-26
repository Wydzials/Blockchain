package org.example.blockchain.core;

import java.util.HashMap;
import java.util.Random;

public class StakeBlockchain extends Blockchain {

  public StakeBlockchain() {
    getChain().add(Block.createGenesisBlock());
  }

  public void validatePendingTransactions(final String[] validatorAddresses) {
    HashMap<Integer, Double> usersMoney = new HashMap<>();
    long sumOfBalance = 0;

    for (int i = 0; i < validatorAddresses.length; i++) {
      long money = super.getBalanceOf(validatorAddresses[i]);
      if (money < 0)
        money = 0;
      usersMoney.put(i, (double) money);
      sumOfBalance += money;
    }

    String miningRewardAddress;
    if (sumOfBalance == 0)
      miningRewardAddress = chooseRandomValidator(validatorAddresses);
    else
      miningRewardAddress = chooseValidatorInProofOfStakeManner(validatorAddresses, usersMoney, sumOfBalance);

    final Block newBlock = new Block(getLatestBlock(), super.getPendingTransactions(), 1);
    super.getChain().add(newBlock);

    super.getPendingTransactions().clear();
    super.getPendingTransactions().add(new Transaction(null, miningRewardAddress, getMiningReward()));
  }

  private String chooseRandomValidator(final String[] miningRewardAddresses) {
    Random random = new Random();
    int chosenIndex = random.nextInt(miningRewardAddresses.length);
    return miningRewardAddresses[chosenIndex];
  }

  private String chooseValidatorInProofOfStakeManner(final String[] miningRewardAddresses,
      HashMap<Integer, Double> usersMoney, long sumOfBalance) {

    double random = Math.random();
    double valueOfProbability = 0;
    int chosenIndex = 0;

    for (int i = 0; i < miningRewardAddresses.length; i++) {
      valueOfProbability += usersMoney.get(i) / sumOfBalance;
      if (random < valueOfProbability) {
        chosenIndex = i;
        break;
      }
    }
    return miningRewardAddresses[chosenIndex];
  }
}
