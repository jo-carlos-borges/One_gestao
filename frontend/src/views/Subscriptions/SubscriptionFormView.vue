<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/services/api';
import { createSubscription } from '@/services/subscriptionService';

const router = useRouter();
const clients = ref([]);

const form = ref({
  clientId: null,
  softwareName: '',
  // Implementação
  implementationFee: 0.00,
  implementationPaymentMethod: 'PIX',
  numberOfInstallments: null,
  // Mensalidade
  monthlyFee: 0.00,
  dueDay: 10
});

const isInstallment = computed(() => form.value.implementationPaymentMethod === 'INSTALLMENT_PLAN');

onMounted(async () => {
  try {
    const response = await api.get('/clients', { params: { size: 1000 } });
    clients.value = response.data.content;
  } catch (error) {
    console.error("Erro ao carregar clientes:", error);
    window.$toast().fire({ icon: 'error', title: 'Não foi possível carregar os clientes.' });
  }
});

const handleSubmit = async () => {
  try {
    // Conversão de tipos para garantir o envio correto
    const payload = {
        ...form.value,
        implementationFee: parseFloat(form.value.implementationFee),
        monthlyFee: parseFloat(form.value.monthlyFee),
        numberOfInstallments: isInstallment.value ? parseInt(form.value.numberOfInstallments) : null
    };

    await createSubscription(payload);
    window.$toast().fire({ icon: 'success', title: 'Contrato SaaS criado com sucesso!' });
    router.push('/subscriptions');
  } catch (error) {
    console.error("Erro ao criar assinatura:", error);
    const errorMsg = error?.response?.data?.message || "Erro ao salvar contrato. Verifique os dados.";
    window.$toast().fire({ icon: 'error', title: errorMsg });
  }
};
</script>

<template>
  <div>
    <h1 class="mb-4">Novo Contrato SaaS</h1>

    <form @submit.prevent="handleSubmit">
      <div class="row">

        <div class="col-md-6">
          <div class="card mb-3">
            <div class="card-header bg-light">
                <strong>1. Dados do Contrato</strong>
            </div>
            <div class="card-body">
              <div class="mb-3">
                <label class="form-label">Cliente</label>
                <select class="form-select" v-model="form.clientId" required>
                  <option :value="null" disabled>Selecione...</option>
                  <option v-for="client in clients" :key="client.id" :value="client.id">{{ client.name }}</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">Nome do Software / Plano</label>
                <input type="text" class="form-control" v-model="form.softwareName" placeholder="Ex: Gestão ERP Pro" required>
              </div>
            </div>
          </div>

          <div class="card mb-3">
            <div class="card-header bg-light">
                <strong>2. Taxa de Implementação (Setup)</strong>
            </div>
            <div class="card-body">
              <div class="mb-3">
                <label class="form-label">Valor da Implementação (R$)</label>
                <input type="number" step="0.01" class="form-control" v-model="form.implementationFee" required>
                <div class="form-text">Se for isento, coloque 0.</div>
              </div>

              <div v-if="form.implementationFee > 0">
                  <div class="mb-3">
                    <label class="form-label">Forma de Pagamento (Setup)</label>
                    <select class="form-select" v-model="form.implementationPaymentMethod">
                      <option value="PIX">À Vista (PIX/Transferência)</option>
                      <option value="INSTALLMENT_PLAN">Parcelado</option>
                    </select>
                  </div>

                  <div v-if="isInstallment" class="mb-3">
                    <label class="form-label">Número de Parcelas</label>
                    <input type="number" class="form-control" v-model="form.numberOfInstallments" min="2" max="12">
                  </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-6">
          <div class="card mb-3 border-primary">
            <div class="card-header bg-primary text-white">
                <strong>3. Recorrência (Mensalidade)</strong>
            </div>
            <div class="card-body">
              <div class="mb-3">
                <label class="form-label">Valor Mensal (R$)</label>
                <input type="number" step="0.01" class="form-control form-control-lg" v-model="form.monthlyFee" required>
              </div>

              <div class="mb-3">
                <label class="form-label">Dia de Vencimento</label>
                <select class="form-select" v-model="form.dueDay" required>
                  <option v-for="day in [1, 5, 10, 15, 20, 25]" :key="day" :value="day">Dia {{ day }}</option>
                </select>
                <div class="form-text">Dia do mês em que o boleto recorrente vence.</div>
              </div>

              <div class="alert alert-info mt-3">
                  <small>
                      <strong>Nota:</strong> O contrato iniciará com status <em>IMPLEMENTATION</em>. 
                      A cobrança de mensalidade começará após a ativação oficial do sistema.
                  </small>
              </div>
            </div>
          </div>
        </div>

      </div>

      <div class="d-flex justify-content-end mt-3">
        <router-link to="/subscriptions" class="btn btn-secondary me-2">Cancelar</router-link>
        <button type="submit" class="btn btn-success px-4">Salvar Contrato</button>
      </div>
    </form>
  </div>
</template>

