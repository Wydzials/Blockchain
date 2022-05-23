package org.example.blockchain.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class Block {

  private final LocalDateTime created;
  private final String previousHash;
  private final String hash;
  private final int nonce;

  private final List<Transaction> transactions = new ArrayList<>();

  public Block(final Block previousBlock, final List<Transaction> transactions, final int difficulty) {
    this.created = LocalDateTime.now();
    this.previousHash = previousBlock.hash;

    final Pair<String, Integer> hashAndNonce = calculateHash(created, previousHash, transactions, difficulty);
    this.hash = hashAndNonce.getLeft();
    this.nonce = hashAndNonce.getRight();

    this.transactions.addAll(transactions);
  }

  private Block() {
    this.created = LocalDateTime.now();
    this.previousHash = "0";

    final Pair<String, Integer> hashAndNonce = calculateHash(created, previousHash, transactions, 1);
    this.hash = hashAndNonce.getLeft();
    this.nonce = hashAndNonce.getRight();
  }

  static Block createGenesisBlock() {
    return new Block();
  }

  @Override
  public String toString() {
    return "  Block {\n"
        + "    created: " + created + "\n"
        + "    previousHash: " + previousHash + "\n"
        + "    hash: " + hash + "\n"
        + "    nonce: " + nonce + "\n"
        + "    transactions: " + getTransactionsAsString(transactions) + "\n"
        + "  }\n";
  }

  boolean isHashValid() {
    final String calculatedHash = DigestUtils.sha256Hex(created + previousHash + nonce +
        getTransactionsAsString(transactions));
    return calculatedHash.equals(hash);
  }

  boolean isPreviousHashValid(final Block previousBlock) {
    return previousHash.equals(previousBlock.hash);
  }

  long getBalanceChangeOf(final String address) {
    final long incomingTransfersSum = transactions.stream()
        .filter(transaction -> Objects.equals(transaction.toAddress, address))
        .mapToLong(transaction -> transaction.amount)
        .sum();

    final long outgoingTransfersSum = transactions.stream()
        .filter(transaction -> Objects.equals(transaction.fromAddress, address))
        .mapToLong(transaction -> transaction.amount)
        .sum();

    return incomingTransfersSum - outgoingTransfersSum;
  }

  private String getTransactionsAsString(final Collection<Transaction> transactions) {
    return Arrays.deepToString(transactions.toArray());
  }

  private Pair<String, Integer> calculateHash(final LocalDateTime created, final String previousHash,
      final List<Transaction> transactions, final int difficulty) {
    final String prefix = StringUtils.repeat("0", difficulty);

    final String transactionsAsString = getTransactionsAsString(transactions);

    String hash = DigestUtils.sha256Hex("a");
    int nonce = 0;

    while (!hash.substring(0, difficulty).equals(prefix)) {
      nonce++;
      hash = DigestUtils.sha256Hex(created + previousHash + nonce + transactionsAsString);
    }

    return Pair.of(hash, nonce);
  }
}
