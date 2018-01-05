// Checks MongoDB for Users that have expired expirationTime
var moment = require('moment');
var User = require('../schemas/user');
var momentUtil = require('../utils/momentUtil');
var twilio = require('./twilioController');
var logController = require('./logController');

module.exports = {
    dbCheck: function () {
        User.find({}, function (err, docs) {
            var now = moment();
            docs.forEach(function (currentUser) {
                var userExpiration = momentUtil.getTime(currentUser.expirationTime);
                console.log(userExpiration.diff(now));
                if (userExpiration.diff(now) <= 0) {
                    doExecute(currentUser);
                }
            });
        });
    }
};

// Executes functionality when user expirationTime is reached
function doExecute(user) {
    logController.log(user.userID + ' has expired');
    user.contactList.forEach(function (contact) {
        logController.log('Sending message "' + user.message + '" to ' + contact);
        // twilio.sendSMS(contact, user.message);
        //console.log(contact);
    });
    User.collection.remove(user);
}