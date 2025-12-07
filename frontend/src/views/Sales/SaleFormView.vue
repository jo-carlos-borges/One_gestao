<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/services/api'; // Usamos o api para carregar dados iniciais
import { createSale } from '@/services/saleService';

const router = useRouter();

// Dados para preencher os seletores
const clients = ref([]);
const products = ref([]);
const users = ref([]); // Para o seletor de instalador

// Estado do formulário
const saleForm = ref({
  clientId: null,
  productIds: [],
  paymentMethod: 'PIX',
  downPayment: null,
  numberOfInstallments: null,
  baseInstallationDate: '',
  siteInstallationDate: '',
  validatorAccess: '',
  vpsIp: '',
  installerId: null,
  outOfHours: false
});

// Propriedade computada para mostrar/esconder campos do crediário
const isInstallmentPlan = computed(() => saleForm.value.paymentMethod === 'INSTALLMENTS_PLAN');

// Função para carregar todos os dados necessários para o formulário
const loadFormData = async () => {
  try {
    const [clientsRes, productsRes, usersRes] = await Promise.all([
      api.get('/clients', { params: { size: 1000, unit: 'FIVEM' } }), // Filtro para clientes FIVEM
      api.get('/products'), // Endpoint correto do backend
      api.get('/users') // Endpoint correto do backend
    ]);
    clients.value = clientsRes.data.content; // Clients retorna Page<ClientResponse>
    products.value = productsRes.data.content; // Products retorna Page<ProductResponse>
    users.value = usersRes.data.content || usersRes.data; // Adapta para diferentes estruturas
  } catch (error) {
    console.error("Erro ao carregar dados do formulário:", error);
    window.$toast().fire({ icon: 'error', title: 'Não foi possível carregar os dados do formulário.' });
  }
};

// Envio do formulário
const handleSubmit = async () => {
  // Validações básicas
  if (!saleForm.value.clientId) {
    window.$toast().fire({ icon: 'warning', title: 'Selecione um cliente.' });
    return;
  }
  if (saleForm.value.productIds.length === 0) {
    window.$toast().fire({ icon: 'warning', title: 'Selecione pelo menos um produto.' });
    return;
  }

  try {
    const payload = { ...saleForm.value };
    // Limpa os campos de crediário se não for o método de pagamento selecionado
    if (!isInstallmentPlan.value) {
      payload.downPayment = null;
      payload.numberOfInstallments = null;
    }
    await createSale(payload);
    window.$toast().fire({ icon: 'success', title: 'Venda registrada com sucesso!' });
    router.push('/gta-sales'); // Redireciona para a lista de vendas
  } catch (error) {
    console.error("Erro ao registrar venda:", error);
    const errorMsg = error?.response?.data?.message || "Ocorreu um erro ao registrar a venda.";
    window.$toast().fire({ icon: 'error', title: errorMsg });
  }
};

onMounted(loadFormData);
</script>

<template>
  <div>
    <h1 class="mb-4">Registrar Nova Venda (FiveM)</h1>
    <form @submit.prevent="handleSubmit">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title mb-4">Informações Gerais</h5>
          <div class="row">
            <div class="col-md-6 mb-3">
              <label for="client" class="form-label">Cliente</label>
              <select id="client" class="form-select" v-model="saleForm.clientId" required>
                <option :value="null" disabled>Selecione um cliente</option>
                <option v-for="client in clients" :key="client.id" :value="client.id">
                  {{ client.name }}
                </option>
              </select>
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label">Produtos</label>
            <div class="product-list">
              <div v-for="product in products" :key="product.id" class="form-check">
                <input class="form-check-input" type="checkbox" :value="product.id" :id="`product_${product.id}`" v-model="saleForm.productIds">
                <label class="form-check-label" :for="`product_${product.id}`">
                  {{ product.name }} - {{ new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.price) }}
                </label>
              </div>
            </div>
          </div>
          <hr>

          <h5 class="card-title my-4">Pagamento</h5>
          <div class="row">
            <div class="col-md-6 mb-3">
              <label for="paymentMethod" class="form-label">Forma de Pagamento</label>
              <select id="paymentMethod" class="form-select" v-model="saleForm.paymentMethod" required>
                <option value="PIX">PIX</option>
                <option value="CREDIT_CARD">Cartão de Crédito (À Vista)</option>
                <option value="CREDIT_CARD_INSTALLMENTS">Cartão de Crédito (Parcelado)</option>
                <option value="INSTALLMENTS_PLAN">Crediário</option>
              </select>
            </div>
          </div>

          <div v-if="isInstallmentPlan" class="row">
            <div class="col-md-6 mb-3">
              <label for="downPayment" class="form-label">Valor da Entrada (R$)</label>
              <input type="number" step="0.01" class="form-control" id="downPayment" v-model="saleForm.downPayment">
            </div>
            <div class="col-md-6 mb-3">
              <label for="numberOfInstallments" class="form-label">Nº de Parcelas (Saldo)</label>
              <input type="number" class="form-control" id="numberOfInstallments" v-model="saleForm.numberOfInstallments" min="1" max="6">
            </div>
          </div>
          <hr>

          <h5 class="card-title my-4">Informações de Instalação</h5>
           <div class="row">
             <div class="col-md-6 mb-3">
                <label for="installer" class="form-label">Instalador Responsável</label>
                <select id="installer" class="form-select" v-model="saleForm.installerId">
                    <option :value="null">Nenhum / Não definido</option>
                    <option v-for="user in users" :key="user.id" :value="user.id">{{ user.name }}</option>
                </select>
             </div>
             <div class="col-md-6 mb-3">
                <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="outOfHours" v-model="saleForm.outOfHours">
                    <label class="form-check-label" for="outOfHours">
                        Instalação Fora de Horário? (Gera bônus de R$ 200,00)
                    </label>
                </div>
             </div>
             <div class="col-md-6 mb-3">
                <label for="vpsIp" class="form-label">IP da VPS</label>
                <input type="text" class="form-control" id="vpsIp" v-model="saleForm.vpsIp">
             </div>
             <div class="col-md-6 mb-3">
                <label for="validatorAccess" class="form-label">Acesso do Validador</label>
                <input type="text" class="form-control" id="validatorAccess" v-model="saleForm.validatorAccess">
             </div>
             <div class="col-md-6 mb-3">
                <label for="baseInstallationDate" class="form-label">Data da Instalação da Base</label>
                <input type="datetime-local" class="form-control" id="baseInstallationDate" v-model="saleForm.baseInstallationDate">
             </div>
              <div class="col-md-6 mb-3">
                <label for="siteInstallationDate" class="form-label">Data da Instalação do Site</label>
                <input type="datetime-local" class="form-control" id="siteInstallationDate" v-model="saleForm.siteInstallationDate">
             </div>
          </div>

        </div>
        <div class="card-footer text-end">
          <router-link to="/gta-sales" class="btn btn-secondary me-2">Cancelar</router-link>
          <button type="submit" class="btn btn-primary">Registrar Venda</button>
        </div>
      </div>
    </form>
  </div>
</template>

<style scoped>
.product-list {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #dee2e6;
  padding: 1rem;
  border-radius: 0.375rem;
}
</style>

