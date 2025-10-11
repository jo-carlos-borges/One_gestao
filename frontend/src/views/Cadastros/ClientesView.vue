<script setup>
import { ref, onMounted, inject, computed } from 'vue'; // Adicione 'computed'
import api from '@/services/api';
import { Plus, Pencil, Trash2 } from 'lucide-vue-next';

const isDark = inject('isDark');
const clientes = ref([]);
const empresas = ref([]); // Para guardar a lista de empresas
const showForm = ref(false);

const novoCliente = ref({
  nome: '',
  discordId: '',
  empresaId: null, // Campo para o ID da empresa selecionada
});

// Computada para verificar se a empresa selecionada é 'Monkey'
const isMonkeySelected = computed(() => {
  const selectedEmpresa = empresas.value.find(e => e.id === novoCliente.value.empresaId);
  // Usando toLowerCase para não depender de maiúsculas/minúsculas
  return selectedEmpresa?.nome.toLowerCase() === 'monkey';
});

const fetchInitialData = async () => {
  try {
    // Busca clientes e empresas em paralelo
    const [clientesResponse, empresasResponse] = await Promise.all([
      api.get('/clientes'),
      api.get('/empresas')
    ]);
    clientes.value = clientesResponse.data;
    empresas.value = empresasResponse.data;
  } catch (error) {
    console.error("Erro ao buscar dados iniciais:", error);
    window.$toast().fire({ icon: 'error', title: 'Não foi possível carregar os dados da página.' });
  }
};

const handleNovoCliente = async () => {
  if (!novoCliente.value.nome || novoCliente.value.nome.trim() === '') {
    window.$toast().fire({ icon: 'warning', title: 'O nome do cliente é obrigatório.' });
    return;
  }
  if (!novoCliente.value.empresaId) {
    window.$toast().fire({ icon: 'warning', title: 'Selecione uma empresa.' });
    return;
  }

  try {
    await api.post('/clientes', novoCliente.value);
    window.$toast().fire({ icon: 'success', title: 'Cliente cadastrado com sucesso!' });

    // Resetar formulário
    novoCliente.value = { nome: '', discordId: '', empresaId: null };
    showForm.value = false;
    await fetchInitialData();
  } catch (error) {
    const msg = error?.response?.data?.message || 'Não foi possível cadastrar o cliente.';
    window.$toast().fire({ icon: 'error', title: msg });
  }
};

onMounted(fetchInitialData);
</script>

<template>
  <div class="col-12">
    <div class="card card-hover" :class="isDark ? 'color__theme__dark text-white' : 'bg-white text-dark'">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="card-title mb-0">Cadastro de Clientes</h4>
        <button class="btn btn-primary btn-sm" @click="showForm = !showForm">
          <Plus :size="16" class="me-1" />
          Novo Cliente
        </button>
      </div>
      <div class="card-body">
        <div v-if="showForm" class="p-3 mb-4 rounded" :class="isDark ? 'bg-dark' : 'bg-light'">
          <form @submit.prevent="handleNovoCliente">
            <div class="row g-3 align-items-end">
              <div class="col-md-6">
                <label for="empresaCliente" class="form-label">Empresa</label>
                <select id="empresaCliente" class="form-select" v-model="novoCliente.empresaId">
                  <option :value="null" disabled>Selecione...</option>
                  <option v-for="empresa in empresas" :key="empresa.id" :value="empresa.id">
                    {{ empresa.nome }}
                  </option>
                </select>
              </div>
              <div class="col-md-6">
                <label for="nomeCliente" class="form-label">Nome do Cliente</label>
                <input type="text" class="form-control" id="nomeCliente" v-model="novoCliente.nome" placeholder="Nome Completo">
              </div>
              <div v-if="isMonkeySelected" class="col-md-12">
                <label for="discordId" class="form-label">ID do Discord</label>
                <input type="text" class="form-control" id="discordId" v-model="novoCliente.discordId" placeholder="usuario#1234">
              </div>
            </div>
             <div class="row mt-3">
              <div class="col-12 text-end">
                 <button type="button" class="btn btn-secondary me-2" @click="showForm = false">Cancelar</button>
                 <button type="submit" class="btn btn-success">Salvar Cliente</button>
              </div>
            </div>
          </form>
        </div>

        <div class="table-responsive">
          <table class="table table-hover" :class="{ 'table-dark': isDark }">
            <thead class="table-light" :class="{'table-dark': isDark}">
              <tr>
                <th scope="col">Nome</th>
                <th scope="col">Empresa</th>
                <th scope="col">Discord</th>
                <th scope="col">Data Cadastro</th>
                <th scope="col" class="text-end">Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="clientes.length === 0">
                <td colspan="5" class="text-center">Nenhum cliente cadastrado.</td>
              </tr>
              <tr v-for="cliente in clientes" :key="cliente.id">
                <td>{{ cliente.nome }}</td>
                <td>
                  <span class="badge" :class="cliente.empresa.nome.toLowerCase() === 'monkey' ? 'bg-primary' : 'bg-info'">
                    {{ cliente.empresa.nome }}
                  </span>
                </td>
                <td>{{ cliente.discordId || 'N/A' }}</td>
                <td>{{ new Date(cliente.dataCadastro).toLocaleDateString('pt-BR') }}</td>
                <td class="text-end">
                  <button class="btn btn-outline-warning btn-sm me-2" @click="alert('Editar cliente ' + cliente.id)">
                    <Pencil :size="14" />
                  </button>
                  <button class="btn btn-outline-danger btn-sm" @click="alert('Deletar cliente ' + cliente.id)">
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
