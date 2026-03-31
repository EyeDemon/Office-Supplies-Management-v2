import React from 'react';
import { Button, Badge } from 'react-bootstrap';

const UserItem = ({ user, onEdit, onDelete, currentUserId }) => {
  const isCurrentUser = user.id === currentUserId;

  return (
    <tr>
      <td><strong>{user.username}</strong>{isCurrentUser && <Badge bg="info" className="ms-2">Bạn</Badge>}</td>
      <td>{user.fullName}</td>
      <td>{user.email}</td>
      <td>{user.phoneNumber || '-'}</td>
      <td>
        <Badge bg={user.role === 'ADMIN' ? 'danger' : 'primary'}>
          {user.role === 'ADMIN' ? 'Quản trị' : 'Người dùng'}
        </Badge>
      </td>
      <td>
        <div className="d-flex gap-1">
          <Button variant="outline-primary" size="sm" onClick={() => onEdit(user)}>Sửa</Button>
          <Button variant="outline-danger" size="sm" onClick={() => onDelete(user.id)} disabled={isCurrentUser}>Xóa</Button>
        </div>
      </td>
    </tr>
  );
};

export default UserItem;
