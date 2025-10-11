package br.com.one.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	@Builder.Default
	@Column(nullable = false, updatable = false)
	private LocalDateTime creationDate = LocalDateTime.now();
}
