<script setup>
import { ref, onMounted } from 'vue';
import api from '@/services/api';

const bonuses = ref([]);
const isLoading = ref(true);
const formatCurrency = (value) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
const formatDate = (isoString) => new Date(isoString).toLocaleDateString('pt-BR');

const fetchBonuses = async () => {
  try {
    const response = await api.get('/bonuses/installations/pending');
    bonuses.value = response.data;
  } catch (error) {
    console.error("Erro ao buscar bônus:", error);
    window.$toast().fire({ icon: 'error', title: 'Erro ao carregar bônus de instalação.' });
  } finally {
    isLoading.value = false;
  }
};

const handlePayBonus = async (saleId) => {
    if(!confirm("Confirmar pagamento deste bônus ao técnico?")) return;
    try {
        await api.patch(`/bonuses/installations/${saleId}/pay`);
        window.$toast().fire({ icon: 'success', title: 'Bônus pago com sucesso!' });
        await fetchBonuses(); // Recarrega a lista
    } catch (error) {
        console.error("Erro ao pagar:", error);
        window.$toast().fire({ icon: 'error', title: 'Erro ao processar pagamento.' });
    }
};

onMounted(fetchBonuses);
</script>

<template>
  <div>
    <h1 class="mb-4">Bônus de Instalação Pendentes</h1>
    <div class="card">
      <div class="table-responsive">
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Técnico</th>
              <th>ID Venda</th>
              <th>Data Venda</th>
              <th>Valor</th>
              <th>Ação</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="isLoading">
              <td colspan="5" class="text-center">Carregando...</td>
            </tr>
            <tr v-else-if="bonuses.length === 0">
              <td colspan="5" class="text-center">Nenhum bônus pendente.</td>
            </tr>
            <tr v-for="bonus in bonuses" :key="bonus.saleId">
              <td>{{ bonus.installerName }}</td>
              <td>#{{ bonus.saleId }}</td>
              <td>{{ formatDate(bonus.saleDate) }}</td>
              <td><strong>{{ formatCurrency(bonus.amount) }}</strong></td>
              <td>
                <button class="btn btn-success btn-sm" @click="handlePayBonus(bonus.saleId)">
                    Pagar
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

