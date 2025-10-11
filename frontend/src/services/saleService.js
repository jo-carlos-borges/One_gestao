import api from './api';

export const createSale = (saleData) => {
  return api.post('/gta-sales', saleData);
};

// Futuramente, podemos adicionar outras funções aqui, como:
// export const getSales = () => api.get('/gta-sales');
// export const getSaleById = (id) => api.get(`/gta-sales/${id}`);

