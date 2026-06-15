const User = require('../models/User');
const cloudinary = require('../config/cloudinary');

exports.getProfile = async (userId) => {
  const user = await User.findById(userId).select('-password -refreshToken');
  if (!user) {
    const err = new Error('User not found');
    err.statusCode = 404;
    throw err;
  }
  return user;
};

exports.updateProfile = async (userId, data, file) => {
  let profilePicUrl;
  if (file) {
    const result = await cloudinary.uploader.upload(file.path, {
      folder: 'nutriscan/profiles'
    });
    profilePicUrl = result.secure_url;
  }

  const updateData = { ...data };
  if (profilePicUrl) {
    updateData.profilePic = profilePicUrl;
  }

  const user = await User.findByIdAndUpdate(userId, updateData, { new: true, runValidators: true }).select('-password -refreshToken');
  if (!user) {
    const err = new Error('User not found');
    err.statusCode = 404;
    throw err;
  }
  return user;
};

exports.deleteProfile = async (userId) => {
  const user = await User.findByIdAndDelete(userId);
  if (!user) {
    const err = new Error('User not found');
    err.statusCode = 404;
    throw err;
  }
  return true;
};
