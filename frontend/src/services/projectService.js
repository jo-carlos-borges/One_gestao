import api from './api';

// Cria um novo projeto
export const createProject = (projectData) => {
  return api.post('/projects', projectData);
};

// Busca a lista de projetos de forma paginada
export const getProjects = (params) => {
  return api.get('/projects', { params });
};

// Futuramente, podemos adicionar:
// export const getProjectById = (id) => api.get(`/projects/${id}`);
