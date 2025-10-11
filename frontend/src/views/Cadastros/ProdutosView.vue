<script setup>
import { ref, onMounted, inject, computed } from 'vue';
import api from '@/services/api';
import { Plus, Pencil, Trash2 } from 'lucide-vue-next';

const isDark = inject('isDark');
const produtos = ref([]);
const empresas = ref([]);
const showForm = ref(false);

const novoProduto = ref({
  empresaId: null,
  nome: '',
  valor: '',
  descricaoSoftware: '',
  tipoBase: null,
});

// Propriedade computada que retorna o nome da empresa selecionada
const selectedEmpresaNome = computed(() => {
  if (!novoProduto.value.empresaId) return '';
  const selectedEmpresa = empresas.value.find(e => e.id === novoProduto.value.empresaId);
  return selectedEmpresa ? selectedEmpresa.nome.toLowerCase() : '';
});

const fetchInitialData = async () => {
  try {
    const [produtosResponse, empresasResponse] = await Promise.all([
      api.get('/produtos'),
      api.get('/empresas')
    ]);
    produtos.value = produtosResponse.data;
    empresas.value = empresasResponse.data;
  } catch (error) {
    window.$toast().fire({ icon: 'error', title: 'Não foi possível carregar os dados da página.' });
  }
};

const handleNovoProduto = async () => {
  // Validações
  if (!novoProduto.value.empresaId) return window.$toast().fire({ icon: 'warning', title: 'Selecione uma empresa.' });
  if (!novoProduto.value.nome) return window.$toast().fire({ icon: 'warning', title: 'O nome do produto é obrigatório.' });
  if (!novoProduto.value.valor || parseFloat(novoProduto.value.valor) <= 0) return window.$toast().fire({ icon: 'warning', title: 'O valor do produto deve ser maior que zero.' });

  try {
    await api.post('/produtos', novoProduto.value);
    window.$toast().fire({ icon: 'success', title: 'Produto cadastrado com sucesso!' });

    // Resetar formulário
    novoProduto.value = { empresaId: null, nome: '', valor: '', descricaoSoftware: '', tipoBase: null };
    showForm.value = false;
    await fetchInitialData();
  } catch (error) {
    const msg = error?.response?.data?.message || 'Não foi possível cadastrar o produto.';
    window.$toast().fire({ icon: 'error', title: msg });
  }
};

onMounted(fetchInitialData);
</script>

<template>
  <div class="col-12">
    <div class="card card-hover" :class="isDark ? 'color__theme__dark text-white' : 'bg-white text-dark'">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="card-title mb-0">Cadastro de Produtos</h4>
        <button class="btn btn-primary btn-sm" @click="showForm = !showForm">
          <Plus :size="16" class="me-1" />
          Novo Produto
        </button>
      </div>
      <div class="card-body">
        <div v-if="showForm" class="p-3 mb-4 rounded" :class="isDark ? 'bg-dark' : 'bg-light'">
          <form @submit.prevent="handleNovoProduto">
            <div class="row g-3">
              <div class="col-md-4">
                <label for="empresaProduto" class="form-label">Empresa</label>
                <select id="empresaProduto" class="form-select" v-model="novoProduto.empresaId">
                  <option :value="null" disabled>Selecione...</option>
                  <option v-for="empresa in empresas" :key="empresa.id" :value="empresa.id">{{ empresa.nome }}</option>
                </select>
              </div>
              <div class="col-md-5">
                <label for="nomeProduto" class="form-label">Nome do Produto</label>
                <input type="text" class="form-control" id="nomeProduto" v-model="novoProduto.nome" placeholder="Nome do item ou software">
              </div>
              <div class="col-md-3">
                <label for="valorProduto" class="form-label">Valor (R$)</label>
                <input type="number" step="0.01" class="form-control" id="valorProduto" v-model="novoProduto.valor" placeholder="1500.00">
              </div>

              <div v-if="selectedEmpresaNome === 'oneway'" class="col-12">
                 <label for="descricaoSoftware" class="form-label">Descrição do Software</label>
                 <textarea class="form-control" id="descricaoSoftware" v-model="novoProduto.descricaoSoftware" rows="3"></textarea>
              </div>

              <div v-if="selectedEmpresaNome === 'monkey'" class="col-12">
                <label for="tipoBase" class="form-label">Tipo de Base (GTA)</label>
                 <select id="tipoBase" class="form-select" v-model="novoProduto.tipoBase">
                  <option :value="null" disabled>Selecione o tipo...</option>
                  <option value="BASE">Base</option>
                  <option value="CURSO">Curso</option>
                  <option value="SCRIPT">Script</option>
                </select>
              </div>
            </div>
             <div class="row mt-3">
              <div class="col-12 text-end">
                 <button type="button" class="btn btn-secondary me-2" @click="showForm = false">Cancelar</button>
                 <button type="submit" class="btn btn-success">Salvar Produto</button>
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
                <th scope="col">Valor</th>
                <th scope="col">Detalhe Específico</th>
                <th scope="col" class="text-end">Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="produtos.length === 0"><td colspan="5" class="text-center">Nenhum produto cadastrado.</td></tr>
              <tr v-for="produto in produtos" :key="produto.id">
                <td>{{ produto.nome }}</td>
                <td><span class="badge" :class="produto.empresa.nome.toLowerCase() === 'monkey' ? 'bg-primary' : 'bg-info'">{{ produto.empresa.nome }}</span></td>
                <td>{{ new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(produto.valor) }}</td>
                <td>{{ produto.tipoBase || produto.descricaoSoftware || 'N/A' }}</td>
                <td class="text-end">
                  <button class="btn btn-outline-warning btn-sm me-2" @click="alert('Editar produto ' + produto.id)"><Pencil :size="14" /></button>
                  <button class="btn btn-outline-danger btn-sm" @click="alert('Deletar produto ' + produto.id)"><Trash2 :size="14" /></button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
