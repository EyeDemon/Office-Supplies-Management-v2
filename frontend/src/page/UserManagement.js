import React, { useState, useEffect, useMemo } from 'react';
import { Container, Row, Col, Button, Alert, Card, Badge } from 'react-bootstrap';
import { userAPI } from '../services/api';
import UserList from '../components/user/UserList';
import UserForm from '../components/user/UserForm';
import UserFilter from '../components/user/UserFilter';
import LoadingSpinner from '../components/common/LoadingSpinner';

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [formLoading, setFormLoading] = useState(false);
  const [filter, setFilter] = useState({ search: '', role: '' });

  useEffect(() => { loadUsers(); }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const response = await userAPI.getAllUsers();
      setUsers(response.data.data?.users || []);
    } catch (err) {
      setError('Lỗi tải danh sách: ' + (err.response?.data?.message || err.message));
    } finally { setLoading(false); }
  };

  const filteredUsers = useMemo(() => {
    return users.filter(u => {
      const matchRole = !filter.role || u.role === filter.role;
      const q = filter.search.toLowerCase();
      const matchSearch = !q ||
        u.username?.toLowerCase().includes(q) ||
        u.fullName?.toLowerCase().includes(q) ||
        u.email?.toLowerCase().includes(q) ||
        u.phoneNumber?.includes(q);
      return matchRole && matchSearch;
    });
  }, [users, filter]);

  const handleCreate = async (userData) => {
    setFormLoading(true);
    try {
      await userAPI.createUser(userData);
      setShowForm(false);
      setSuccess('Tạo người dùng thành công!');
      loadUsers();
    } catch (err) {
      setError('Lỗi tạo: ' + (err.response?.data?.message || err.message));
    } finally { setFormLoading(false); }
  };

  const handleUpdate = async (userData) => {
    setFormLoading(true);
    try {
      await userAPI.updateUser(editingUser.id, userData);
      setShowForm(false); setEditingUser(null);
      setSuccess('Cập nhật thành công!');
      loadUsers();
    } catch (err) {
      setError('Lỗi cập nhật: ' + (err.response?.data?.message || err.message));
    } finally { setFormLoading(false); }
  };

  const handleDelete = async (userId) => {
    try {
      await userAPI.deleteUser(userId);
      setSuccess('Đã xóa người dùng!');
      loadUsers();
    } catch (err) {
      setError('Lỗi xóa: ' + (err.response?.data?.message || err.message));
    }
  };

  if (loading && users.length === 0) return <LoadingSpinner message="Đang tải..." />;

  return (
    <Container>
      <Row className="mb-3">
        <Col>
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h1 className="mb-0">Quản Lý Người Dùng</h1>
              <p className="text-muted mb-0">
                Tổng: <Badge bg="secondary">{users.length}</Badge>&nbsp;
                Hiển thị: <Badge bg="primary">{filteredUsers.length}</Badge>
              </p>
            </div>
            {!showForm && (
              <Button variant="primary" onClick={() => { setShowForm(true); setEditingUser(null); }}>
                + Thêm người dùng
              </Button>
            )}
          </div>
        </Col>
      </Row>

      {error   && <Alert variant="danger"  dismissible onClose={() => setError('')}>{error}</Alert>}
      {success && <Alert variant="success" dismissible onClose={() => setSuccess('')}>{success}</Alert>}

      {showForm ? (
        <Card className="card-shadow mb-3">
          <Card.Body>
            <Card.Title>{editingUser ? '✏️ Chỉnh sửa' : '➕ Thêm người dùng mới'}</Card.Title>
            <UserForm
              user={editingUser}
              onSubmit={editingUser ? handleUpdate : handleCreate}
              onCancel={() => { setShowForm(false); setEditingUser(null); }}
              loading={formLoading}
            />
          </Card.Body>
        </Card>
      ) : (
        <>
          <UserFilter onFilter={setFilter} />
          <UserList users={filteredUsers} onEdit={(u) => { setEditingUser(u); setShowForm(true); }} onDelete={handleDelete} loading={loading} />
        </>
      )}
    </Container>
  );
};

export default UserManagement;
