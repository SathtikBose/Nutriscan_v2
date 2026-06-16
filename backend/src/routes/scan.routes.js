const express = require('express');
const router = express.Router();
const scanController = require('../controllers/scan.controller');
const { protect } = require('../middleware/auth');
const multer = require('multer');

const upload = multer({ storage: multer.memoryStorage() });

router.use(protect);

router.post('/analyze', upload.single('image'), scanController.analyzeFood);
router.get('/history', scanController.getHistory);
router.get('/:scanId', scanController.getScanById);
router.delete('/:scanId', scanController.deleteScan);

module.exports = router;
