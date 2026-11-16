package org.delcom.ifs25019springbootcashflow.domain;
import java.time.LocalDateTime;
public record Transaction(Long id, Long userId, String description, long amount, TransactionType type, String proofPath, LocalDateTime createdAt) {}
