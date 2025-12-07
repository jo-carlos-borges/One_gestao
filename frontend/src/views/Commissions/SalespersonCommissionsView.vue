<script setup>
import { ref, onMounted } from 'vue';
import api from '@/services/api';

const commissions = ref([]);
const isLoading = ref(true);

const formatCurrency = (value) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);

onMounted(async () => {
  try {
    const response = await api.get('/commissions/salespeople');
    commissions.value = response.data;
  } catch (error) {
    console.error("Erro ao buscar comissões dos vendedores:", error);
  } finally {
    isLoading.value = false;
  }
});

const handlePayCommission = async (salespersonId) => {
  if (!confirm('Você confirma o pagamento total das comissões pendentes para este vendedor? Esta ação não pode ser desfeita.')) return;

  try {
    await api.post(`/commissions/salespeople/${salespersonId}/pay`);
    // Atualiza a lista para remover o vendedor pago
    commissions.value = commissions.value.filter(c => c.salespersonId !== salespersonId);
    // Exibir toast de sucesso
    window.$toast().fire({
      icon: 'success',
      title: 'Pagamento realizado com sucesso!'
    });
  } catch (error) {
    console.error("Erro ao pagar comissão:", error);
    // Exibir toast de erro
    window.$toast().fire({
      icon: 'error',
      title: 'Erro ao realizar o pagamento. Tente novamente.'
    });
  }
};
</script>

<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Comissões a Pagar (Vendedores)</h1>
    </div>
    <div class="card">
      <div class="card-header">
        Valores Pendentes
      </div>
      <div class="table-responsive">
        <table class="table table-hover table-striped mb-0">
          <thead>
            <tr>
              <th>Vendedor</th>
              <th class="text-end">Valor a Receber</th>
              <th class="text-center">Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="isLoading">
              <td colspan="3" class="text-center">Carregando...</td>
            </tr>
            <tr v-else-if="commissions.length === 0">
              <td colspan="3" class="text-center">Nenhuma comissão pendente.</td>
            </tr>
            <tr v-for="comm in commissions" :key="comm.salespersonId">
              <td>{{ comm.salespersonName }}</td>
              <td class="text-end">
                <strong>{{ formatCurrency(comm.totalCommissionAmount) }}</strong>
              </td>
              <td class="text-center">
                <button class="btn btn-sm btn-success" @click="handlePayCommission(comm.salespersonId)">
                  Marcar como Pago
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>


