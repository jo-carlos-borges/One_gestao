<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import api from '@/services/api';
import { markInstallmentAsPaid } from '@/services/installmentService'; // 1. Importe o novo serviço

const route = useRoute();
const sale = ref(null);
const isLoading = ref(true);

const formatCurrency = (value) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
const formatDate = (isoString) => isoString ? new Date(isoString).toLocaleDateString('pt-BR') : 'N/A';
const formatDateTime = (isoString) => isoString ? new Date(isoString).toLocaleString('pt-BR') : 'N/A';

const paymentMethodText = {
  PIX: 'PIX',
  CREDIT_CARD: 'Cartão de Crédito',
  CREDIT_CARD_INSTALLMENTS: 'Cartão Parcelado',
  INSTALLMENTS_PLAN: 'Crediário'
};

const statusText = {
    PENDING: 'Pendente',
    PAID: 'Pago',
    OVERDUE: 'Vencido'
};

// 2. NOVA FUNÇÃO para marcar a parcela como paga
const handleMarkAsPaid = async (installmentId) => {
  if (!confirm('Deseja confirmar o pagamento desta parcela?')) return;

  try {
    const response = await markInstallmentAsPaid(installmentId);
    const updatedInstallment = response.data;

    // Atualiza a parcela na lista local sem precisar recarregar a página
    const index = sale.value.installments.findIndex(i => i.id === installmentId);
    if (index !== -1) {
      sale.value.installments[index] = updatedInstallment;
    }
    alert('Parcela marcada como paga com sucesso!');
    // Exibir toast de sucesso
  } catch (error) {
    console.error("Erro ao marcar parcela como paga:", error);
    alert('Erro ao marcar parcela como paga. Tente novamente.');
    // Exibir toast de erro
  }
};

onMounted(async () => {
  const saleId = route.params.id;
  try {
    const response = await api.get(`/gta-sales/${saleId}`);
    sale.value = response.data;
  } catch (error) {
    console.error("Erro ao buscar detalhes da venda:", error);
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div v-if="isLoading" class="text-center">
    <p>Carregando detalhes da venda...</p>
  </div>
  <div v-else-if="!sale">
    <p class="text-center">Venda não encontrada.</p>
  </div>
  <div v-else>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Detalhes da Venda #{{ sale.id }}</h1>
      <router-link to="/gta-sales" class="btn btn-secondary">Voltar para a Lista</router-link>
    </div>

    <div class="row">
      <div class="col-lg-8">
        <div v-if="sale.installments && sale.installments.length > 0" class="card mb-4">
          <div class="card-header">
            <h5 class="mb-0">Parcelas do Crediário</h5>
          </div>
          <div class="table-responsive">
            <table class="table table-striped mb-0">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Valor</th>
                  <th>Vencimento</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(installment, index) in sale.installments" :key="installment.id">
                  <td>{{ index + 1 }}</td>
                  <td>{{ formatCurrency(installment.amount) }}</td>
                  <td>{{ formatDate(installment.dueDate) }}</td>
                  <td>
                    <span class="badge" :class="{
                        'bg-warning text-dark': installment.status === 'PENDING',
                        'bg-success': installment.status === 'PAID',
                        'bg-danger': installment.status === 'OVERDUE'
                    }">{{ statusText[installment.status] }}</span>
                  </td>
                  <td>
                    <button v-if="installment.status !== 'PAID'" 
                            class="btn btn-sm btn-outline-success"
                            @click="handleMarkAsPaid(installment.id)">
                      Marcar como Paga
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <h5 class="mb-0">Produtos Vendidos</h5>
          </div>
          <ul class="list-group list-group-flush">
            <li v-for="product in sale.products" :key="product.id" class="list-group-item d-flex justify-content-between">
              <span>{{ product.name }}</span>
              <strong>{{ formatCurrency(product.price) }}</strong>
            </li>
          </ul>
        </div>
      </div>

      <div class="col-lg-4">
        <div class="card mb-4">
          <div class="card-header">
            <h5 class="mb-0">Informações da Venda</h5>
          </div>
          <div class="card-body">
            <p><strong>Cliente:</strong> {{ sale.client.name }}</p>
            <p><strong>Vendedor:</strong> {{ sale.salesperson.name }}</p>
            <p><strong>Data:</strong> {{ formatDateTime(sale.saleDate) }}</p>
            <p><strong>Valor Total:</strong> <strong>{{ formatCurrency(sale.totalAmount) }}</strong></p>
            <p><strong>Forma de Pagamento:</strong> {{ paymentMethodText[sale.paymentMethod] }}</p>
            <div v-if="sale.paymentMethod === 'INSTALLMENTS_PLAN'">
                <p><strong>Valor da Entrada:</strong> {{ formatCurrency(sale.downPayment || 0) }}</p>
            </div>
          </div>
        </div>

        <div class="card">
            <div class="card-header"><h5 class="mb-0">Detalhes da Instalação</h5></div>
            <div class="card-body">
                <p><strong>Instalador:</strong> {{ sale.installer ? sale.installer.name : 'Não definido' }}</p>
                <p><strong>IP da VPS:</strong> {{ sale.vpsIp || 'Não informado' }}</p>
                <p><strong>Acesso Validador:</strong> {{ sale.validatorAccess || 'Não informado' }}</p>
                <p><strong>Instalação da Base:</strong> {{ formatDateTime(sale.baseInstallationDate) }}</p>
                <p><strong>Instalação do Site:</strong> {{ formatDateTime(sale.siteInstallationDate) }}</p>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>

