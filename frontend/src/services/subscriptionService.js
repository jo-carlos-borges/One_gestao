import api from './api';

export const createSubscription = (data) => {
  return api.post('/subscriptions', data);
};

export const getSubscriptions = () => {
  return api.get('/subscriptions');
};

export const getSubscriptionById = (id) => {
  return api.get(`/subscriptions/${id}`);
};

