import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const NotFound = () => {
  const navigate = useNavigate();
  return (
    <Container className="d-flex flex-column align-items-center justify-content-center min-vh-100 text-center">
      <div style={{ fontSize: '5rem' }}>🔍</div>
      <h1 className="display-4 text-muted">404</h1>
      <p className="lead">Trang bạn tìm không tồn tại.</p>
      <Button variant="primary" onClick={() => navigate('/dashboard')}>Về trang chủ</Button>
    </Container>
  );
};

export default NotFound;
