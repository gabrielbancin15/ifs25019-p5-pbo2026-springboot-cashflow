package org.delcom.ifs25019springbootcashflow.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DomainTypesTest {
 @Test void domainExceptionsKeepMessages(){assertEquals("domain",new DomainException("domain").getMessage());assertEquals("missing",new EntityNotFoundException("missing").getMessage());assertEquals("invalid",new ValidationException("invalid").getMessage());}
 @Test void rolesAndSortOptionsAreAvailable(){assertEquals(2,Role.values().length);assertEquals(6,SortOption.values().length);}
}
