const ai = require('../config/gemini');

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

        const response = await ai.models.generateContent({
            model: 'gemini-2.5-flash',
            contents: [
                {
                    inlineData: {
                        data: file.buffer.toString("base64"),
                        mimeType: file.mimetype === 'image/*' ? 'image/jpeg' : file.mimetype
                    }
                },
                prompt
            ]
        });

        const textResponse = response.text;
        
        // Clean up markdown block if present
        let cleanJson = textResponse.trim();
        if (cleanJson.startsWith('```json')) {
            cleanJson = cleanJson.substring(7);
        }
        if (cleanJson.startsWith('```')) {
            cleanJson = cleanJson.substring(3);
        }
        if (cleanJson.endsWith('```')) {
            cleanJson = cleanJson.substring(0, cleanJson.length - 3);
        }

        const parsedResult = JSON.parse(cleanJson);
        return {
            ...parsedResult,
            aiRawResponse: textResponse
        };
    } catch (error) {
        console.error("Gemini API Error:", error);
        throw new Error("Failed to analyze food image with AI");
    }
};

exports.generateMotivationMessage = async () => {
    try {
        const prompt = "Generate a short, engaging, and highly motivating one-sentence tip about healthy eating and nutrition. Do not include quotes or surrounding text.";
        const response = await ai.models.generateContent({
            model: 'gemini-2.5-flash',
            contents: prompt
        });
        return response.text.trim();
    } catch (error) {
        console.error("Gemini API Error:", error);
        return "Every healthy choice you make today is an investment in your tomorrow. Scan before you snack!";
    }
};
