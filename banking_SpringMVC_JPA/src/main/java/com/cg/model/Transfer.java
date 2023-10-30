package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private Customer sender;
    @ManyToOne
    @JoinColumn(name = "recipient", referencedColumnName = "id", nullable = false)
    private Customer recipient;
    @Column(name = "transfer_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transferAmount;

    private Long fees;

    @Column(name = "fees_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal feesAmount;
    @Column(name = "transaction_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;
    @Column(nullable = false, unique = true, columnDefinition = "DATETIME")
    private LocalDateTime timeTransfer;
    private Boolean deleted;
}
