package br.com.one.domain;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@Column(length = 18)
	private String document;
	
	@Column(nullable = false, unique = true, length = 150)
	private String email;
	
	@Column(length = 20)
	private String phone;
	
	@Column(length = 100)
	private String discordId;
	
	@ElementCollection(targetClass = BusinessUnit.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "client_business_units", joinColumns = @JoinColumn(name = "client_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "business_unit")
    private Set<BusinessUnit> businessUnits;
	
	@Builder.Default
	@Column(nullable = false, updatable = false)
	private LocalDateTime creationDate = LocalDateTime.now();
}
