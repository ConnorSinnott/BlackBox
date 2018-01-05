// Runs passed function every given second
var cycle = require('./cycleController');

var timr = require('timr');
var scheduler = timr();

module.exports = {
    startTimer: function(intervalLength){
        scheduler().every(intervalLength).seconds().run(cycle.dbCheck);
    }
};