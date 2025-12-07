package br.com.one.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "client_id")
	private Client client;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "salesperson_id")
	private User salesPerson;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sale_products",
        joinColumns = @JoinColumn(name = "sale_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal downPayment;
    
    private Integer numberOfInstallments;
    
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime saleDate = LocalDateTime.now();
    
    // --- Informações da Instalação ---
    private LocalDateTime baseInstallationDate;
    private LocalDateTime siteInstallationDate;
    private String validatorAccess;
    private String vpsIp;
    
    @ManyToOne
    @JoinColumn(name = "installer_id")
    private User installer;
    
    @Column(nullable = false)
    private boolean outOfHours;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal installationBonus;
    
    @Column(nullable = false)
    private boolean installationBonusPaid;
}
