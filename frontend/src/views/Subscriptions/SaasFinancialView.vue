<script setup>
import { ref, onMounted } from 'vue';
import api from '@/services/api';

const installments = ref([]);
const isLoading = ref(true);
const isGenerating = ref(false);

const formatCurrency = (val) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val);
const formatDate = (val) => val ? new Date(val).toLocaleDateString('pt-BR') : '-';

const fetchFinancials = async () => {
  try {
    const response = await api.get('/subscriptions/financial');
    installments.value = response.data;
  } catch (error) {
    console.error("Erro ao carregar financeiro:", error);
    window.$toast().fire({ icon: 'error', title: 'Erro ao carregar dados financeiros.' });
  } finally {
    isLoading.value = false;
  }
};

const generateInvoices = async () => {
  if(!confirm("Deseja gerar as faturas de mensalidade para TODOS os clientes ativos referente a este mês?")) return;

  isGenerating.value = true;
  try {
    const response = await api.post('/subscriptions/generate-invoices');
    window.$toast().fire({ icon: 'success', title: response.data || 'Faturas geradas com sucesso!' });
    fetchFinancials();
  } catch (error) {
    console.error(error);
    window.$toast().fire({ icon: 'error', title: 'Erro ao gerar faturas.' });
  } finally {
    isGenerating.value = false;
  }
};

// Função genérica para baixar parcela (igual a usada em outros lugares)
const payInstallment = async (id) => {
  if(!confirm("Confirmar recebimento?")) return;
  try {
    await api.patch(`/installments/${id}/pay`);
    window.$toast().fire({ icon: 'success', title: 'Parcela marcada como paga!' });
    fetchFinancials();
  } catch(e) {
    console.error("Erro ao processar pagamento:", e);
    window.$toast().fire({ icon: 'error', title: 'Erro ao processar pagamento.' });
  }
}

onMounted(fetchFinancials);
</script>

<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Financeiro SaaS</h1>
      <button class="btn btn-primary" @click="generateInvoices" :disabled="isGenerating">
        <span v-if="isGenerating" class="spinner-border spinner-border-sm me-2"></span>
        Gerar Mensalidades do Mês
      </button>
    </div>

    <div v-if="isLoading" class="text-center mt-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Carregando...</span>
      </div>
    </div>

    <div v-else class="card">
      <div class="table-responsive">
        <table class="table table-hover table-striped mb-0">
          <thead>
            <tr>
              <th>Vencimento</th>
              <th>Cliente</th>
              <th>Descrição</th>
              <th>Valor</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="installments.length === 0">
              <td colspan="6" class="text-center">Nenhum lançamento encontrado.</td>
            </tr>
            <tr v-for="inst in installments" :key="inst.id">
              <td>{{ formatDate(inst.dueDate) }}</td>
              <td>{{ inst.clientName || '-' }}</td>
              <td>{{ inst.description || 'Parcela' }}</td>
              <td><strong>{{ formatCurrency(inst.amount) }}</strong></td>
              <td>
                <span class="badge" :class="inst.status === 'PAID' ? 'bg-success' : 'bg-warning text-dark'">
                  {{ inst.status === 'PAID' ? 'PAGO' : 'PENDENTE' }}
                </span>
              </td>
              <td>
                <button v-if="inst.status !== 'PAID'" class="btn btn-sm btn-success" @click="payInstallment(inst.id)">
                  Receber
                </button>
                <span v-else class="text-muted">-</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

