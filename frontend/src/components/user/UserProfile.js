import React from 'react';
import { Container, Card } from 'react-bootstrap';
import { useAuth } from '../../contexts/AuthContext';

const UserProfile = () => {
  const { user } = useAuth();

  return (
    <Container>
      <div className="row justify-content-center">
        <div className="col-md-8">
          <Card className="card-shadow">
            <Card.Body>
              <Card.Title className="text-center mb-4">👤 Hồ Sơ Cá Nhân</Card.Title>
              
              <div className="row">
                <div className="col-md-6">
                  <h6>Thông tin tài khoản</h6>
                  <p><strong>Tên đăng nhập:</strong> {user?.username}</p>
                  <p><strong>Email:</strong> {user?.email}</p>
                  <p><strong>Vai trò:</strong> {user?.role === 'ADMIN' ? 'Quản trị viên' : 'Người dùng'}</p>
                </div>
                
                <div className="col-md-6">
                  <h6>Thông tin cá nhân</h6>
                  <p><strong>Họ và tên:</strong> {user?.fullName || 'Chưa cập nhật'}</p>
                  <p><strong>Số điện thoại:</strong> {user?.phoneNumber || 'Chưa cập nhật'}</p>
                  <p><strong>Ngày tạo:</strong> {user?.createdAt ? new Date(user.createdAt).toLocaleDateString('vi-VN') : 'N/A'}</p>
                </div>
              </div>
              
              <div className="text-center mt-4">
                <small className="text-muted">
                  Liên hệ quản trị viên để thay đổi thông tin cá nhân
                </small>
              </div>
            </Card.Body>
          </Card>
        </div>
      </div>
    </Container>
  );
};

export default UserProfile;