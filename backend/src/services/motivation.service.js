const Motivation = require('../models/Motivation');
const ai = require('../config/gemini');

exports.getDailyMotivation = async (userId) => {
  // Check if there is a motivation for today
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  let motivation = await Motivation.findOne({
    userId,
    createdAt: { $gte: today }
  });

  if (!motivation) {
    // Generate new motivation using Gemini
    const geminiService = require('./gemini.service');
    const message = await geminiService.generateMotivationMessage();
    
    motivation = await Motivation.create({
      userId,
      message
    });
  }

  return motivation;
};
