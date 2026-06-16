const jwt = require('jsonwebtoken');
const User = require('../models/User');

const protect = async (req, res, next) => {
  let token;

  console.log('Protect Middleware - Authorization Header:', req.headers.authorization);

  if (req.headers.authorization && req.headers.authorization.startsWith('Bearer')) {
    token = req.headers.authorization.split(' ')[1];
  }

  if (!token) {
    console.log('Protect Middleware - No token found in request headers');
    return res.status(401).json({ success: false, message: 'Not authorized to access this route' });
  }

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    console.log('Protect Middleware - Token verified successfully, user ID:', decoded.id);
    req.user = await User.findById(decoded.id).select('-password');
    if (!req.user) {
        console.log('Protect Middleware - User not found in database for ID:', decoded.id);
        return res.status(401).json({ success: false, message: 'User not found' });
    }
    next();
  } catch (err) {
    console.error('Protect Middleware - JWT verification failed:', err.message);
    return res.status(401).json({ success: false, message: 'Not authorized to access this route' });
  }
};

module.exports = { protect };
