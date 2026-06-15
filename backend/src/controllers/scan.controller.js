const scanService = require('../services/scan.service');

exports.analyzeFood = async (req, res, next) => {
  try {
    const result = await scanService.analyzeFood(req.user, req.file);
    res.status(201).json({ success: true, data: result });
  } catch (error) {
    next(error);
  }
};

exports.getHistory = async (req, res, next) => {
  try {
    const { filter, sort, search } = req.query;
    const history = await scanService.getHistory(req.user.id, filter, sort, search);
    res.status(200).json({ success: true, data: history });
  } catch (error) {
    next(error);
  }
};

exports.getScanById = async (req, res, next) => {
  try {
    const scan = await scanService.getScanById(req.user.id, req.params.scanId);
    res.status(200).json({ success: true, data: scan });
  } catch (error) {
    next(error);
  }
};

exports.deleteScan = async (req, res, next) => {
  try {
    await scanService.deleteScan(req.user.id, req.params.scanId);
    res.status(200).json({ success: true, data: {} });
  } catch (error) {
    next(error);
  }
};
