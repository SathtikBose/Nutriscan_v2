require('dotenv').config();

const requiredEnvVars = [
    'MONGODB_URI',
    'JWT_SECRET',
    'JWT_REFRESH_SECRET',
    'CLOUDINARY_CLOUD_NAME',
    'CLOUDINARY_API_KEY',
    'CLOUDINARY_API_SECRET',
    'GEMINI_API_KEY',
    'RESEND_API_KEY'
];

exports.validateEnv = () => {
    const missing = requiredEnvVars.filter(envVar => !process.env[envVar]);
    
    if (missing.length > 0) {
        console.error(`❌ Missing required environment variables: ${missing.join(', ')}`);
        console.error('⚠️ Server might not function correctly on Vercel without these variables.');
        // Removed process.exit(1) to prevent Vercel FUNCTION_INVOCATION_FAILED errors
    } else {
        console.log('✅ Environment variables validated successfully.');
    }
};
