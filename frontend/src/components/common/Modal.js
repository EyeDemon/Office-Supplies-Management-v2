import React from 'react';
import { Modal as BsModal, Button } from 'react-bootstrap';

const Modal = ({
  show,
  onHide,
  title,
  children,
  onConfirm,
  confirmLabel = 'Xác nhận',
  confirmVariant = 'primary',
  cancelLabel = 'Hủy',
  size,
}) => {
  return (
    <BsModal show={show} onHide={onHide} size={size} centered>
      <BsModal.Header closeButton>
        <BsModal.Title>{title}</BsModal.Title>
      </BsModal.Header>
      <BsModal.Body>{children}</BsModal.Body>
      <BsModal.Footer>
        <Button variant="secondary" onClick={onHide}>{cancelLabel}</Button>
        {onConfirm && (
          <Button variant={confirmVariant} onClick={onConfirm}>{confirmLabel}</Button>
        )}
      </BsModal.Footer>
    </BsModal>
  );
};

export default Modal;
