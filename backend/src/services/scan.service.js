const Scan = require('../models/Scan');
const User = require('../models/User');
const cloudinary = require('../config/cloudinary');
const ai = require('../config/gemini');

exports.analyzeFood = async (user, file) => {
  if (!file) {
    const err = new Error('No image provided');
    err.statusCode = 400;
    throw err;
  }

  // 1. Upload to Cloudinary
  const result = await cloudinary.uploader.upload(file.path, {
    folder: 'nutriscan/scans'
  });
  const imageUrl = result.secure_url;

  // 2. Prepare Gemini Context
  const userContext = {
    age: user.age,
    allergies: user.allergies,
    weight: user.weight,
    height: user.height
  };

  // 3. Call Gemini API using the uploaded file path
  const geminiService = require('./gemini.service');
  const aiResult = await geminiService.analyzeFoodImage(file.path, userContext);
  
  // Set default score color based on status
  let scoreColour = "#F59E0B"; // Moderate (Amber)
  if (aiResult.productScoreStatus?.toLowerCase() === 'good') scoreColour = "#22C55E"; // Green
  else if (aiResult.productScoreStatus?.toLowerCase() === 'bad') scoreColour = "#EF4444"; // Red
  
  aiResult.productScoreColour = scoreColour;

  // 4. Save to Database
  const scan = await Scan.create({
    userId: user._id,
    productImage: imageUrl,
    ...aiResult
  });

  return scan;
};

exports.getHistory = async (userId, filter, sort, search) => {
  let query = { userId };
  
  if (filter && filter !== 'All') {
    query.productScoreStatus = filter.toLowerCase();
  }

  if (search) {
    query.productName = { $regex: search, $options: 'i' };
  }

  let sortObj = { createdAt: -1 };
  if (sort === 'Score') {
    sortObj = { productScore: -1 };
  }

  const scans = await Scan.find(query).sort(sortObj);
  return scans;
};

exports.getScanById = async (userId, scanId) => {
  const scan = await Scan.findOne({ _id: scanId, userId });
  if (!scan) {
    const err = new Error('Scan not found');
    err.statusCode = 404;
    throw err;
  }
  return scan;
};

exports.deleteScan = async (userId, scanId) => {
  const scan = await Scan.findOneAndDelete({ _id: scanId, userId });
  if (!scan) {
    const err = new Error('Scan not found');
    err.statusCode = 404;
    throw err;
  }
  return true;
};
