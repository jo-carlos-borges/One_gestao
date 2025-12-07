<script setup>
import { ref, onMounted } from 'vue';
// Futuramente, vamos mover as chamadas de API para um arquivo separado (service)
import api from '@/services/api'; 
import { Plus, Edit, Trash2 } from 'lucide-vue-next';

const clients = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentClient = ref({
  name: '',
  document: '',
  email: '',
  phone: '',
  discordId: '',
  businessUnits: [] // Array para armazenar as unidades de negócio
});

// Busca os clientes da API
const fetchClients = async () => {
  try {
    const response = await api.get('/clients');
    clients.value = response.data.content; // O Spring Pageable aninha o array em 'content'
  } catch (error) {
    console.error("Erro ao buscar clientes:", error);
    // Exibir um toast de erro aqui
  }
};

// Abre o modal para um novo cliente
const openCreateModal = () => {
  isEditing.value = false;
  currentClient.value = { name: '', document: '', email: '', phone: '', discordId: '', businessUnits: [] };
  showModal.value = true;
};

// Abre o modal para editar um cliente existente
const openEditModal = (client) => {
  isEditing.value = true;
  currentClient.value = { 
    ...client,
    businessUnits: client.businessUnits || [] // Garante que seja um array
  };
  showModal.value = true;
};

// Salva ou atualiza o cliente
const saveClient = async () => {
  try {
    // Validação: pelo menos uma unidade de negócio deve ser selecionada
    if (!currentClient.value.businessUnits || currentClient.value.businessUnits.length === 0) {
      alert("Selecione pelo menos uma Unidade de Negócio.");
      return;
    }
    
    if (isEditing.value) {
      await api.put(`/clients/${currentClient.value.id}`, currentClient.value);
    } else {
      await api.post('/clients', currentClient.value);
    }
    showModal.value = false;
    fetchClients(); // Re-busca a lista para exibir a atualização
    // Exibir um toast de sucesso aqui
  } catch (error) {
    console.error("Erro ao salvar cliente:", error);
    // Exibir um toast de erro aqui
  }
};

// Deleta um cliente
const deleteClient = async (id) => {
  if (!confirm('Tem certeza que deseja excluir este cliente?')) return;
  try {
    await api.delete(`/clients/${id}`);
    fetchClients(); // Re-busca a lista
    // Exibir um toast de sucesso aqui
  } catch (error) {
    console.error("Erro ao deletar cliente:", error);
    // Exibir um toast de erro aqui
  }
};

onMounted(fetchClients);
</script>

<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Gestão de Clientes</h1>
      <button class="btn btn-primary d-flex align-items-center" @click="openCreateModal">
        <Plus size="16" class="me-2" />
        Novo Cliente
      </button>
    </div>

    <div class="card">
      <div class="table-responsive">
        <table class="table table-hover table-striped mb-0">
          <thead>
            <tr>
              <th>Nome</th>
              <th>Email</th>
              <th>Telefone</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="clients.length === 0">
              <td colspan="4" class="text-center">Nenhum cliente encontrado.</td>
            </tr>
            <tr v-for="client in clients" :key="client.id">
              <td>{{ client.name }}</td>
              <td>{{ client.email }}</td>
              <td>{{ client.phone }}</td>
              <td>
                <button class="btn btn-sm btn-outline-primary me-2" @click="openEditModal(client)">
                  <Edit size="14" />
                </button>
                <button class="btn btn-sm btn-outline-danger" @click="deleteClient(client.id)">
                  <Trash2 size="14" />
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <div v-if="showModal" class="modal fade show d-block" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ isEditing ? 'Editar Cliente' : 'Novo Cliente' }}</h5>
          <button type="button" class="btn-close" @click="showModal = false"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveClient">
            <div class="card mb-3">
              <div class="card-header">Unidades de Negócio (Onde este cliente atua?)</div>
              <div class="card-body">
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" id="fivem" value="FIVEM" v-model="currentClient.businessUnits">
                  <label class="form-check-label" for="fivem">FiveM (Scripts/Mods)</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" id="factory" value="SOFTWARE_FACTORY" v-model="currentClient.businessUnits">
                  <label class="form-check-label" for="factory">Fábrica de Software</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" id="saas" value="SAAS" v-model="currentClient.businessUnits">
                  <label class="form-check-label" for="saas">SaaS (Assinaturas)</label>
                </div>
              </div>
            </div>
            
            <div class="mb-3">
              <label for="name" class="form-label">Nome</label>
              <input type="text" class="form-control" id="name" v-model="currentClient.name" required>
            </div>
            <div class="mb-3">
              <label for="email" class="form-label">Email</label>
              <input type="email" class="form-control" id="email" v-model="currentClient.email" required>
            </div>
             <div class="mb-3">
              <label for="phone" class="form-label">Telefone</label>
              <input type="text" class="form-control" id="phone" v-model="currentClient.phone">
            </div>
            <div class="mb-3">
              <label for="document" class="form-label">Documento (CPF/CNPJ)</label>
              <input type="text" class="form-control" id="document" v-model="currentClient.document">
            </div>
            <div class="mb-3">
              <label for="discordId" class="form-label">ID do Discord</label>
              <input type="text" class="form-control" id="discordId" v-model="currentClient.discordId">
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="showModal = false">Cancelar</button>
          <button type="button" class="btn btn-primary" @click="saveClient">Salvar</button>
        </div>
      </div>
    </div>
  </div>
  <div v-if="showModal" class="modal-backdrop fade show"></div>
</template>