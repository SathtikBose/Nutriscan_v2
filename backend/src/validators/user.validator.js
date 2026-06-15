const { body } = require('express-validator');

exports.updateProfileValidation = [
  body('name').optional().notEmpty().withMessage('Name cannot be empty'),
  body('age').optional().isNumeric().withMessage('Age must be a number'),
  body('weight').optional().isNumeric().withMessage('Weight must be a number'),
  body('height').optional().isNumeric().withMessage('Height must be a number'),
  body('allergies').optional().isArray().withMessage('Allergies must be an array'),
  body('dietaryPreferences').optional().isArray().withMessage('Dietary preferences must be an array')
];
