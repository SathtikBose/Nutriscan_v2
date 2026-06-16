const Motivation = require('../models/Motivation');

exports.getDailyMotivation = async (userId) => {
  // Check if there is a motivation for today
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  let motivation = await Motivation.findOne({
    userId,
    createdAt: { $gte: today }
  });

  if (!motivation) {
    // Generate new motivation using Groq
    const groqService = require('./groq.service');
    const message = await groqService.generateMotivationMessage();
    
    motivation = await Motivation.create({
      userId,
      message
    });
  }

  return motivation;
};
