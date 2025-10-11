<script setup>
import { inject, ref, onMounted, onUnmounted, computed } from 'vue'
import { ShoppingCart, UserCircle, Package, DollarSign, Plus, Filter } from 'lucide-vue-next'


const isDark = inject('isDark')
const chartPeriod = ref('week') // Período padrão: semana

// Dados para os gráficos
const weekData = [
  { key: 'Seg', value: 4800 },
  { key: 'Ter', value: 5600 },
  { key: 'Qua', value: 3200 },
  { key: 'Qui', value: 4900 },
  { key: 'Sex', value: 5100 },
  { key: 'Sáb', value: 6800 },
  { key: 'Dom', value: 5400 }
]

const monthData = [
  { key: 'Semana 1', value: 18500 },
  { key: 'Semana 2', value: 22400 },
  { key: 'Semana 3', value: 19800 },
  { key: 'Semana 4', value: 23600 }
]

const yearData = [
  { key: 'Jan', value: 65000 },
  { key: 'Fev', value: 72000 },
  { key: 'Mar', value: 84000 },
  { key: 'Abr', value: 78000 },
  { key: 'Mai', value: 92000 },
  { key: 'Jun', value: 88000 },
  { key: 'Jul', value: 94000 },
  { key: 'Ago', value: 96000 },
  { key: 'Set', value: 89000 },
  { key: 'Out', value: 93000 },
  { key: 'Nov', value: 98000 },
  { key: 'Dez', value: 110000 }
]

// Função para formatar valores em reais
const formatCurrency = (value) => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value)
}

// Função para alterar o período do gráfico
const setChartPeriod = (period) => {
  chartPeriod.value = period
}

// Dados do gráfico com base no período selecionado
const chartData = computed(() => {
  switch (chartPeriod.value) {
    case 'week':
      return weekData
    case 'month':
      return monthData
    case 'year':
      return yearData
    default:
      return weekData
  }
})

// Séries para o gráfico
const chartSeries = computed(() => {
  return [{
    name: 'Vendas',
    data: chartData.value.map(item => item.value)
  }]
})

// Opções do gráfico
const chartOptions = computed(() => {
  return {
    chart: {
		type: 'bar',
		height: 300,
		toolbar: {
			show: false
		},
		fontFamily: 'inherit',
		foreColor: isDark.value ? '#e2e8f0' : '#6c757d',
		background: isDark.value? 'color__theme__dark' : 'color__theme__light'
    },
    plotOptions: {
      bar: {
        borderRadius: 4,
        columnWidth: '60%',
        dataLabels: {
          position: 'top'
        }
      }
    },
    colors: ['#0d6efd'],
    dataLabels: {
      enabled: false
    },
    stroke: {
      show: true,
      width: 2,
      colors: ['transparent']
    },
    xaxis: {
      categories: chartData.value.map(item => item.key),
      axisBorder: {
        show: false
      },
      axisTicks: {
        show: false
      }
    },
    yaxis: {
      labels: {
        formatter: function (value) {
          if (value >= 1000) {
            return 'R$ ' + (value / 1000).toFixed(1) + 'k'
          }
          return 'R$ ' + value
        }
      }
    },
    fill: {
      opacity: 1,
      type: 'gradient',
      gradient: {
        shade: 'light',
        type: 'vertical',
        shadeIntensity: 0.4,
        opacityFrom: 1,
        opacityTo: 0.8,
        stops: [0, 100]
      }
    },
    tooltip: {
      y: {
        formatter: function (value) {
          return formatCurrency(value)
        }
      },
      theme: isDark.value ? 'dark' : 'light'
    },
    grid: {
      borderColor: isDark.value ? '#374151' : '#e5e7eb',
      strokeDashArray: 4,
      yaxis: {
        lines: {
          show: true
        }
      }
    },
    theme: {
      mode: isDark.value ? 'dark' : 'light'
    }
  }
})

// Lista de empresas para o seletor
const companies = [
  { id: 1, name: 'Empresa Principal' },
  { id: 2, name: 'Filial 1' },
  { id: 3, name: 'Filial 2' },
  { id: 4, name: 'Filial 3' }
]

// Empresa selecionada atualmente
const selectedCompanies = ref(companies[0])

