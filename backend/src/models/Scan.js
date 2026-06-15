const mongoose = require('mongoose');

const scanSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  productName: { type: String, required: true },
  productDescription: { type: String },
  productImage: { type: String, required: true },
  productScore: { type: Number, required: true },
  productScoreStatus: { type: String, required: true }, // good, moderate, bad
  productScoreColour: { type: String },
  goodIngredients: { type: [String], default: [] },
  badIngredients: { type: [String], default: [] },
  allIngredients: { type: [String], default: [] },
  allergenWarnings: { type: [String], default: [] },
  severity: { type: String, default: 'safe' }, // safe, caution, dangerous
  betterAlternatives: { type: [String], default: [] },
  nutritionSummary: {
    calories: String,
    protein: String,
    carbs: String,
    fat: String,
    saturatedFat: String,
    sugar: String,
    sodium: String,
    fiber: String,
    servingSize: String
  },
  recommendation: { type: String },
  explanation: { type: String },
  aiRawResponse: { type: String }
}, {
  timestamps: true
});

const Scan = mongoose.model('Scan', scanSchema);
module.exports = Scan;
