package org.delcom.ifs25019springbootcashflow.domain.repository;
import org.delcom.ifs25019springbootcashflow.domain.*; import java.util.*;
public interface ITransactionRepository extends IRepository<Transaction,Long> { List<Transaction> findByUserId(long userId); }
