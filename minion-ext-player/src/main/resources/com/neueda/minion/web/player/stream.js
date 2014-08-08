define([
    'minion',
    '//cdnjs.cloudflare.com/ajax/libs/howler/1.1.17/howler.min.js',
], function(Minion, howler) {
    'use strict';

    var STREAM_VOLUME = Minion.cfg.asNumber('web.player.volume.stream') || 0.5;

    var Stream = function(opts) {
        this.opts = opts;
        this.id = opts.url;
        this.isStream = true;
        this.stream = null;
    };

    Stream.prototype.load = function(cb) {
        this.stream = new howler.Howl({
            format: this.opts.format,
            urls: [this.opts.url],
            volume: STREAM_VOLUME,
            autoplay: false,
            onload: cb.bind(null)
        });
    };

    Stream.prototype.play = function() {
        if (this.stream != null) {
            this.stream.play();
        }
    };

    Stream.prototype.stop = function() {
        if (this.stream != null) {
            this.stream.unload();
        }
    };

    Stream.prototype.mute = function() {
        if (this.stream != null) {
            this.stream.volume(0);
        }
    };

    Stream.prototype.unmute = function() {
        if (this.stream != null) {
            this.stream.volume(STREAM_VOLUME);
        }
    };

    return Stream;

});
