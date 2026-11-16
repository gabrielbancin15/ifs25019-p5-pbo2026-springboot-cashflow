package org.delcom.ifs25019springbootcashflow.domain;

import java.util.Comparator;

public enum SortOption {
 NEWEST(Comparator.comparing(Transaction::createdAt).reversed()),
 OLDEST(Comparator.comparing(Transaction::createdAt)),
 AMOUNT_HIGH(Comparator.comparingLong(Transaction::amount).reversed()),
 AMOUNT_LOW(Comparator.comparingLong(Transaction::amount)),
 INCOME_FIRST(Comparator.comparing((Transaction t)->t.type()!=TransactionType.INCOME).thenComparing(Transaction::createdAt,Comparator.reverseOrder())),
 EXPENSE_FIRST(Comparator.comparing((Transaction t)->t.type()!=TransactionType.EXPENSE).thenComparing(Transaction::createdAt,Comparator.reverseOrder()));
 private final Comparator<Transaction> comparator;
 SortOption(Comparator<Transaction> comparator){this.comparator=comparator;}
 public Comparator<Transaction> comparator(){return comparator;}
}
