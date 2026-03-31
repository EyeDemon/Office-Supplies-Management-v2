import React, { useState } from 'react';
import { Form, Button, Card, Container, Row, Col, Alert } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import { userAPI } from '../services/api';

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '', email: '', fullName: '', phoneNumber: '', password: '', confirmPassword: ''
  });
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState('');
  const [loading, setLoading] = useState(false);

  const validate = () => {
    const errs = {};
    if (!formData.username.trim()) errs.username = 'Tên đăng nhập là bắt buộc';
    if (!formData.email.trim()) errs.email = 'Email là bắt buộc';
    else if (!/\S+@\S+\.\S+/.test(formData.email)) errs.email = 'Email không hợp lệ';
    if (!formData.fullName.trim()) errs.fullName = 'Họ tên là bắt buộc';
    if (!formData.password) errs.password = 'Mật khẩu là bắt buộc';
    else if (formData.password.length < 6) errs.password = 'Mật khẩu phải ít nhất 6 ký tự';
    if (formData.password !== formData.confirmPassword) errs.confirmPassword = 'Mật khẩu không khớp';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors(prev => ({ ...prev, [name]: '' }));
    if (apiError) setApiError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;
    setLoading(true);
    try {
      const { confirmPassword, ...payload } = formData;
      const response = await userAPI.createUser(payload);
      if (response.data.success) {
        navigate('/login', { state: { message: 'Đăng ký thành công! Hãy đăng nhập.' } });
      } else {
        setApiError(response.data.message || 'Đăng ký thất bại');
      }
    } catch (err) {
      setApiError(err.response?.data?.message || 'Lỗi kết nối server');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container fluid className="bg-light min-vh-100 d-flex align-items-center py-4">
      <Container>
        <Row className="justify-content-center">
          <Col md={7} lg={6}>
            <Card className="card-shadow">
              <Card.Body className="p-4">
                <div className="text-center mb-4">
                  <h3>Đăng Ký Tài Khoản</h3>
                </div>
                {apiError && <Alert variant="danger">{apiError}</Alert>}
                <Form onSubmit={handleSubmit}>
                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label>Tên đăng nhập *</Form.Label>
                        <Form.Control name="username" value={formData.username} onChange={handleChange} isInvalid={!!errors.username} />
                        <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label>Họ và tên *</Form.Label>
                        <Form.Control name="fullName" value={formData.fullName} onChange={handleChange} isInvalid={!!errors.fullName} />
                        <Form.Control.Feedback type="invalid">{errors.fullName}</Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>
                  <Form.Group className="mb-3">
                    <Form.Label>Email *</Form.Label>
                    <Form.Control type="email" name="email" value={formData.email} onChange={handleChange} isInvalid={!!errors.email} />
                    <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group className="mb-3">
                    <Form.Label>Số điện thoại</Form.Label>
                    <Form.Control name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} />
                  </Form.Group>
                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label>Mật khẩu *</Form.Label>
                        <Form.Control type="password" name="password" value={formData.password} onChange={handleChange} isInvalid={!!errors.password} />
                        <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label>Xác nhận mật khẩu *</Form.Label>
                        <Form.Control type="password" name="confirmPassword" value={formData.confirmPassword} onChange={handleChange} isInvalid={!!errors.confirmPassword} />
                        <Form.Control.Feedback type="invalid">{errors.confirmPassword}</Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>
                  <Button variant="primary" type="submit" className="w-100 py-2" disabled={loading}>
                    {loading ? 'Đang xử lý...' : 'Đăng ký'}
                  </Button>
                </Form>
                <div className="text-center mt-3">
                  <small>Đã có tài khoản? <Link to="/login">Đăng nhập</Link></small>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </Container>
  );
};

export default Register;
