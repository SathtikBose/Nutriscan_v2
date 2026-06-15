const mongoose = require('mongoose');

const motivationSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  message: { type: String, required: true }
}, {
  timestamps: true
});

const Motivation = mongoose.model('Motivation', motivationSchema);
module.exports = Motivation;
