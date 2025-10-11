package br.com.one.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.one.domain.Client;
import br.com.one.domain.Installment;
import br.com.one.domain.InstallmentStatus;
import br.com.one.domain.PaymentMethod;
import br.com.one.domain.Product;
import br.com.one.domain.Sale;
import br.com.one.domain.User;
import br.com.one.mappers.SaleMapper;
import br.com.one.records.SaleRequest;
import br.com.one.records.SaleResponse;
import br.com.one.records.SaleSummaryResponse;
import br.com.one.repositories.ClientRepository;
import br.com.one.repositories.InstallmentRepository;
import br.com.one.repositories.ProductRepository;
import br.com.one.repositories.SaleRepository;
import br.com.one.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final InstallmentRepository installmentRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public SaleResponse createSale(SaleRequest request, User salesperson) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + request.clientId()));

        Set<Product> products = new HashSet<>(productRepository.findAllById(request.productIds()));
        if (products.size() != request.productIds().size()) {
            throw new EntityNotFoundException("Um ou mais produtos não foram encontrados.");
        }

        BigDecimal totalAmount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        User installer = null;
        if (request.installerId() != null) {
            installer = userRepository.findById(request.installerId())
                    .orElseThrow(() -> new EntityNotFoundException("Instalador não encontrado com ID: " + request.installerId()));
        }

        Sale sale = Sale.builder()
                .client(client)
                .salesPerson(salesperson)
                .products(products)
                .totalAmount(totalAmount)
                .paymentMethod(request.paymentMethod())
                .downPayment(request.downPayment())
                .numberOfInstallments(request.numberOfInstallments())
                .baseInstallationDate(request.baseInstallationDate())
                .siteInstallationDate(request.siteInstallationDate())
                .validatorAccess(request.validatorAccess())
                .vpsIp(request.vpsIp())
                .installer(installer)
                .build();

        Sale savedSale = saleRepository.save(sale);

        List<Installment> installments = Collections.emptyList();
        if (request.paymentMethod() == PaymentMethod.INSTALLMENTS_PLAN) {
            installments = createInstallmentsForSale(savedSale);
        }

        return SaleMapper.toResponse(savedSale, installments);
    }

    private List<Installment> createInstallmentsForSale(Sale sale) {
        BigDecimal downPayment = sale.getDownPayment() != null ? sale.getDownPayment() : BigDecimal.ZERO;
        int numberOfInstallments = sale.getNumberOfInstallments() != null ? sale.getNumberOfInstallments() : 1;

        if (numberOfInstallments <= 0) {
            throw new IllegalArgumentException("O número de parcelas deve ser maior que zero.");
        }

        BigDecimal remainingAmount = sale.getTotalAmount().subtract(downPayment);
        BigDecimal installmentAmount = remainingAmount.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);

        List<Installment> installments = new ArrayList<>();
        for (int i = 1; i <= numberOfInstallments; i++) {
            Installment installment = Installment.builder()
                    .sale(sale)
                    .amount(installmentAmount)
                    .dueDate(LocalDate.now().plusMonths(i))
                    .status(InstallmentStatus.PENDING)
                    .build();
            installments.add(installment);
        }
        return installmentRepository.saveAll(installments);
    }
    
    public Page<SaleSummaryResponse> findAll(Pageable pageable) {
        return saleRepository.findAll(pageable)
                .map(SaleMapper::toSummaryResponse);
    }
    
    public Optional<SaleResponse> findSaleDetailsById(Long id) {
    	Optional<Sale> saleOptional = saleRepository.findById(id);
    	
    	if (saleOptional.isEmpty()) {
    		return Optional.empty();
    	}
    	
    	Sale sale = saleOptional.get();
    	List<Installment> installments = installmentRepository.findBySaleId(id);
    	
    	SaleResponse response = SaleMapper.toResponse(sale, installments);
    	return Optional.of(response);
    }
}