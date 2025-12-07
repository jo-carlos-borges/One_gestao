<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import api from '@/services/api';
import { getSubscriptionById } from '@/services/subscriptionService';

const route = useRoute();
const subscription = ref(null);
const isLoading = ref(true);

const formatCurrency = (val) => val ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val) : '-';
const formatDate = (val) => val ? new Date(val).toLocaleDateString('pt-BR') : '-';

const fetchDetails = async () => {
  try {
    const response = await getSubscriptionById(route.params.id);
    subscription.value = response.data;
  } catch (error) {
    console.error("Erro ao carregar detalhes:", error);
    window.$toast().fire({ icon: 'error', title: 'Não foi possível carregar os detalhes da assinatura.' });
  } finally {
    isLoading.value = false;
  }
};

const payInstallment = async (installmentId) => {
  if(!confirm("Confirmar o recebimento desta parcela?")) return;
  try {
    await api.patch(`/installments/${installmentId}/pay`);
    window.$toast().fire({ icon: 'success', title: 'Parcela marcada como paga!' });
    fetchDetails(); // Recarrega para atualizar o status
  } catch (error) {
    console.error("Erro ao processar pagamento:", error);
    window.$toast().fire({ icon: 'error', title: 'Erro ao processar pagamento.' });
  }
};

const activateContract = async () => {
  if(!confirm("Tem certeza? Isso mudará o status para ATIVO e permitirá cobranças mensais.")) return;
  try {
    await api.patch(`/subscriptions/${route.params.id}/activate`);
    window.$toast().fire({ icon: 'success', title: 'Contrato ativado com sucesso!' });
    fetchDetails(); // Recarrega
  } catch (error) {
    console.error("Erro ao ativar contrato:", error);
    window.$toast().fire({ icon: 'error', title: 'Erro ao ativar contrato.' });
  }
};

onMounted(fetchDetails);
</script>

<template>
  <div v-if="subscription">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Detalhes do Contrato #{{ subscription.id }}</h1>
      <router-link to="/subscriptions" class="btn btn-secondary">Voltar</router-link>
    </div>

    <div class="row mb-4">
      <div class="col-md-4">
        <div class="card h-100 border-primary">
          <div class="card-body">
            <h5 class="card-title text-primary">Mensalidade</h5>
            <h2 class="display-6">{{ formatCurrency(subscription.monthlyFee) }}</h2>
            <p class="mb-0">Cliente: <strong>{{ subscription.clientName }}</strong></p>
            <p>Software: {{ subscription.softwareName }}</p>
          </div>
        </div>
      </div>
      <div class="col-md-4">
         <div class="card h-100">
          <div class="card-body">
            <h5 class="card-title">Status</h5>
            <span class="badge fs-5 mb-2" 
                  :class="{
                    'bg-warning': subscription.status === 'IMPLEMENTATION',
                    'bg-success': subscription.status === 'ACTIVE',
                    'bg-danger': subscription.status === 'SUSPENDED' || subscription.status === 'CANCELLED'
                  }">
              {{ subscription.status === 'IMPLEMENTATION' ? 'Em Implementação' : 
                 subscription.status === 'ACTIVE' ? 'Ativo' : 
                 subscription.status === 'SUSPENDED' ? 'Suspenso' : 'Cancelado' }}
            </span>
            <p class="text-muted small">
              O contrato iniciou em: {{ formatDate(subscription.startDate) }}
            </p>
            <div v-if="subscription.status === 'IMPLEMENTATION'" class="mt-3">
              <button class="btn btn-success w-100" @click="activateContract">
                <i class="bi bi-check-circle"></i> Ativar Contrato
              </button>
              <small class="text-muted d-block mt-2">
                Clique ao finalizar a implantação. Isso habilitará a geração de mensalidades.
              </small>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card h-100 bg-light">
           <div class="card-body">
             <h5 class="card-title">Taxa de Implementação</h5>
             <h3 class="text-dark">{{ formatCurrency(subscription.implementationFee) }}</h3>
             <p v-if="subscription.implementationFee === 0" class="text-success">Isento</p>
           </div>
        </div>
      </div>
    </div>

    <div class="card" v-if="subscription.implementationInstallments && subscription.implementationInstallments.length > 0">
      <div class="card-header">
        <strong>Financeiro - Taxa de Implementação</strong>
      </div>
      <div class="table-responsive">
        <table class="table table-striped mb-0">
          <thead>
            <tr>
              <th>Vencimento</th>
              <th>Valor</th>
              <th>Pagamento</th>
              <th>Status</th>
              <th>Ação</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="inst in subscription.implementationInstallments" :key="inst.id">
              <td>{{ formatDate(inst.dueDate) }}</td>
              <td>{{ formatCurrency(inst.amount) }}</td>
              <td>{{ formatDate(inst.paymentDate) }}</td>
              <td>
                <span class="badge" :class="inst.status === 'PAID' ? 'bg-success' : 'bg-secondary'">
                  {{ inst.status === 'PAID' ? 'PAGO' : 'PENDENTE' }}
                </span>
              </td>
              <td>
                <button v-if="inst.status === 'PENDING'" 
                        class="btn btn-sm btn-success" 
                        @click="payInstallment(inst.id)">
                  Receber
                </button>
                <span v-else class="text-muted">-</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div v-else class="alert alert-secondary">
        Não há parcelas de implementação geradas para este contrato.
    </div>

  </div>
  <div v-else-if="isLoading" class="text-center mt-5">
    <div class="spinner-border text-primary" role="status">
      <span class="visually-hidden">Carregando...</span>
    </div>
  </div>
</template>

