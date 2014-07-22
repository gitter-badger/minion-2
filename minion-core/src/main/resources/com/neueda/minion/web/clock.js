define(['jquery'], function($) {
    'use strict';

    var Clock = function(el) {
        this.el = $(el);
        this.suffix = null;
        this.tick = null;
    };

    Clock.prototype.setSuffix = function(suffix) {
        this.suffix = suffix;
    };

    Clock.prototype.start = function() {
        this.tick = window.setInterval(function() {
            var date = new Date();
            var h = leadingZero(date.getHours());
            var m = leadingZero(date.getMinutes());
            var text = h + ':' + m;
            if (this.suffix) {
                text += '\n' + this.suffix;
            }
            this.el.text(text);
        }.bind(this));
    };

    Clock.prototype.stop = function() {
        window.clearInterval(this.tick);
        this.tick = null;
    };

    function leadingZero(n) {
        return n < 10 ? '0' + n : n;
    }

    return Clock;
});
