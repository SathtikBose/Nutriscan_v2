const groq = require('../config/groq');

exports.analyzeFoodImage = async (file, userContext) => {
    try {
        const prompt = `Analyze this food product image. 
User context: Age ${userContext.age || 'unknown'}, Allergies: ${(userContext.allergies && userContext.allergies.length > 0) ? userContext.allergies.join(', ') : 'none'}, Dietary Preferences: ${(userContext.dietaryPreferences && userContext.dietaryPreferences.length > 0) ? userContext.dietaryPreferences.join(', ') : 'none'}, Weight: ${userContext.weight || 'unknown'}kg, Height: ${userContext.height || 'unknown'}cm.

Please extract nutritional information, ingredients, and evaluate it for the user. 
IMPORTANT: If the product contains the user's allergies or violates their dietary preferences, you MUST set "severity" to "dangerous" and warn them heavily in "recommendation" and "explanation".
Return ONLY a JSON object with this exact structure (no markdown, no backticks, just the raw JSON object):
{
  "productName": "string",
  "productDescription": "string",
  "productScore": number (0-100),
  "productScoreStatus": "string (good/moderate/bad)",
  "goodIngredients": ["string"],
  "badIngredients": ["string"],
  "allIngredients": ["string"],
  "allergenWarnings": ["string"],
  "severity": "string (safe/caution/dangerous)",
  "betterAlternatives": ["string"],
  "nutritionSummary": {
    "calories": "string",
    "protein": "string",
    "fat": "string",
    "carbs": "string",
    "sugar": "string",
    "fiber": "string",
    "sodium": "string",
    "servingSize": "string"
  },
  "recommendation": "string",
  "explanation": "string"
}`;

        // Convert file buffer to base64 data URL
        const base64Image = file.buffer.toString("base64");
        const mimeType = file.mimetype === 'image/*' ? 'image/jpeg' : file.mimetype;
        const dataUrl = `data:${mimeType};base64,${base64Image}`;

        const chatCompletion = await groq.chat.completions.create({
            messages: [
                {
                    role: 'user',
                    content: [
                        { type: 'text', text: prompt },
                        {
                            type: 'image_url',
                            image_url: {
                                url: dataUrl,
                            },
                        },
                    ],
                },
            ],
            model: 'meta-llama/llama-4-scout-17b-16e-instruct',
            response_format: { type: "json_object" }
        });

        const textResponse = chatCompletion.choices[0].message.content;
        const parsedResult = JSON.parse(textResponse);
        return {
            ...parsedResult,
            aiRawResponse: textResponse
        };
    } catch (error) {
        console.error("Groq API Error:", error);
        throw new Error("Failed to analyze food image with AI");
    }
};

exports.generateMotivationMessage = async () => {
    try {
        const prompt = "Generate a short, engaging, and highly motivating one-sentence tip about healthy eating and nutrition. Do not include quotes or surrounding text.";
        const chatCompletion = await groq.chat.completions.create({
            messages: [
                {
                    role: 'user',
                    content: prompt,
                },
            ],
            model: 'llama-3.1-8b-instant',
        });
        return chatCompletion.choices[0].message.content.trim();
    } catch (error) {
        console.error("Groq API Error:", error);
        return "Every healthy choice you make today is an investment in your tomorrow. Scan before you snack!";
    }
};
