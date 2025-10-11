<script setup>
import { ref, onMounted } from 'vue';
import { Plus, Eye } from 'lucide-vue-next';
import api from '@/services/api';

const sales = ref([]);
const isLoading = ref(true);

// Formata o valor monetário para exibição
const formatCurrency = (value) => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value);
};

// Formata a data e hora
const formatDateTime = (isoString) => {
  if (!isoString) return 'N/A';
  const date = new Date(isoString);
  return date.toLocaleString('pt-BR');
};

// Mapeia os métodos de pagamento para texto legível
const paymentMethodText = {
  PIX: 'PIX',
  CREDIT_CARD: 'Cartão de Crédito',
  CREDIT_CARD_INSTALLMENTS: 'Cartão Parcelado',
  INSTALLMENTS_PLAN: 'Crediário'
};

// Busca as vendas da API
const fetchSales = async () => {
  isLoading.value = true;
  try {
    const response = await api.get('/gta-sales');
    sales.value = response.data.content || response.data;
  } catch (error) {
    console.error("Erro ao buscar vendas:", error);
    window.$toast().fire({ icon: 'error', title: 'Não foi possível carregar as vendas.' });
  } finally {
    isLoading.value = false;
  }
};

onMounted(fetchSales);
</script>
<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Vendas (FiveM)</h1>
      <router-link to="/gta-sales/new" class="btn btn-primary d-flex align-items-center">
        <Plus size="16" class="me-2" />
        Nova Venda
      </router-link>
    </div>
    <div class="card">
      <div class="table-responsive">
        <table class="table table-hover table-striped mb-0">
          <thead>
            <tr>
              <th>ID</th>
              <th>Cliente</th>
              <th>Vendedor</th>
              <th>Valor Total</th>
              <th>Pagamento</th>
              <th>Data da Venda</th>
              <th class="text-end">Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="isLoading">
              <td colspan="7" class="text-center">Carregando...</td>
            </tr>
            <tr v-else-if="sales.length === 0">
              <td colspan="7" class="text-center">Nenhuma venda encontrada.</td>
            </tr>
            <tr v-for="sale in sales" :key="sale.id">
              <td>#{{ sale.id }}</td>
              <td>{{ sale.clientName }}</td>
              <td>{{ sale.salespersonName }}</td>
              <td>{{ formatCurrency(sale.totalAmount) }}</td>
              <td>
                <span class="badge" :class="{
                  'bg-info': sale.paymentMethod === 'PIX',
                  'bg-primary': sale.paymentMethod && sale.paymentMethod.includes('CREDIT_CARD'),
                  'bg-warning text-dark': sale.paymentMethod === 'INSTALLMENTS_PLAN'
                }">
                  {{ paymentMethodText[sale.paymentMethod] || sale.paymentMethod }}
                </span>
              </td>
              <td>{{ formatDateTime(sale.saleDate) }}</td>
              <td class="text-end">
                <router-link :to="`/gta-sales/${sale.id}`" class="btn btn-sm btn-outline-secondary" title="Visualizar Detalhes">
                  <Eye size="14" />
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      </div>
  </div>
</template>