// Função para mudar a empresa selecionada
const selectCompany = (company) => {
  selectedCompanies.value = company
  // Aqui você pode adicionar lógica para carregar os dados da empresa selecionada
  console.log(`Carregando dados da empresa: ${company.name}`)
  dropdownOpen.value = false
}

// Controle do dropdown
const dropdownOpen = ref(false)
const dropdownRef = ref(null)

const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value
}

// Função para fechar o dropdown quando clicar fora
const handleClickOutside = (event) => {
  if (dropdownRef.value && !dropdownRef.value.contains(event.target)) {
    dropdownOpen.value = false
  }
}

// Adiciona o evento de clique ao montar o componente
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

// Remove o evento ao desmontar o componente para evitar memory leaks
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

</script>

<template>
	<div class="dashboard">
		<div class="dashboard__container">
			<div class="row mb-4">
				<div class="col-12 d-flex justify-content-between align-items-center">
					<div>
						<h1 class="dashboard__title">Dashboard</h1>
						<p class="dashboard__subtitle">Bem-vindo ao ThunderSales</p>
					</div>

					<!-- Seletor de Empresa -->
					<div class="dashboard__company-selector">
						<label for="company-dropdown" class="form-label mb-1 d-flex align-items-center">
							<Filter size="16" class="me-2" :class="isDark ? 'text-light' : 'text-dark'" />
							Filtrar
						</label>
						<div class="dropdown" ref="dropdownRef">
							<button id="company-dropdown" class="btn dropdown-toggle dashboard__company-btn"
								:class="isDark ? 'color__theme__dark' : 'color__theme__light'" @click.stop="toggleDropdown">
								<span class="dashboard__company-name">{{ selectedCompanies.name }}</span>
							</button>
							<ul class="dropdown-menu dropdown-menu-end"
								:class="[dropdownOpen ? 'show' : '', isDark ? 'dropdown-menu-dark' : '']">
								<li v-for="company in companies" :key="company.id">
									<a class="dropdown-item" href="#" @click.prevent="selectCompany(company)">
										{{ company.name }}
									</a>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- Cards de Estatísticas -->
		<div class="row mb-4">
			<div class="col-md-3 col-sm-6 mb-4 mb-md-0">
				<div class="card card-hover dashboard__card" :class="isDark ? 'color__theme__dark text-white' : ''">
					<div class="card-body">
						<div class="d-flex justify-content-between align-items-start">
							<div>
								<h6 class="card-subtitle mb-2" :class="isDark ? 'text-white' : 'text-muted'">Vendas Hoje</h6>
								<h3 class="card-title mb-0">R$ 5.480,00</h3>
								<p class="card-text mt-2">
									<span class="text-success">+12%</span> desde ontem
								</p>
							</div>
							<div class="dashboard__icon">
								<ShoppingCart size="24" :class="isDark ? 'text-info' : 'text-primary'" />
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-3 col-sm-6 mb-4 mb-md-0">
				<div class="card card-hover dashboard__card" :class="isDark ? 'color__theme__dark text-white' : ''">
					<div class="card-body">
						<div class="d-flex justify-content-between align-items-start">
							<div>
								<h6 class="card-subtitle mb-2" :class="isDark ? 'text-white' : 'text-muted'">Clientes
									Novos</h6>
								<h3 class="card-title mb-0">24</h3>
								<p class="card-text mt-2">
									<span class="text-success">+8%</span> desde a semana passada
								</p>
							</div>
							<div class="dashboard__icon">
								<UserCircle size="24" :class="isDark ? 'text-info' : 'text-primary'" />
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-3 col-sm-6 mb-4 mb-md-0">
				<div class="card card-hover dashboard__card" :class="isDark ? 'color__theme__dark text-white' : ''">
					<div class="card-body">
						<div class="d-flex justify-content-between align-items-start">
							<div>
								<h6 class="card-subtitle mb-2" :class="isDark ? 'text-white' : 'text-muted'">Produtos
									Vendidos</h6>
								<h3 class="card-title mb-0">142</h3>
								<p class="card-text mt-2">
									<span class="text-danger">-3%</span> desde ontem
								</p>
							</div>
							<div class="dashboard__icon">
								<Package size="24" :class="isDark ? 'text-info' : 'text-primary'" />
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-3 col-sm-6 mb-4 mb-md-0">
				<div class="card card-hover dashboard__card" :class="isDark ? 'color__theme__dark text-white' : ''">
					<div class="card-body">
						<div class="d-flex justify-content-between align-items-start">
							<div>
								<h6 class="card-subtitle mb-2" :class="isDark ? 'text-white' : 'text-muted'">Receita
									Mensal</h6>
								<h3 class="card-title mb-0">R$ 84.320,00</h3>
								<p class="card-text mt-2">
									<span class="text-success">+18%</span> desde o mês passado
								</p>
							</div>
							<div class="dashboard__icon">
								<DollarSign size="24" :class="isDark ? 'text-info' : 'text-primary'" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- Gráficos -->
		<div class="row mb-4">
			<div class="col-lg-8 mb-4 mb-lg-0">
				<div class="card dashboard__card" :class="isDark ? 'color__theme__dark text-white' : ''">
					<div class="card-header d-flex justify-content-between align-items-center">
						<h5 class="card-title mb-0">Vendas dos Últimos 7 Dias</h5>
						<div class="btn-group">
							<button class="btn btn-sm" 
								:class="[isDark ? 'btn-outline-light' : 'btn-outline-primary', chartPeriod === 'week' ? 'active' : '']"
								@click="setChartPeriod('week')">Semana</button>
							<button class="btn btn-sm" 
								:class="[isDark ? 'btn-outline-light' : 'btn-outline-primary', chartPeriod === 'month' ? 'active' : '']"
								@click="setChartPeriod('month')">Mês</button>
							<button class="btn btn-sm" 
								:class="[isDark ? 'btn-outline-light' : 'btn-outline-primary', chartPeriod === 'year' ? 'active' : '']"
								@click="setChartPeriod('year')">Ano</button>
						</div>
					</div>
					<div class="card-body">
						<div class="dashboard__chart__container">
							<ApexCharts
								type="bar"
								height="300"
								:options="chartOptions"
								:series="chartSeries"
							></ApexCharts>
						</div>
					</div>
				</div>
			</div>

			<div class="col-lg-4">
				<div class="card dashboard__card" :class="isDark ? 'color__theme__dark text-white' : ''">
					<div class="card-header">
						<h5 class="card-title mb-0">Produtos Mais Vendidos</h5>
					</div>
					<div class="card-body p-0">
						<ul class="list-group list-group-flush">
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								Smartphone XYZ
								<span class="badge bg-primary rounded-pill">124</span>
							</li>
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								Notebook Ultra
								<span class="badge bg-primary rounded-pill">98</span>
							</li>
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								Smart TV 4K
								<span class="badge bg-primary rounded-pill">76</span>
							</li>
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								Fone de Ouvido Bluetooth
								<span class="badge bg-primary rounded-pill">65</span>
							</li>
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								Câmera Digital
								<span class="badge bg-primary rounded-pill">42</span>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<!-- Últimas Atividades e Lembretes -->
		<div class="row">
			<div class="col-lg-6 mb-4 mb-lg-0">
				<div class="card dashboard__card" :class="isDark ? 'color__theme__dark text-white' : ''">
					<div class="card-header">
						<h5 class="card-title mb-0">Últimas Vendas</h5>
					</div>
					<div class="card-body p-0">
						<div class="table-responsive">
							<table class="table" :class="isDark ? 'color__theme__dark text-white' : ''">
								<thead>
									<tr>
										<th>Produto</th>
										<th>Valor</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Smartphone XYZ</td>
										<td>R$ 1.899,00</td>
										<td><span class="badge bg-success">Concluído</span></td>
									</tr>
									<tr>
										<td>Notebook Ultra</td>
										<td>R$ 3.450,00</td>
										<td><span class="badge bg-warning text-dark">Pendente</span></td>
									</tr>
									<tr>
										<td>Smart TV 4K</td>
										<td>R$ 2.799,00</td>
										<td><span class="badge bg-success">Concluído</span></td>
									</tr>
									<tr>
										<td>Fone de Ouvido Bluetooth</td>
										<td>R$ 299,00</td>
										<td><span class="badge bg-success">Concluído</span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>

			<div class="col-lg-6">
				<div class="card dashboard__card" :class="isDark ? 'color__theme__dark text-white' : ''">
					<div class="card-header d-flex justify-content-between align-items-center">
						<h5 class="card-title mb-0">Lembretes</h5>
						<button class="btn btn-sm" :class="isDark ? 'btn-outline-light' : 'btn-outline-primary'">
							<Plus size="16" /> Novo
						</button>
					</div>
					<div class="card-body p-0">
						<ul class="list-group list-group-flush">
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								<div>
									<h6 class="mb-1 limitador">Reunião com fornecedores</h6>
									<small :class="isDark ? 'text-white' : 'text-muted'">Hoje, 14:00</small>
								</div>
								<span class="badge bg-danger">Urgente</span>
							</li>
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								<div>
									<h6 class="mb-1 limitador">Conferir estoque</h6>
									<small :class="isDark ? 'text-white' : 'text-muted'">Amanhã, 09:00</small>
								</div>
								<span class="badge bg-warning text-dark">Importante</span>
							</li>
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								<div>
									<h6 class="mb-1 limitador">Pagamento de fornecedores</h6>
									<small :class="isDark ? 'text-white' : 'text-muted'">23/05/2025</small>
								</div>
								<span class="badge bg-info">Financeiro</span>
							</li>
							<li class="list-group-item d-flex justify-content-between align-items-center"
								:class="isDark ? 'color__theme__dark text-white border-secondary' : ''">
								<div>
									<h6 class="mb-1 limitador">Treinamento de equipe</h6>
									<small :class="isDark ? 'text-white' : 'text-muted'">25/05/2025</small>
								</div>
								<span class="badge bg-primary">Equipe</span>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
