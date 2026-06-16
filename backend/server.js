require('dotenv').config();
const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const mongoSanitize = require('express-mongo-sanitize');
const xss = require('xss-clean');
const morgan = require('morgan');
const connectDB = require('./src/config/db');
const errorHandler = require('./src/middleware/error');
const { generalLimiter } = require('./src/middleware/rateLimiter');

const { validateEnv } = require('./src/utils/env');

// Validate environment variables
validateEnv();

// Connect to database
connectDB();

const app = express();

// Trust proxy for Vercel and rate limiting
app.set('trust proxy', 1);

// Security Middleware
app.use(helmet());
app.use(cors());
app.use(mongoSanitize());
// app.use(xss()); // Removed deprecated xss-clean

// Body parser
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Rate limiting
app.use(generalLimiter);

// Logging
if (process.env.NODE_ENV === 'development') {
  app.use(morgan('dev'));
} else {
  app.use(morgan('combined'));
}

// Routes
const authRoutes = require('./src/routes/auth.routes');
const userRoutes = require('./src/routes/user.routes');
const scanRoutes = require('./src/routes/scan.routes');
const motivationRoutes = require('./src/routes/motivation.routes');
const healthRoutes = require('./src/routes/health.routes');

app.use('/api/auth', authRoutes);
app.use('/api/user', userRoutes);
app.use('/api/scan', scanRoutes);
app.use('/api/motivation', motivationRoutes);
app.use('/api/health', healthRoutes);

app.get('/', (req, res) => {
  res.json({ message: 'Welcome to NutriScan API' });
});

// Global error handler
app.use(errorHandler);

// Port
const PORT = process.env.PORT || 3000;

if (process.env.NODE_ENV !== 'production') {
  app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
  });
}

module.exports = app;
