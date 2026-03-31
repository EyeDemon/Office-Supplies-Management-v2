import React from 'react';
import { Navbar, Nav, Container, Button, Badge } from 'react-bootstrap';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { authAPI } from '../../services/api';

const Header = () => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = async () => {
    try { await authAPI.logout(); } catch (_) {}
    logout();
    navigate('/login');
  };

  const navLink = (to, label) => (
    <Nav.Link as={Link} to={to} active={location.pathname === to}>{label}</Nav.Link>
  );

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="mb-4">
      <Container>
        <Navbar.Brand as={Link} to="/dashboard">🏢 Quản Lý Văn Phòng Phẩm</Navbar.Brand>
        <Navbar.Toggle aria-controls="main-nav" />
        <Navbar.Collapse id="main-nav">
          <Nav className="me-auto">
            {navLink('/dashboard', 'Dashboard')}
            {isAdmin() && navLink('/users', 'Quản Lý Người Dùng')}
            {navLink('/profile', 'Hồ Sơ')}
          </Nav>
          <Nav className="ms-auto align-items-center">
            {user && (
              <>
                <Navbar.Text className="me-3">
                  Xin chào, <strong>{user.fullName || user.username}</strong>
                  &nbsp;
                  <Badge bg={user.role === 'ADMIN' ? 'danger' : 'primary'} className="ms-1">
                    {user.role === 'ADMIN' ? 'Admin' : 'User'}
                  </Badge>
                </Navbar.Text>
                <Button variant="outline-light" size="sm" onClick={handleLogout}>Đăng xuất</Button>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
