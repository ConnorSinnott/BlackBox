'use strict';

var mongoose = require('../controllers/mongoController.js').getMongo();

// Define userSchema schema
var userSchema = {
    userID: String,
    contactList: [],
    message: String,
    expirationTime: String,
    duration: Number
};

// Using "User" collection
module.exports = mongoose.model('User', mongoose.Schema(userSchema));