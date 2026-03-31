import React, { useState } from 'react';
import { Card, Container, Row, Col, Form, Button, Alert, Badge } from 'react-bootstrap';
import { useAuth } from '../contexts/AuthContext';
import { userAPI } from '../services/api';

const UserProfile = () => {
  const { user, login } = useAuth();

  const [formData, setFormData] = useState({
    email:       user?.email       || '',
    fullName:    user?.fullName    || '',
    phoneNumber: user?.phoneNumber || '',
  });
  const [pwData, setPwData]     = useState({ currentPassword: '', newPassword: '', confirmPassword: '' });
  const [msg,    setMsg]        = useState({ type: '', text: '' });
  const [loading, setLoading]   = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handlePwChange = (e) => {
    const { name, value } = e.target;
    setPwData(prev => ({ ...prev, [name]: value }));
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    if (!formData.fullName.trim() || !formData.email.trim()) {
      setMsg({ type: 'danger', text: 'Họ tên và email là bắt buộc' });
      return;
    }
    setLoading(true);
    try {
      const res = await userAPI.updateUser(user.id, {
        email:       formData.email,
        fullName:    formData.fullName,
        phoneNumber: formData.phoneNumber,
        role:        user.role,
      });
      // Cập nhật AuthContext với dữ liệu mới
      const token = localStorage.getItem('token');
      login(res.data.data, token);
      setMsg({ type: 'success', text: 'Cập nhật hồ sơ thành công!' });
    } catch (err) {
      setMsg({ type: 'danger', text: err.response?.data?.message || 'Cập nhật thất bại' });
    } finally { setLoading(false); }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (!pwData.currentPassword || !pwData.newPassword) {
      setMsg({ type: 'danger', text: 'Vui lòng điền đủ thông tin mật khẩu' });
      return;
    }
    if (pwData.newPassword.length < 6) {
      setMsg({ type: 'danger', text: 'Mật khẩu mới phải ít nhất 6 ký tự' });
      return;
    }
    if (pwData.newPassword !== pwData.confirmPassword) {
      setMsg({ type: 'danger', text: 'Xác nhận mật khẩu không khớp' });
      return;
    }
    setLoading(true);
    try {
      await userAPI.updateUser(user.id, { password: pwData.newPassword, role: user.role,
        email: user.email, fullName: user.fullName });
      setPwData({ currentPassword: '', newPassword: '', confirmPassword: '' });
      setMsg({ type: 'success', text: 'Đổi mật khẩu thành công!' });
    } catch (err) {
      setMsg({ type: 'danger', text: err.response?.data?.message || 'Đổi mật khẩu thất bại' });
    } finally { setLoading(false); }
  };

  return (
    <Container>
      <h1 className="mb-4">Hồ Sơ Cá Nhân</h1>

      {msg.text && (
        <Alert variant={msg.type} dismissible onClose={() => setMsg({ type: '', text: '' })}>
          {msg.text}
        </Alert>
      )}

      <Row className="g-4">
        {/* Thông tin cơ bản */}
        <Col md={6}>
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title>👤 Thông tin tài khoản</Card.Title>
              <Form onSubmit={handleUpdateProfile}>
                <Form.Group className="mb-3">
                  <Form.Label>Tên đăng nhập</Form.Label>
                  <Form.Control value={user?.username || ''} disabled />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Vai trò</Form.Label>
                  <div>
                    <Badge bg={user?.role === 'ADMIN' ? 'danger' : 'primary'}>
                      {user?.role === 'ADMIN' ? 'Quản trị viên' : 'Nhân viên'}
                    </Badge>
                  </div>
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Họ và tên *</Form.Label>
                  <Form.Control name="fullName" value={formData.fullName} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Email *</Form.Label>
                  <Form.Control name="email" type="email" value={formData.email} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Số điện thoại</Form.Label>
                  <Form.Control name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} />
                </Form.Group>
                <Button variant="primary" type="submit" disabled={loading}>
                  {loading ? 'Đang lưu...' : 'Lưu thay đổi'}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>

        {/* Đổi mật khẩu */}
        <Col md={6}>
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title>🔐 Đổi mật khẩu</Card.Title>
              <Form onSubmit={handleChangePassword}>
                <Form.Group className="mb-3">
                  <Form.Label>Mật khẩu hiện tại</Form.Label>
                  <Form.Control name="currentPassword" type="password"
                    value={pwData.currentPassword} onChange={handlePwChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Mật khẩu mới</Form.Label>
                  <Form.Control name="newPassword" type="password"
                    value={pwData.newPassword} onChange={handlePwChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Xác nhận mật khẩu mới</Form.Label>
                  <Form.Control name="confirmPassword" type="password"
                    value={pwData.confirmPassword} onChange={handlePwChange} />
                </Form.Group>
                <Button variant="warning" type="submit" disabled={loading}>
                  {loading ? 'Đang đổi...' : 'Đổi mật khẩu'}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default UserProfile;
