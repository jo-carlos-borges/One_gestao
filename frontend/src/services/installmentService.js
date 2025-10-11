// src/services/installmentService.js
import api from './api';

export const markInstallmentAsPaid = (installmentId) => {
  // Usamos o método PATCH para a URL específica que criamos
  return api.patch(`/installments/${installmentId}/pay`);
};

