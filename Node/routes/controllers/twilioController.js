// Use twilio to send passed SMS message to passed phone number
var twilio = require('twilio');

// Find your account sid and auth token in your Twilio account Console.
var client = twilio(process.env.TWILIO_STR1, process.env.TWILIO_STR2);

module.exports = {
    sendSMS: function(pTo, pBody){
        console.log('Sending to: ' + pTo + ' \nBody: ' + pBody);
        client.sendMessage({
            to: pTo,
            from: '+12065553312',
            body: pBody
        });
    }
};
