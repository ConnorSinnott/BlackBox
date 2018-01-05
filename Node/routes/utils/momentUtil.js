'use strict';

var moment = require('moment');
var MOMENT_FORMAT = 'YYYY-MM-DD HH:mm:ss';

module.exports = {
    addTimeStamp: function (obj, columnName, addition) {
        obj[columnName] = moment().add(addition, 'second').format(MOMENT_FORMAT);
        return obj;
    },
    getTime: function(string) {
        return moment(string, MOMENT_FORMAT);
    }
};