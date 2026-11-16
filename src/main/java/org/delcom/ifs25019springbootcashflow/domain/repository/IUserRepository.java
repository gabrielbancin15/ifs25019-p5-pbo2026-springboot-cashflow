package org.delcom.ifs25019springbootcashflow.domain.repository;
import org.delcom.ifs25019springbootcashflow.domain.User; import java.util.*;
public interface IUserRepository extends IRepository<User,Long> { Optional<User> findByUsername(String username); }
