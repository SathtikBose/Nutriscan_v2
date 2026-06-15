const motivationService = require('../services/motivation.service');

exports.getDailyMotivation = async (req, res, next) => {
  try {
    const motivation = await motivationService.getDailyMotivation(req.user.id);
    res.status(200).json({ success: true, data: motivation });
  } catch (error) {
    next(error);
  }
};
