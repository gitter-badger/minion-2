define(function() {
    'use strict';

    var Minion = {};

    var commandEvents = new window.EventSource('/commands');
    Minion.command = function(event, fn) {
        commandEvents.addEventListener(event, function(e) {
            var data = JSON.parse(e.data);
            fn(data);
        });
    };

    var configuration = {};
    Minion.cfg = {};
    Minion.cfg.asString = function(key, fallback) {
        return configuration[key] || fallback;
    };
    Minion.cfg.asNumber = function(key, fallback) {
        var value = this.getString(key, fallback);
        return  value != null ? +value : null;
    };

    var states = {};
    Minion.state = function(qualifier) {
        return states[qualifier];
    };

    Minion.start = function(bootstrap) {
        configuration = bootstrap.configuration;
        states = bootstrap.states;
    };

    return Minion;
});
