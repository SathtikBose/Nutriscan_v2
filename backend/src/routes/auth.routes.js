const express = require('express');
const router = express.Router();
const authController = require('../controllers/auth.controller');
const { 
  signupValidation, 
  loginValidation, 
  forgotPasswordValidation, 
  verifyOtpValidation, 
  resetPasswordValidation, 
  changePasswordValidation 
} = require('../validators/auth.validator');
const validate = require('../middleware/validate');
const { protect } = require('../middleware/auth');
const { authLimiter } = require('../middleware/rateLimiter');

router.post('/signup', signupValidation, validate, authController.signup);
router.post('/login', authLimiter, loginValidation, validate, authController.login);
router.post('/forgot-password', authLimiter, forgotPasswordValidation, validate, authController.forgotPassword);
router.post('/verify-otp', authLimiter, verifyOtpValidation, validate, authController.verifyOtp);
router.post('/reset-password', authLimiter, resetPasswordValidation, validate, authController.resetPassword);
router.post('/change-password', protect, changePasswordValidation, validate, authController.changePassword);
router.post('/refresh-token', authController.refreshToken);
router.post('/logout', protect, authController.logout);

module.exports = router;
