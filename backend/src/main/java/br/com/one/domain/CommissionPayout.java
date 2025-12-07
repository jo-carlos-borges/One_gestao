package br.com.one.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class CommissionPayout {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "salesperson_id")
	private User salesperon;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amountPaid;
	
	@Column(nullable = false)
	private LocalDateTime payoutDate;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "admin_user_id")
    private User processedBy;
}
