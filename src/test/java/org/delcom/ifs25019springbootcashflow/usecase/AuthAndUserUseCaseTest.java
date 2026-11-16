package org.delcom.ifs25019springbootcashflow.usecase;

import org.delcom.ifs25019springbootcashflow.adapter.repository.InMemoryRepository;
import org.delcom.ifs25019springbootcashflow.domain.*;
import org.delcom.ifs25019springbootcashflow.domain.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class AuthAndUserUseCaseTest {
 private static class MemoryUsers extends InMemoryRepository<User,Long> implements IUserRepository {
  private long next=1;MemoryUsers(){super(User::id);}
  @Override public User save(User u){if(u.id()==null)u=new User(next++,u.username(),u.passwordHash(),u.fullName(),u.role(),u.photoPath());return super.save(u);}
  @Override public Optional<User> findByUsername(String username){return findAll().stream().filter(u->u.username().equals(username)).findFirst();}
 }
 private MemoryUsers repository;private AuthUseCase auth;private UserUseCase users;
 @BeforeEach void setup(){repository=new MemoryUsers();auth=new AuthUseCase(repository);users=new UserUseCase(repository);}
 @Test void createsRootAdminOnlyOnce(){User admin=auth.ensureRootAdmin("root","rootpass");assertEquals(Role.ADMIN,admin.role());assertEquals(admin.id(),auth.ensureRootAdmin("root","changed").id());}
 @Test void registerAndLogin(){User user=assertDoesNotThrow(()->auth.register("siswa","secret123","Nama Siswa"));assertEquals(Role.USER,user.role());assertEquals(user.id(),assertDoesNotThrow(()->auth.login("siswa","secret123")).id());}
 @Test void rejectBadRegistrationAndLogin(){assertThrows(ValidationException.class,()->auth.register("","123",""));assertThrows(ValidationException.class,()->auth.login("tidakada","salah"));assertDoesNotThrow(()->auth.register("siswa","secret123","Nama"));assertThrows(ValidationException.class,()->auth.register("siswa","secret123","Nama"));}
 @Test void updateUserProfileAndFindUser()throws Exception{User user=auth.register("siswa","secret123","Nama");assertEquals(user.id(),users.get(user.id()).id());User renamed=users.updateName(user,"Nama Baru");User changed=users.updatePassword(renamed,"passwordbaru");User photo=users.updatePhoto(changed,"/uploads/foto.png");assertEquals(1,users.list().size());assertEquals("Nama Baru",photo.fullName());assertEquals("/uploads/foto.png",photo.photoPath());assertTrue(PasswordHasher.matches("passwordbaru",photo.passwordHash()));}
 @Test void validateProfileAndMissingUser()throws Exception{User user=auth.register("siswa","secret123","Nama");assertThrows(ValidationException.class,()->users.updateName(user," "));assertThrows(ValidationException.class,()->users.updatePassword(user,"123"));assertThrows(EntityNotFoundException.class,()->users.get(999));}
}
