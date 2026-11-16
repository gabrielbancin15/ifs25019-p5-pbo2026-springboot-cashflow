package org.delcom.ifs25019springbootcashflow.adapter.repository;

import org.delcom.ifs25019springbootcashflow.domain.Role;
import org.delcom.ifs25019springbootcashflow.domain.User;
import org.delcom.ifs25019springbootcashflow.domain.repository.IUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements IUserRepository {
 private final JdbcTemplate db;
 public JdbcUserRepository(JdbcTemplate db){this.db=db;}
 private User map(java.sql.ResultSet r,int n)throws java.sql.SQLException{return new User(r.getLong("id"),r.getString("username"),r.getString("password_hash"),r.getString("full_name"),Role.valueOf(r.getString("role")),r.getString("photo_path"));}
 public Optional<User> findByUsername(String u){return db.query("SELECT * FROM app_users WHERE username=?",this::map,u).stream().findFirst();}
 public Optional<User> findById(Long id){return db.query("SELECT * FROM app_users WHERE id=?",this::map,id).stream().findFirst();}
 public List<User> findAll(){return db.query("SELECT * FROM app_users ORDER BY id",this::map);}
 public User save(User u){
  if(u.id()==null){
   KeyHolder keys=new GeneratedKeyHolder();
   db.update(con->{var ps=con.prepareStatement("INSERT INTO app_users(username,password_hash,full_name,role,photo_path) VALUES(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);ps.setString(1,u.username());ps.setString(2,u.passwordHash());ps.setString(3,u.fullName());ps.setString(4,u.role().name());ps.setString(5,u.photoPath());return ps;},keys);
   Number id=keys.getKey();if(id==null)throw new IllegalStateException("ID pengguna tidak terbentuk.");return findById(id.longValue()).orElseThrow();
  }
  db.update("UPDATE app_users SET full_name=?,password_hash=?,photo_path=? WHERE id=?",u.fullName(),u.passwordHash(),u.photoPath(),u.id());
  return findById(u.id()).orElseThrow();
 }
 public void deleteById(Long id){db.update("DELETE FROM app_users WHERE id=?",id);}
}
