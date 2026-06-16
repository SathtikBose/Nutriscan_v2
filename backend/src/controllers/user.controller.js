const userService = require('../services/user.service');

exports.getProfile = async (req, res, next) => {
  try {
    const user = await userService.getProfile(req.user.id);
    res.status(200).json({ success: true, data: user });
  } catch (error) {
    next(error);
  }
};

exports.updateProfile = async (req, res, next) => {
  try {
    if (req.body.allergies && typeof req.body.allergies === 'string') {
      req.body.allergies = req.body.allergies.split(',').map(s => s.trim()).filter(Boolean);
    }
    if (req.body.dietaryPreferences && typeof req.body.dietaryPreferences === 'string') {
      req.body.dietaryPreferences = req.body.dietaryPreferences.split(',').map(s => s.trim()).filter(Boolean);
    }
    const user = await userService.updateProfile(req.user.id, req.body, req.file);
    res.status(200).json({ success: true, data: user });
  } catch (error) {
    next(error);
  }
};

exports.deleteProfile = async (req, res, next) => {
  try {
    await userService.deleteProfile(req.user.id);
    res.status(200).json({ success: true, data: {} });
  } catch (error) {
    next(error);
  }
};
