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
  const user = await User.findOne({ email });
  if (!user) {
    const err = new Error('No account found with this email');
    err.statusCode = 404;
    throw err;
  }
  
  const otp = generateOTP();
  user.resetPasswordOtp = otp;
  user.resetPasswordOtpExpiry = Date.now() + 15 * 60 * 1000; // 15 mins
  await user.save();
  
  const emailHtml = `
    <h1>Password Reset</h1>
    <p>Your OTP for password reset is: <strong>${otp}</strong></p>
    <p>This OTP is valid for 15 minutes.</p>
  `;
  
  const emailResult = await sendEmail({
    to: email,
    subject: 'Password Reset OTP - NutriScan',
    html: emailHtml
  });

  if (!emailResult.success) {
    const err = new Error('Failed to send OTP email. Note: If using Resend test API key, you can only send to your verified email.');
    err.statusCode = 500;
    throw err;
  }

  return { success: true, message: 'OTP sent to email' };
};

exports.verifyOtp = async (data) => {
  const { email, otp } = data;
  const user = await User.findOne({ 
    email, 
    resetPasswordOtp: otp,
    resetPasswordOtpExpiry: { $gt: Date.now() }
  });
  
  if (!user) {
    const err = new Error('Invalid or expired OTP');
    err.statusCode = 400;
    throw err;
  }
  
  return { success: true, message: 'OTP verified' };
};

exports.resetPassword = async (data) => {
  const { email, otp, newPassword } = data;
  const user = await User.findOne({ 
    email, 
    resetPasswordOtp: otp,
    resetPasswordOtpExpiry: { $gt: Date.now() }
  });
  
  if (!user) {
    const err = new Error('Invalid or expired OTP');
    err.statusCode = 400;
    throw err;
  }
  
  user.password = newPassword;
  user.resetPasswordOtp = undefined;
  user.resetPasswordOtpExpiry = undefined;
  await user.save();
  
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
