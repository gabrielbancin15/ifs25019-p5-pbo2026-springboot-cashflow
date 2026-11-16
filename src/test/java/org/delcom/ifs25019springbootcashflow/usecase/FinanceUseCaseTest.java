package org.delcom.ifs25019springbootcashflow.usecase;

import org.delcom.ifs25019springbootcashflow.adapter.repository.InMemoryRepository;
import org.delcom.ifs25019springbootcashflow.domain.*;
import org.delcom.ifs25019springbootcashflow.domain.repository.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class FinanceUseCaseTest {
 private FinanceUseCase finance;
 private static class MemoryTransactions extends InMemoryRepository<Transaction,Long> implements ITransactionRepository {
  private long nextId=1;
  MemoryTransactions(){super(Transaction::id);}
  @Override public Transaction save(Transaction t){if(t.id()==null)t=new Transaction(nextId++,t.userId(),t.description(),t.amount(),t.type(),t.proofPath(),t.createdAt());return super.save(t);}
  @Override public List<Transaction> findByUserId(long uid){return findAll().stream().filter(t->t.userId()==uid).toList();}
 }
 @BeforeEach void setUp(){finance=new FinanceUseCase(new MemoryTransactions());}
 @Test void addIncomeAndExpenseCalculateBalance(){
  assertDoesNotThrow(()->finance.add(7,"Gaji",5_000_000,TransactionType.INCOME,null));
  assertDoesNotThrow(()->finance.add(7,"Makan",500_000,TransactionType.EXPENSE,null));
  assertEquals(4_500_000,finance.balance(7));
  assertEquals(5_000_000,finance.total(7,TransactionType.INCOME));
  assertEquals(500_000,finance.total(7,TransactionType.EXPENSE));
  assertEquals(0,finance.balance(8));
 }
 @Test void rejectInvalidTransaction() {
  assertThrows(ValidationException.class,()->finance.add(7,"",100,TransactionType.INCOME,null));
  assertThrows(ValidationException.class,()->finance.add(7,"Valid",0,TransactionType.INCOME,null));
 }
 @Test void filterAndSortTransactions(){
  assertDoesNotThrow(()->finance.add(7,"Gaji bulanan",100,TransactionType.INCOME,null));
  assertDoesNotThrow(()->finance.add(7,"Belanja",300,TransactionType.EXPENSE,null));
  assertEquals(1,finance.list(7,"gaji",SortOption.NEWEST).size());
  assertEquals(300,finance.list(7,"",SortOption.AMOUNT_HIGH).get(0).amount());
  assertEquals(100,finance.list(7,"",SortOption.AMOUNT_LOW).get(0).amount());
  assertEquals(2,finance.list(7,"",SortOption.OLDEST).size());
  assertEquals(2,finance.list(7,"",SortOption.NEWEST).size());
  assertEquals(TransactionType.INCOME,finance.list(7,"",SortOption.INCOME_FIRST).get(0).type());
  assertEquals(TransactionType.EXPENSE,finance.list(7,"",SortOption.EXPENSE_FIRST).get(0).type());
  assertEquals(2,finance.listForAdmin(7).size());
  assertEquals(2,finance.all().size());
  assertEquals(2,finance.searchAllForAdmin(7).size());
 }
 @Test void userCannotDeleteOrUpdateAnotherUsersTransaction()throws Exception{
  Transaction saved=finance.add(7,"Gaji",100,TransactionType.INCOME,null);
  assertThrows(ValidationException.class,()->finance.delete(8,saved.id()));
  assertThrows(ValidationException.class,()->finance.update(8,saved.id(),"Hack",200,TransactionType.EXPENSE));
  assertDoesNotThrow(()->finance.delete(7,saved.id()));
  assertEquals(0,finance.balance(7));
 }
 @Test void updateChangesOwnedTransaction()throws Exception{
  Transaction saved=finance.add(7,"Lama",100,TransactionType.INCOME,null);
  Transaction updated=finance.update(7,saved.id(),"Baru",250,TransactionType.EXPENSE);
  assertEquals("Baru",updated.description());
  assertEquals(250,updated.amount());
  assertEquals(-250,finance.balance(7));
 }
}
