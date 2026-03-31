import React, { useState, useEffect } from 'react';
import { Card, Container, Row, Col, Badge } from 'react-bootstrap';
import { useAuth } from '../contexts/AuthContext';
import { userAPI } from '../services/api';

const StatCard = ({ icon, title, value, bg }) => (
  <Card className={`card-shadow text-white bg-${bg} h-100`}>
    <Card.Body className="d-flex align-items-center gap-3">
      <div style={{ fontSize: '2.5rem' }}>{icon}</div>
      <div>
        <div style={{ fontSize: '1.8rem', fontWeight: 'bold', lineHeight: 1 }}>{value}</div>
        <div style={{ opacity: 0.9 }}>{title}</div>
      </div>
    </Card.Body>
  </Card>
);

const Dashboard = () => {
  const { user, isAdmin } = useAuth();
  const [stats, setStats] = useState({ total: 0, admins: 0, users: 0 });

  useEffect(() => {
    if (isAdmin()) {
      userAPI.getAllUsers()
        .then(res => {
          const list = res.data.data?.users || [];
          setStats({
            total:  list.length,
            admins: list.filter(u => u.role === 'ADMIN').length,
            users:  list.filter(u => u.role === 'USER').length,
          });
        })
        .catch(() => {});
    }
  }, []);

  return (
    <Container>
      <Row className="mb-4">
        <Col>
          <h1>Dashboard</h1>
          <p className="text-muted">
            Chào mừng, <strong>{user?.fullName || user?.username}</strong>!
            &nbsp;<Badge bg={user?.role === 'ADMIN' ? 'danger' : 'primary'}>
              {user?.role === 'ADMIN' ? 'Quản trị viên' : 'Nhân viên'}
            </Badge>
          </p>
        </Col>
      </Row>

      {isAdmin() && (
        <Row className="mb-4 g-3">
          <Col md={4}><StatCard icon="👥" title="Tổng người dùng" value={stats.total}  bg="primary" /></Col>
          <Col md={4}><StatCard icon="🔑" title="Quản trị viên"   value={stats.admins} bg="danger"  /></Col>
          <Col md={4}><StatCard icon="👤" title="Nhân viên"       value={stats.users}  bg="success" /></Col>
        </Row>
      )}

      <Row className="g-3">
        <Col md={6}>
          <Card className="card-shadow h-100">
            <Card.Body>
              <Card.Title>👤 Thông tin tài khoản</Card.Title>
              <table className="table table-sm table-borderless mb-0">
                <tbody>
                  <tr><td className="text-muted" style={{width:'40%'}}>Tên đăng nhập</td><td><strong>{user?.username}</strong></td></tr>
                  <tr><td className="text-muted">Họ và tên</td><td>{user?.fullName || '—'}</td></tr>
                  <tr><td className="text-muted">Email</td><td>{user?.email}</td></tr>
                  <tr><td className="text-muted">Số điện thoại</td><td>{user?.phoneNumber || 'Chưa cập nhật'}</td></tr>
                  <tr><td className="text-muted">Vai trò</td><td>
                    <Badge bg={user?.role === 'ADMIN' ? 'danger' : 'primary'}>
                      {user?.role === 'ADMIN' ? 'Quản trị viên' : 'Nhân viên'}
                    </Badge>
                  </td></tr>
                </tbody>
              </table>
            </Card.Body>
          </Card>
        </Col>
        <Col md={6}>
          <Card className="card-shadow h-100">
            <Card.Body>
              <Card.Title>🚀 Tính năng</Card.Title>
              <ul className="list-unstyled mb-0">
                <li className="mb-2">✅ Đăng nhập / Đăng xuất bảo mật</li>
                <li className="mb-2">✅ Xem và cập nhật hồ sơ cá nhân</li>
                <li className="mb-2">✅ Đổi mật khẩu</li>
                {isAdmin() && <>
                  <li className="mb-2">✅ Xem danh sách tất cả người dùng</li>
                  <li className="mb-2">✅ Thêm / Sửa / Xóa người dùng</li>
                  <li className="mb-2">✅ Lọc &amp; tìm kiếm</li>
                  <li className="mb-2">✅ Phân quyền ADMIN / USER</li>
                </>}
              </ul>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard;
