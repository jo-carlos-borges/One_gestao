package br.com.one.mappers;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.one.domain.Installment;
import br.com.one.domain.Sale;
import br.com.one.records.ClientResponse;
import br.com.one.records.InstallmentResponse;
import br.com.one.records.ProductSummaryResponse;
import br.com.one.records.SaleResponse;
import br.com.one.records.SaleSummaryResponse;
import br.com.one.records.UserSummaryResponse;

public class SaleMapper {
    private SaleMapper() {}

    public static SaleSummaryResponse toSummaryResponse(Sale sale) {
        return new SaleSummaryResponse(
                sale.getId(),
                sale.getClient().getName(),
                sale.getSalesPerson().getName(),
                sale.getTotalAmount(),
                sale.getPaymentMethod(),
                sale.getSaleDate()
        );
    }
    
 // NOVO MÉTODO PARA A RESPOSTA DETALHADA
    public static SaleResponse toResponse(Sale sale, List<Installment> installments) {
        ClientResponse clientResponse = ClientMapper.toResponse(sale.getClient());
        UserSummaryResponse salespersonResponse = new UserSummaryResponse(sale.getSalesPerson().getId(), sale.getSalesPerson().getName());

        UserSummaryResponse installerResponse = null;
        if (sale.getInstaller() != null) {
            installerResponse = new UserSummaryResponse(sale.getInstaller().getId(), sale.getInstaller().getName());
        }

        Set<ProductSummaryResponse> productResponses = sale.getProducts().stream()
                .map(p -> new ProductSummaryResponse(p.getId(), p.getName(), p.getPrice()))
                .collect(Collectors.toSet());

        List<InstallmentResponse> installmentResponses = installments != null ? installments.stream()
                .map(i -> new InstallmentResponse(i.getId(), i.getAmount(), i.getDueDate(), i.getPaymentDate(), i.getStatus()))
                .collect(Collectors.toList()) : Collections.emptyList();

        return new SaleResponse(
                sale.getId(),
                clientResponse,
                salespersonResponse,
                productResponses,
                sale.getTotalAmount(),
                sale.getPaymentMethod(),
                sale.getDownPayment(),
                sale.getNumberOfInstallments(),
                sale.getSaleDate(),
                sale.getBaseInstallationDate(),
                sale.getSiteInstallationDate(),
                sale.getValidatorAccess(),
                sale.getVpsIp(),
                installerResponse,
                installmentResponses,
                sale.isOutOfHours(),
                sale.getInstallationBonus(),
                sale.isInstallationBonusPaid()
        );
    }
}