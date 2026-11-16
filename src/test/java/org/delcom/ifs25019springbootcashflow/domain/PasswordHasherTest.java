package org.delcom.ifs25019springbootcashflow.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class PasswordHasherTest {
 @Test void hashesAreStableAndMatch(){String hash=PasswordHasher.hash("rahasia123");assertEquals(64,hash.length());assertTrue(PasswordHasher.matches("rahasia123",hash));assertFalse(PasswordHasher.matches("salah",hash));}
}
