import React, { useState, useMemo } from 'react';
import { Table, Button, Badge, Modal } from 'react-bootstrap';
import storageService from '../../services/storageService';

const UserList = ({ users, onEdit, onDelete, loading = false }) => {
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  // FIX: Tránh gọi localStorage.getItem() trong mỗi vòng lặp render
  const currentUserId = useMemo(() => storageService.getUser()?.id, []);

  const handleDeleteClick = (user) => {
    setSelectedUser(user);
    setShowDeleteModal(true);
  };

  const handleConfirmDelete = () => {
    if (selectedUser) {
      onDelete(selectedUser.id);
      setShowDeleteModal(false);
      setSelectedUser(null);
    }
  };

  const getRoleVariant = (role) => (role === 'ADMIN' ? 'danger' : 'primary');

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('vi-VN');
  };

  if (loading) {
    return (
      <div className="text-center py-4">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-2 text-muted">Đang tải danh sách người dùng...</p>
      </div>
    );
  }

  if (!users || users.length === 0) {
    return (
      <div className="text-center py-4">
        <p className="text-muted">Không có người dùng nào được tìm thấy.</p>
      </div>
    );
  }

  return (
    <>
      <div className="table-responsive">
        <Table striped bordered hover>
          <thead className="table-dark">
            <tr>
              <th>#</th>
              <th>Tên đăng nhập</th>
              <th>Họ tên</th>
              <th>Email</th>
              <th>Số điện thoại</th>
              <th>Vai trò</th>
              <th>Ngày tạo</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user, index) => (
              <tr key={user.id}>
                <td>{index + 1}</td>
                <td>
                  <strong>{user.username}</strong>
                  {user.id === currentUserId && (
                    <Badge bg="info" className="ms-2">Bạn</Badge>
                  )}
                </td>
                <td>{user.fullName}</td>
                <td>{user.email}</td>
                <td>{user.phoneNumber || '-'}</td>
                <td>
                  <Badge bg={getRoleVariant(user.role)}>
                    {user.role === 'ADMIN' ? 'Quản trị' : 'Người dùng'}
                  </Badge>
                </td>
                <td>{formatDate(user.createdAt)}</td>
                <td>
                  <div className="d-flex gap-1">
                    <Button variant="outline-primary" size="sm" onClick={() => onEdit(user)}>
                      Sửa
                    </Button>
                    <Button
                      variant="outline-danger"
                      size="sm"
                      onClick={() => handleDeleteClick(user)}
                      disabled={user.id === currentUserId}
                    >
                      Xóa
                    </Button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>

      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Xác nhận xóa</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Bạn có chắc chắn muốn xóa người dùng <strong>{selectedUser?.username}</strong>?
          <br />
          <span className="text-danger">Hành động này không thể hoàn tác!</span>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>Hủy</Button>
          <Button variant="danger" onClick={handleConfirmDelete}>Xóa</Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default UserList;
