import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';

const UserFilter = ({ onFilter }) => {
  const [search, setSearch] = useState('');
  const [role, setRole] = useState('');

  const handleFilter = () => onFilter({ search, role });

  const handleReset = () => {
    setSearch('');
    setRole('');
    onFilter({ search: '', role: '' });
  };

  return (
    <Row className="mb-3 g-2">
      <Col md={5}>
        <Form.Control
          placeholder="Tìm theo tên, email, username..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && handleFilter()}
        />
      </Col>
      <Col md={3}>
        <Form.Select value={role} onChange={(e) => setRole(e.target.value)}>
          <option value="">Tất cả vai trò</option>
          <option value="ADMIN">Quản trị viên</option>
          <option value="USER">Người dùng</option>
        </Form.Select>
      </Col>
      <Col md="auto">
        <Button variant="primary" onClick={handleFilter}>Lọc</Button>
      </Col>
      <Col md="auto">
        <Button variant="outline-secondary" onClick={handleReset}>Đặt lại</Button>
      </Col>
    </Row>
  );
};

export default UserFilter;
