const express = require('express');
const router = express.Router();
const userController = require('../controllers/user.controller');
const { updateProfileValidation } = require('../validators/user.validator');
const validate = require('../middleware/validate');
const { protect } = require('../middleware/auth');
const multer = require('multer');

const upload = multer({ dest: 'uploads/' });

router.use(protect);

router.route('/profile')
  .get(userController.getProfile)
  .put(upload.single('profilePic'), updateProfileValidation, validate, userController.updateProfile)
  .delete(userController.deleteProfile);

module.exports = router;
