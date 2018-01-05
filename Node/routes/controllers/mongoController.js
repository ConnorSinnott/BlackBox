'use strict';
var mongoose = require('mongoose');

// Connection string for MongoDB
var connectionString = process.env.MONGO_CONN_STRING;

module.exports = {
    getMongo: function () {
        if (mongoose.connection.readyState === 0) {
            console.log('Connecting with' + connectionString);
            mongoose.connect(connectionString);
        }
        return mongoose;
    }
};