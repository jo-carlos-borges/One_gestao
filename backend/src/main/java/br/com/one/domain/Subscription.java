package br.com.one.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "installments")
@ToString(exclude = "installments")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "salesperson_id")
    private User salesperson;

    @Column(nullable = false)
    private String softwareName;

    // DADOS DA IMPLEMENTAÇÃO (SETUP)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal implementationFee;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod implementationPaymentMethod;

    // DADOS DA MENSALIDADE (RECORRÊNCIA)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyFee;

    @Column(nullable = false)
    private int dueDay; // Dia de vencimento da mensalidade (ex: dia 10)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private LocalDateTime startDate;

    // As parcelas aqui referem-se APENAS à taxa de implementação
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Installment> installments;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();
}