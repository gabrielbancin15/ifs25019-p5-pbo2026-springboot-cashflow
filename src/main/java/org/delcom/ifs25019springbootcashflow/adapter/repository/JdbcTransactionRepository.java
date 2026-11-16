package org.delcom.ifs25019springbootcashflow.adapter.repository;

import org.delcom.ifs25019springbootcashflow.domain.Transaction;
import org.delcom.ifs25019springbootcashflow.domain.TransactionType;
import org.delcom.ifs25019springbootcashflow.domain.repository.ITransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTransactionRepository implements ITransactionRepository {
 private final JdbcTemplate db;
 public JdbcTransactionRepository(JdbcTemplate db){this.db=db;}
 private Transaction map(java.sql.ResultSet r,int n)throws java.sql.SQLException{
  Timestamp created=r.getTimestamp("created_at");
  return new Transaction(r.getLong("id"),r.getLong("user_id"),r.getString("description"),r.getLong("amount"),TransactionType.valueOf(r.getString("type")),r.getString("proof_path"),created.toLocalDateTime());
 }
 public List<Transaction> findByUserId(long uid){return db.query("SELECT * FROM transactions WHERE user_id=? ORDER BY created_at DESC",this::map,uid);}
 public Optional<Transaction> findById(Long id){return db.query("SELECT * FROM transactions WHERE id=?",this::map,id).stream().findFirst();}
 public List<Transaction> findAll(){return db.query("SELECT * FROM transactions ORDER BY created_at DESC",this::map);}
 public Transaction save(Transaction t){
  if(t.id()==null){
   KeyHolder keys=new GeneratedKeyHolder();
   PreparedStatementCreator sql=con->{var ps=con.prepareStatement("INSERT INTO transactions(user_id,description,amount,type,proof_path,created_at) VALUES(?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);ps.setLong(1,t.userId());ps.setString(2,t.description());ps.setLong(3,t.amount());ps.setString(4,t.type().name());ps.setString(5,t.proofPath());ps.setTimestamp(6,Timestamp.valueOf(t.createdAt()));return ps;};
   db.update(sql,keys);Number id=keys.getKey();if(id==null)throw new IllegalStateException("ID transaksi tidak terbentuk.");return findById(id.longValue()).orElseThrow();
  }
  db.update("UPDATE transactions SET description=?,amount=?,type=?,proof_path=? WHERE id=? AND user_id=?",t.description(),t.amount(),t.type().name(),t.proofPath(),t.id(),t.userId());
  return findById(t.id()).orElseThrow();
 }
 public void deleteById(Long id){db.update("DELETE FROM transactions WHERE id=?",id);}
}
