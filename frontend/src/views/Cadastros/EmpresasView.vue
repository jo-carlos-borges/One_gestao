<script setup>
import { ref, onMounted, inject } from 'vue';
import api from '@/services/api';
// Importando mais ícones que vamos usar
import { Plus, Pencil, Trash2 } from 'lucide-vue-next';

const isDark = inject('isDark');
const empresas = ref([]);
const showForm = ref(false); // Novo: controla a visibilidade do formulário

const novaEmpresa = ref({
  nome: ''
});

const fetchEmpresas = async () => {
  try {
    const response = await api.get('/empresas');
    empresas.value = response.data;
  } catch (error) {
    console.error("Erro ao buscar empresas:", error);
    window.$toast().fire({
        icon: 'error',
        title: 'Não foi possível carregar as empresas.'
    });
  }
};

const handleNovaEmpresa = async () => {
  if (!novaEmpresa.value.nome || novaEmpresa.value.nome.trim() === '') {
    window.$toast().fire({
        icon: 'warning',
        title: 'O nome da empresa não pode ser vazio.'
    });
    return;
  }

  try {
    await api.post('/empresas', novaEmpresa.value);
    
    window.$toast().fire({
        icon: 'success',
        title: 'Empresa cadastrada com sucesso!'
    });

    novaEmpresa.value.nome = '';
    showForm.value = false; // Novo: esconde o formulário após o sucesso
    await fetchEmpresas();

  } catch (error) {
    console.error("Erro ao criar empresa:", error);
    const msg = error?.response?.data?.message || 'Não foi possível cadastrar a empresa.';
    window.$toast().fire({
        icon: 'error',
        title: msg
    });
  }
};

// Funções para os botões de ação (ainda não funcionais)
const editarEmpresa = (empresa) => {
  alert(`Futuramente, aqui abriremos o modal para editar: ${empresa.nome}`);
};

const deletarEmpresa = (empresa) => {
  alert(`Futuramente, aqui confirmaremos a exclusão de: ${empresa.nome}`);
};

onMounted(fetchEmpresas);
</script>

<template>
  <div class="col-12">
    <div class="card card-hover" :class="isDark ? 'color__theme__dark text-white' : 'bg-white text-dark'">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="card-title mb-0">Cadastro de Empresas</h4>
        <button class="btn btn-primary btn-sm" @click="showForm = !showForm">
          <Plus :size="16" class="me-1" />
          Nova Empresa
        </button>
      </div>
      <div class="card-body">
        
        <div v-if="showForm" class="p-3 mb-4 rounded" :class="isDark ? 'bg-dark' : 'bg-light'">
          <form @submit.prevent="handleNovaEmpresa">
            <div class="row g-2 align-items-end">
              <div class="col-md">
                <label for="nomeEmpresa" class="form-label">Nome da Empresa</label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="nomeEmpresa" 
                  v-model="novaEmpresa.nome"
                  placeholder="Ex: Monkey"
                >
              </div>
              <div class="col-md-auto">
                <button type="submit" class="btn btn-success">Salvar</button>
              </div>
              <div class="col-md-auto">
                <button type="button" class="btn btn-secondary" @click="showForm = false">Cancelar</button>
              </div>
            </div>
          </form>
        </div>

        <div class="table-responsive">
          <table class="table table-hover" :class="{ 'table-dark': isDark }">
            <thead class="table-light" :class="{'table-dark': isDark}">
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Nome</th>
                <th scope="col" class="text-end">Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="empresas.length === 0">
                <td colspan="3" class="text-center">Nenhuma empresa cadastrada.</td>
              </tr>
              <tr v-for="empresa in empresas" :key="empresa.id">
                <td>{{ empresa.id }}</td>
                <td>{{ empresa.nome }}</td>
                <td class="text-end">
                  <button class="btn btn-outline-warning btn-sm me-2" @click="editarEmpresa(empresa)">
                    <Pencil :size="14" />
                  </button>
                  <button class="btn btn-outline-danger btn-sm" @click="deletarEmpresa(empresa)">
                    <Trash2 :size="14" />
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Estilos para um espaçamento melhor e aparência mais limpa */
.card-header {
  padding: 1rem 1.5rem;
}
.card-body {
  padding: 1.5rem;
}
.table {
  margin-bottom: 0;
}
.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
}
</style>