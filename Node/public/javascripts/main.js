/**
 * Created by spectre on 3/5/17.
 */
'use strict';
$(document).ready(function () {

    var elNewUserId = $('#newUserId');
    var elNewMessage = $('#newMessage');
    var elNewDuration = $('#newDuration');
    var elNewSubmit = $('#newSubmit');

    var elPingUserId = $('#pingUserId');
    var elPingSubmit = $('#pingSubmit');

    var elStopUserId = $('#stopUserId');
    var elStopSubmit = $('#stopSubmit');

    var elData = $('#data');

    function displayData() {
        $.get("/display", function (result) {
            elData.html(JSON.stringify(result, null, 4));
            setTimeout(displayData, 2000);
        });
    }

    displayData();

    elNewSubmit.click(function (e) {
        e.preventDefault();
        $.post("/new", {
            userID: elNewUserId.val(),
            message: elNewMessage.val(),
            duration: elNewDuration.val(),
        }, function (result) {

        })
    });

    elPingSubmit.click(function (e) {
        e.preventDefault();
        $.post("/ping", {
            userID: elPingUserId.val()
        }, function (result) {

        })
    });

    elStopSubmit.click(function (e) {
        e.preventDefault();
        $.post("/stop", {
            userID: elStopUserId.val()
        }, function (result) {

        })
    });

});
