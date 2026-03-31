import { authAPI } from './api';

const authService = {
  login: async (username, password) => {
    const response = await authAPI.login(username, password);
    return response.data;
  },

  logout: async () => {
    try {
      await authAPI.logout();
    } catch (e) {
      // Bỏ qua lỗi logout phía server, vẫn xóa session client
    }
  },
};

export default authService;
