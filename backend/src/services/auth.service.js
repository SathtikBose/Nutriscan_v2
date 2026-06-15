const User = require('../models/User');
const { generateToken, generateRefreshToken } = require('../utils/token');
const { generateOTP } = require('../utils/otp');
const sendEmail = require('../utils/email');

exports.signup = async (data) => {
  const { name, email, password } = data;
  let user = await User.findOne({ email });
  if (user) {
    const err = new Error('User already exists');
    err.statusCode = 400;
    throw err;
  }

  user = await User.create({ name, email, password });
  const token = generateToken(user._id);
  const refreshToken = generateRefreshToken(user._id);
  user.refreshToken = refreshToken;
  await user.save();

  return { success: true, token, refreshToken, user: { id: user._id, name, email } };
};

exports.login = async (data) => {
  const { email, password } = data;
  const user = await User.findOne({ email });
  if (!user || !(await user.matchPassword(password))) {
    const err = new Error('Invalid credentials');
    err.statusCode = 401;
    throw err;
  }

  const token = generateToken(user._id);
  const refreshToken = generateRefreshToken(user._id);
  user.refreshToken = refreshToken;
  await user.save();

  return { success: true, token, refreshToken, user: { id: user._id, name: user.name, email: user.email } };
};

exports.forgotPassword = async (email) => {
  // Implementation stub for forgot password
  return { success: true, message: 'OTP sent to email' };
};

exports.verifyOtp = async (data) => {
  // Implementation stub for verify OTP
  return { success: true, message: 'OTP verified' };
};

exports.resetPassword = async (data) => {
  // Implementation stub for reset password
  return { success: true, message: 'Password reset successful' };
};

exports.changePassword = async (userId, data) => {
  // Implementation stub for change password
  return { success: true, message: 'Password changed successfully' };
};

exports.refreshToken = async (token) => {
  // Implementation stub for refresh token
  return { success: true, token: 'new-token' };
};

exports.logout = async (userId) => {
  const user = await User.findById(userId);
  if (user) {
    user.refreshToken = null;
    await user.save();
  }
  return { success: true, message: 'Logged out successfully' };
};
