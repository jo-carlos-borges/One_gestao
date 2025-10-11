<script setup>
import { ref, onMounted } from 'vue';
import api from '@/services/api'; 
import { Plus, Edit, Trash2 } from 'lucide-vue-next';

const products = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentProduct = ref({
  name: '',
  price: 0.00,
  description: ''
});

// Formata o valor monetário para exibição
const formatCurrency = (value) => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value);
};

// Busca os produtos da API
const fetchProducts = async () => {
  try {
    const response = await api.get('/products');
    products.value = response.data.content; 
  } catch (error) {
    console.error("Erro ao buscar produtos:", error);
    // Chamar toast de erro
  }
};

// Abre o modal para um novo produto
const openCreateModal = () => {
  isEditing.value = false;
  currentProduct.value = { name: '', price: 0.00, description: '' };
  showModal.value = true;
};

// Abre o modal para editar um produto existente
const openEditModal = (product) => {
  isEditing.value = true;
  currentProduct.value = { ...product };
  showModal.value = true;
};

// Salva ou atualiza o produto
const saveProduct = async () => {
  try {
    // Garante que o preço seja enviado como número
    const payload = { 
      ...currentProduct.value, 
      price: parseFloat(currentProduct.value.price) 
    };

    if (isEditing.value) {
      await api.put(`/products/${payload.id}`, payload);
    } else {
      await api.post('/products', payload);
    }
    showModal.value = false;
    fetchProducts(); // Re-busca a lista para exibir a atualização
    // Chamar toast de sucesso
  } catch (error) {
    console.error("Erro ao salvar produto:", error);
    // Chamar toast de erro com detalhes da validação, se houver
  }
};

// Deleta um produto
const deleteProduct = async (id) => {
  if (!confirm('Tem certeza que deseja excluir este produto?')) return;
  try {
    await api.delete(`/products/${id}`);
    fetchProducts(); // Re-busca a lista
    // Chamar toast de sucesso
  } catch (error) {
    console.error("Erro ao deletar produto:", error);
    // Chamar toast de erro
  }
};

onMounted(fetchProducts);
</script>

<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Produtos (Bases e Scripts)</h1>
      <button class="btn btn-primary d-flex align-items-center" @click="openCreateModal">
        <Plus size="16" class="me-2" />
        Novo Produto
      </button>
    </div>

    <div class="card">
      <div class="table-responsive">
        <table class="table table-hover table-striped mb-0">
          <thead>
            <tr>
              <th>Nome</th>
              <th>Preço</th>
              <th>Descrição</th>
              <th class="text-end">Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="products.length === 0">
              <td colspan="4" class="text-center">Nenhum produto encontrado.</td>
            </tr>
            <tr v-for="product in products" :key="product.id">
              <td>{{ product.name }}</td>
              <td>{{ formatCurrency(product.price) }}</td>
              <td class="limitador-2">{{ product.description }}</td>
              <td class="text-end">
                <button class="btn btn-sm btn-outline-primary me-2" @click="openEditModal(product)">
                  <Edit size="14" />
                </button>
                <button class="btn btn-sm btn-outline-danger" @click="deleteProduct(product.id)">
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
          <h5 class="modal-title">{{ isEditing ? 'Editar Produto' : 'Novo Produto' }}</h5>
          <button type="button" class="btn-close" @click="showModal = false"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveProduct">
            <div class="mb-3">
              <label for="name" class="form-label">Nome do Produto</label>
              <input type="text" class="form-control" id="name" v-model="currentProduct.name" required>
            </div>
            <div class="mb-3">
              <label for="price" class="form-label">Preço (R$)</label>
              <input type="number" step="0.01" class="form-control" id="price" v-model="currentProduct.price" required>
            </div>
             <div class="mb-3">
              <label for="description" class="form-label">Descrição</label>
              <textarea class="form-control" id="description" rows="3" v-model="currentProduct.description"></textarea>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="showModal = false">Cancelar</button>
          <button type="button" class="btn btn-primary" @click="saveProduct">Salvar</button>
        </div>
      </div>
    </div>
  </div>
  <div v-if="showModal" class="modal-backdrop fade show"></div>
</template>