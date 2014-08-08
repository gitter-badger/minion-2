define(function() {
    'use strict';

    var Silence = function(isStream) {
        this.id = 'silence';
        this.isStream = isStream;
    };

    Silence.prototype.load = function(cb) {
        cb();
    };

    Silence.prototype.play = function(cb) {
        if (!this.isStream) {
            cb();
        }
    };

    Silence.prototype.stop = function() {
    };

    Silence.prototype.mute = function() {
    };

    Silence.prototype.unmute = function() {
    };

    return Silence;

});
