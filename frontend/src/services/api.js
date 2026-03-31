import axios from 'axios';

// Tạo axios instance với config mặc định
const api = axios.create({
  baseURL: '/api',
  withCredentials: true, // quan trọng cho session-based auth
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor tự động xử lý lỗi 401
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (username, password) =>
    api.post('/auth/login', { username, password }),

  logout: () =>
    api.post('/auth/logout'),
};

// User API
export const userAPI = {
  getAllUsers: () =>
    api.get('/users'),

  getUserById: (id) =>
    api.get(`/users/${id}`),

  createUser: (userData) =>
    api.post('/users', userData),

  updateUser: (id, userData) =>
    api.put(`/users/${id}`, userData),

  deleteUser: (id) =>
    api.delete(`/users/${id}`),
};

export default api;
