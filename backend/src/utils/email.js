const { Resend } = require('resend');

const sendEmail = async ({ to, subject, html }) => {
  try {
    const resend = new Resend(process.env.RESEND_API_KEY);
    const { data, error } = await resend.emails.send({
      from: process.env.RESEND_FROM_EMAIL || 'onboarding@resend.dev',
      to,
      subject,
      html
    });
    if (error) {
      console.error('Resend API error:', error);
      return { success: false, error };
    }
    return { success: true, data };
  } catch (error) {
    console.error('Email send error:', error);
    return { success: false, error };
  }
};

module.exports = sendEmail;