.dashboard__container {
	padding: 1.5rem 0;
}

.dashboard__title {
	font-size: 1.75rem;
	font-weight: 600;
	margin-bottom: 0.5rem;
}

.dashboard__subtitle {
	color: #6c757d;
	margin-bottom: 1.5rem;
}

/* Estilos para o seletor de empresa */
.dashboard__company-selector {
	position: relative;
}

.dashboard__company-selector .form-label {
	font-weight: 500;
	color: #6c757d;
}

.dark .dashboard__company-selector .form-label {
	color: #e2e8f0;
}

.dashboard__company-btn {
	display: flex;
	align-items: center;
	font-weight: 500;
	padding: 0.5rem 1rem;
	border-radius: 0.5rem;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
	transition: all 0.3s ease;
	min-width: 180px;
	max-width: 250px;
}

.dashboard__company-btn:hover {
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.dashboard__company-name {
	display: inline-block;
	max-width: 200px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

/* Mantendo classes nativas do Bootstrap */
.dropdown-menu {
	border-radius: 0.5rem;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
	padding: 0.5rem 0;
	min-width: 180px;
	max-width: 300px;
	margin-top: 0.5rem;
	right: 0;
	left: auto;
}

.dropdown-item {
	padding: 0.5rem 1rem;
	transition: all 0.2s ease;
	white-space: normal;
	word-break: break-word;
}

.dropdown-item:hover {
	background-color: rgba(13, 110, 253, 0.1);
}

/* Estilos existentes convertidos para BEM */
.dashboard__card {
	height: 100%;
	border-radius: 0.5rem;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
	transition: all 0.3s ease;
}

.dashboard__card:hover {
	transform: translateY(-5px);
	box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.dashboard__icon {
	background-color: rgba(13, 110, 253, 0.1);
	padding: 0.75rem;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
}

.dashboard__chart__container {
	height: 300px;
	position: relative;
}

.dashboard__chart-placeholder {
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: flex-end;
}

.dashboard__chart-bars {
	display: flex;
	justify-content: space-around;
	align-items: flex-end;
	height: 90%;
}

.dashboard__chart-bar {
	width: 40px;
	background: var(--linear-gradient-logo-color);
	border-radius: 4px 4px 0 0;
	margin: 0 5px;
}

.dashboard__chart--dark .dashboard__chart-bar {
	background: linear-gradient(to top, #0d6efd, #6610f2);
}

.dashboard__chart-labels {
	display: flex;
	justify-content: space-around;
	margin-top: 10px;
}

.dashboard__chart-labels span {
	font-size: 0.8rem;
	color: #6c757d;
}

.table.color__theme__dark {
	--bs-table-bg: #2c2c2c !important;
	--bs-table-color: #fff !important;
	--bs-table-border-color: #373b3e !important;
	color: #fff !important;
}

.table.color__theme__dark th,
.table.color__theme__dark td {
	border-color: #373b3e !important;
}

@media (max-width: 768px) {
	.dashboard__chart-bar {
		width: 30px;
	}
}
</style>
