<script setup>
import { ref, onMounted } from 'vue';
import { Plus } from 'lucide-vue-next';
import { getSubscriptions } from '@/services/subscriptionService';

const subscriptions = ref([]);
const isLoading = ref(true);

// Formata o valor monetário para exibição
const formatCurrency = (value) => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value);
};

// Formata a data
const formatDate = (isoString) => {
  if (!isoString) return 'N/A';
  const date = new Date(isoString);
  return date.toLocaleDateString('pt-BR');
};

// Mapeia os status para texto legível
const statusText = {
  IMPLEMENTATION: 'Em Implementação',
  ACTIVE: 'Ativo',
  SUSPENDED: 'Suspenso',
  CANCELLED: 'Cancelado'
};

// Busca as assinaturas da API
const fetchSubscriptions = async () => {
  isLoading.value = true;
  try {
    const response = await getSubscriptions();
    subscriptions.value = response.data.content || response.data;
  } catch (error) {
    console.error("Erro ao buscar assinaturas:", error);
    window.$toast().fire({ icon: 'error', title: 'Não foi possível carregar as assinaturas.' });
  } finally {
    isLoading.value = false;
  }
};

onMounted(fetchSubscriptions);
</script>

<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Assinaturas SaaS</h1>
      <router-link to="/subscriptions/new" class="btn btn-primary">
        <Plus class="me-2" style="width: 18px; height: 18px;" />
        Nova Assinatura
      </router-link>
    </div>

    <div v-if="isLoading" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Carregando...</span>
      </div>
    </div>

    <div v-else-if="subscriptions.length === 0" class="alert alert-info">
      Nenhuma assinatura encontrada. <router-link to="/subscriptions/new">Criar primeira assinatura</router-link>
    </div>

    <div v-else class="card">
      <div class="card-body">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>Cliente</th>
                <th>Software</th>
                <th>Status</th>
                <th>Mensalidade</th>
                <th>Vencimento</th>
                <th>Data de Criação</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="subscription in subscriptions" :key="subscription.id">
                <td>{{ subscription.clientName || 'N/A' }}</td>
                <td>{{ subscription.softwareName }}</td>
                <td>
                  <span class="badge" :class="{
                    'bg-warning': subscription.status === 'IMPLEMENTATION',
                    'bg-success': subscription.status === 'ACTIVE',
                    'bg-danger': subscription.status === 'SUSPENDED' || subscription.status === 'CANCELLED'
                  }">
                    {{ statusText[subscription.status] || subscription.status }}
                  </span>
                </td>
                <td>{{ formatCurrency(subscription.monthlyFee) }}</td>
                <td>Dia {{ subscription.dueDay }}</td>
                <td>{{ formatDate(subscription.createdAt) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

