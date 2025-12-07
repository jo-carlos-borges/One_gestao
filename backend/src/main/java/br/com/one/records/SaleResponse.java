package br.com.one.records;

import br.com.one.domain.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record SaleResponse(
    Long id,
    ClientResponse client,
    UserSummaryResponse salesperson,
    Set<ProductSummaryResponse> products,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    BigDecimal downPayment,
    Integer numberOfInstallments,
    LocalDateTime saleDate,
    LocalDateTime baseInstallationDate,
    LocalDateTime siteInstallationDate,
    String validatorAccess,
    String vpsIp,
    UserSummaryResponse installer,
    List<InstallmentResponse> installments,
    boolean outOfHours,
    BigDecimal installationBonus,
    boolean installationBonusPaid
) {}