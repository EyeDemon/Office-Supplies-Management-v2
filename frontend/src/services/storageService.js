const KEYS = {
  USER: 'user',
  TOKEN: 'token',
};

const storageService = {
  getUser: () => {
    try {
      const data = localStorage.getItem(KEYS.USER);
      return data ? JSON.parse(data) : null;
    } catch {
      return null;
    }
  },

  setUser: (user) => {
    localStorage.setItem(KEYS.USER, JSON.stringify(user));
  },

  getToken: () => localStorage.getItem(KEYS.TOKEN),

  setToken: (token) => localStorage.setItem(KEYS.TOKEN, token),

  clear: () => {
    localStorage.removeItem(KEYS.USER);
    localStorage.removeItem(KEYS.TOKEN);
  },

  isAuthenticated: () => {
    return !!storageService.getUser() && !!storageService.getToken();
  },
};

export default storageService;
