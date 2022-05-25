package org.example.blockchain.core;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

public abstract class Blockchain {

    private final List<Transaction> pendingTransactions = new ArrayList<>();

    private final List<Block> chain = new ArrayList<>();

    public List<Block> getChain() {
        return chain;
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void createTransaction(final String fromAddress, final String toAddress, final long amount) {
        Validate.notEmpty(fromAddress);
        Validate.notEmpty(toAddress);
        Validate.inclusiveBetween(1, Long.MAX_VALUE, amount);

        final Transaction newTransaction = new Transaction(fromAddress, toAddress, amount);
        pendingTransactions.add(newTransaction);
    }

    abstract public void minePendingTransactions(final String miningRewardAddress);

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
}
