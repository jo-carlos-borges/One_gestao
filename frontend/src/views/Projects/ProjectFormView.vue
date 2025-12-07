<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/services/api'; // para chamadas genéricas
import { createProject } from '@/services/projectService';

const router = useRouter();
const clients = ref([]);
const users = ref([]); // Todos os usuários (vendedores, devs, etc)
const projectForm = ref({
  name: '',
  description: '',
  clientId: null,
  developerIds: [],
  totalValue: 0.00,
  paymentMethod: 'PIX',
  numberOfInstallments: null
});

const isInstallmentPlan = computed(() => projectForm.value.paymentMethod === 'INSTALLMENTS_PLAN');

onMounted(async () => {
  try {
    const [clientsRes, usersRes] = await Promise.all([
      api.get('/clients', { params: { size: 1000, unit: 'SOFTWARE_FACTORY' } }), // Filtro para clientes SOFTWARE_FACTORY
      api.get('/users', { params: { size: 1000 } })
    ]);
    clients.value = clientsRes.data.content;
    users.value = usersRes.data.content;
  } catch (error) {
    console.error("Erro ao carregar dados do formulário:", error);
  }
});

const handleSubmit = async () => {
  try {
    const payload = {
        ...projectForm.value,
        totalValue: parseFloat(projectForm.value.totalValue)
    };
    if (!isInstallmentPlan.value) {
        payload.numberOfInstallments = null;
    }
    await createProject(payload);
    router.push('/projects');
  } catch (error) {
    console.error("Erro ao criar projeto:", error);
  }
};
</script>
<template>
  <div>
    <h1 class="mb-4">Novo Projeto</h1>
    <form @submit.prevent="handleSubmit">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title mb-4">Detalhes do Projeto</h5>
          <div class="mb-3">
            <label for="name" class="form-label">Nome do Projeto</label>
            <input type="text" class="form-control" id="name" v-model="projectForm.name" required>
          </div>
          <div class="mb-3">
            <label for="client" class="form-label">Cliente</label>
            <select id="client" class="form-select" v-model="projectForm.clientId" required>
              <option :value="null" disabled>Selecione um cliente</option>
              <option v-for="client in clients" :key="client.id" :value="client.id">{{ client.name }}</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-label">Desenvolvedores Envolvidos</label>
            <div class="developer-list">
              <div v-for="user in users" :key="user.id" class="form-check">
                <input class="form-check-input" type="checkbox" :value="user.id" :id="`user_${user.id}`" v-model="projectForm.developerIds">
                <label class="form-check-label" :for="`user_${user.id}`">{{ user.name }}</label>
              </div>
            </div>
          </div>
          <div class="mb-3">
             <label for="description" class="form-label">Descrição</label>
             <textarea class="form-control" id="description" rows="4" v-model="projectForm.description"></textarea>
          </div>
          <hr>
          <h5 class="card-title my-4">Financeiro</h5>
          <div class="row">
            <div class="col-md-4 mb-3">
              <label for="totalValue" class="form-label">Valor Total (R$)</label>
              <input type="number" step="0.01" class="form-control" id="totalValue" v-model="projectForm.totalValue" required>
            </div>
            <div class="col-md-4 mb-3">
              <label for="paymentMethod" class="form-label">Forma de Pagamento</label>
              <select id="paymentMethod" class="form-select" v-model="projectForm.paymentMethod" required>
                <option value="PIX">PIX (À Vista)</option>
                <option value="INSTALLMENTS_PLAN">Parcelado (Boleto/PIX)</option>
              </select>
            </div>
            <div v-if="isInstallmentPlan" class="col-md-4 mb-3">
              <label for="numberOfInstallments" class="form-label">Nº de Parcelas</label>
              <input type="number" class="form-control" id="numberOfInstallments" v-model="projectForm.numberOfInstallments" min="1">
            </div>
          </div>
        </div>
        <div class="card-footer text-end">
          <router-link to="/projects" class="btn btn-secondary me-2">Cancelar</router-link>
          <button type="submit" class="btn btn-primary">Salvar Projeto</button>
        </div>
      </div>
    </form>
  </div>
</template>
<style scoped>
.developer-list {
  max-height: 250px;
  overflow-y: auto;
  border: 1px solid #dee2e6;
  padding: 1rem;
  border-radius: 0.375rem;
}
</style>
