<script setup>
import { ref, onMounted } from 'vue';
import { Plus } from 'lucide-vue-next';
import { getProjects } from '@/services/projectService';

const projects = ref([]);
const isLoading = ref(true);

const formatCurrency = (value) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
const formatDateTime = (isoString) => new Date(isoString).toLocaleString('pt-BR');

const statusText = {
  PLANNED: 'Planejado',
  IN_PROGRESS: 'Em Progresso',
  COMPLETED: 'Concluído',
  ON_HOLD: 'Em Espera',
  CANCELED: 'Cancelado'
};

onMounted(async () => {
  try {
    const response = await getProjects();
    projects.value = response.data.content;
  } catch (error) {
    console.error("Erro ao buscar projetos:", error);
  } finally {
    isLoading.value = false;
  }
});
</script>
<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Projetos (Fábrica de Software)</h1>
      <router-link to="/projects/new" class="btn btn-primary d-flex align-items-center">
        <Plus size="16" class="me-2" />
        Novo Projeto
      </router-link>
    </div>
    <div class="card">
      <div class="table-responsive">
        <table class="table table-hover table-striped mb-0">
          <thead>
            <tr>
              <th>Nome do Projeto</th>
              <th>Cliente</th>
              <th>Valor</th>
              <th>Status</th>
              <th>Desenvolvedores</th>
              <th>Data de Criação</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="isLoading"><td colspan="6" class="text-center">Carregando...</td></tr>
            <tr v-else-if="projects.length === 0"><td colspan="6" class="text-center">Nenhum projeto encontrado.</td></tr>
            <tr v-for="project in projects" :key="project.id">
              <td>{{ project.name }}</td>
              <td>{{ project.clientName }}</td>
              <td>{{ formatCurrency(project.totalValue) }}</td>
              <td>
                <span class="badge bg-secondary">{{ statusText[project.status] || project.status }}</span>
              </td>
              <td class="text-center">{{ project.developerCount }}</td>
              <td>{{ formatDateTime(project.creationDate) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
