import React from 'react';
import { Alert } from 'react-bootstrap';

const AlertMessage = ({ type = 'info', message, onClose, dismissible = true }) => {
  if (!message) return null;
  return (
    <Alert variant={type} onClose={onClose} dismissible={dismissible}>
      {message}
    </Alert>
  );
};

export default AlertMessage;
