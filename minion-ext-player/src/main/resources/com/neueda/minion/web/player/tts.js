define([
    'lodash'
], function(_) {
    'use strict';

    var Tts = function(opts) {
        this.opts = opts;
        this.id = opts.text;
        this.isStream = false;
        this.msg = null;
    };

    Tts.prototype.load = function(cb) {
        this.msg = new window.SpeechSynthesisUtterance();
        this.msg.text = this.opts.text;
        this.msg.voice = _.find(window.speechSynthesis.getVoices(), function(voice) {
            return voice.name.toLowerCase() === this.opts.voice;
        }.bind(this));
        cb();
    };

    Tts.prototype.play = function(cb) {
        if (this.msg == null) {
            return;
        }
        this.msg.onend = cb;
        window.speechSynthesis.speak(this.msg);
    };

    Tts.prototype.stop = function() {
        window.speechSynthesis.cancel();
    };

    Tts.prototype.mute = function() {
    };

    Tts.prototype.unmute = function() {
    };

    return Tts;

});
