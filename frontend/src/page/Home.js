import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Container, Row, Col, Button, Card } from 'react-bootstrap';

const Home = () => {
  const navigate = useNavigate();
  const { isAuthenticated, loading } = useAuth();

  useEffect(() => {
    if (!loading && isAuthenticated()) navigate('/dashboard', { replace: true });
  }, [loading, isAuthenticated, navigate]);

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={8} className="text-center">
          <div style={{ fontSize: '4rem' }}>🏢</div>
          <h1 className="display-4 mb-3">Quản Lý Văn Phòng Phẩm</h1>
          <p className="lead text-muted mb-4">Hệ thống quản lý người dùng và phân quyền truy cập</p>
          <div className="d-flex gap-3 justify-content-center">
            <Button variant="primary" size="lg" onClick={() => navigate('/login')}>Đăng nhập</Button>
            <Button variant="outline-primary" size="lg" onClick={() => navigate('/register')}>Đăng ký</Button>
          </div>
          <Card className="mt-5 text-start">
            <Card.Body>
              <Card.Title>📋 Tài khoản demo (mật khẩu: 123456)</Card.Title>
              <table className="table table-sm mb-0">
                <thead><tr><th>Tài khoản</th><th>Mật khẩu</th><th>Vai trò</th></tr></thead>
                <tbody>
                  <tr><td><code>admin</code></td><td><code>123456</code></td><td>Quản trị viên</td></tr>
                  <tr><td><code>manager</code></td><td><code>123456</code></td><td>Quản trị viên</td></tr>
                  <tr><td><code>nhanvien1</code></td><td><code>123456</code></td><td>Nhân viên</td></tr>
                </tbody>
              </table>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Home;
