var express = require('express');
var momentUtil = require('./utils/momentUtil');
var router = express.Router();

// Define User schema
var User = require('./schemas/user.js');

// Render index.jade
router.get('/', function (req, res) {
    res.render('index');
})

// get Displays all Docs in MongoDB
router.get('/display', function (req, res) {
    User.find(function (req, docs) {
        res.send(docs);
    });
});

// post Adds new User to MongoDB
router.post('/new', function (req, res) {
    // If submitted user does not exist, create new user in MongoDB
    User.find({'userID': req.body.userID}, function (err, docs) {
        if (docs.length === 0) {
            var newUser = new User(req.body);
            newUser = momentUtil.addTimeStamp(newUser, 'expirationTime', req.body.duration); // Current time + duration
            if(req.body.contactList){
                newUser.contactList = req.body.contactList;
            }else{
                newUser.contactList = ['12065558874'];
            }

            // Save new user to MongoDB
            newUser.save(function (err, doc) {
                res.send(doc);
            });
        } else {
            res.send('User ' + req.body.userID + ' already exists!');
        }
    });
});

// post Updates MongoDB User's expirationTime
router.post('/ping', function (req, res) {
    User.findOne({'userID': req.body.userID}, {}, function (err, doc) {
        if (doc) {
            var user = doc;
            user = momentUtil.addTimeStamp(doc, 'expirationTime', doc.duration);
            user.save(function (err, doc) {
                res.send(doc);
            })
        } else {
            res.send('User does not exist!');
        }
    });
});

// post Removes User from MongoDB
router.post('/stop', function (req, res) {
    User.findOne({'userID': req.body.userID}, {}, function (err, doc) {
        if (doc) {
            User.collection.remove(doc, function (err, result) {
                if (err) {
                    res.send(err);
                } else {
                    res.send(result);
                }
            });
        } else {
            res.send('User does not exist!');
        }
    });
});

// get Empties User collection from MongoDB
router.get('/empty', function (req, res) {
    User.collection.remove();
    res.send('Success');
});

module.exports = router;