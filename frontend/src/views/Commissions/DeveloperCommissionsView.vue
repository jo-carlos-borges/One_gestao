<script setup>
import { ref, onMounted } from 'vue';
import api from '@/services/api';

const scores = ref([]);
const isLoading = ref(true);

onMounted(async () => {
  try {
    const response = await api.get('/commissions/developers');
    scores.value = response.data;
  } catch (error) {
    console.error("Erro ao buscar pontuação dos desenvolvedores:", error);
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Comissões - Pontuação dos Desenvolvedores</h1>
    </div>
    <div class="card">
      <div class="card-header">
        Ranking de Pontos
      </div>
      <div class="table-responsive">
        <table class="table table-hover table-striped mb-0">
          <thead>
            <tr>
              <th>Posição</th>
              <th>Desenvolvedor</th>
              <th class="text-center">Pontuação Total</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="isLoading">
              <td colspan="3" class="text-center">Carregando...</td>
            </tr>
            <tr v-else-if="scores.length === 0">
              <td colspan="3" class="text-center">Nenhuma pontuação encontrada.</td>
            </tr>
            <tr v-for="(score, index) in scores" :key="score.developerId">
              <td><strong>#{{ index + 1 }}</strong></td>
              <td>{{ score.developerName }}</td>
              <td class="text-center">
                <span class="badge bg-primary fs-6">{{ score.totalPoints }}</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

