define([
    'minion',
    '//cdnjs.cloudflare.com/ajax/libs/howler/1.1.17/howler.min.js',
], function(Minion, howler) {
    'use strict';

    var SFX_VOLUME = Minion.cfg.asNumber('web.player.volume.sfx') || 1;

    var Sfx = function(opts) {
        this.opts = opts;
        this.id = opts.path;
        this.isStream = false;
        this.sfx = null;
    };

    var cache = {};
    Sfx.prototype.load = function(cb) {
        if (this.opts.cached) {
            this.sfx = cache[this.opts.path];
            cb();
        }
        if (this.sfx == null) {
            this.sfx = new howler.Howl({
                format: 'mp3',
                urls: [this.opts.path],
                volume: SFX_VOLUME,
                autoplay: false,
                onload: function() {
                    if (this.opts.cached) {
                        cache[this.opts.path] = this.sfx;
                    }
                    cb();
                }.bind(this)
            });
        }
    };

    Sfx.prototype.play = function(cb) {
        if (this.sfx == null) {
            return;
        }
        this.sfx.on('end', function() {
            if (!this.opts.cached) {
                this.sfx.unload();
            }
            cb();
        }.bind(this));
        this.sfx.play();
    };

    Sfx.prototype.stop = function() {
        if (this.sfx == null) {
            return;
        }
        this.sfx.stop();
        if (!this.opts.cached) {
            this.sfx.unload();
        }
    };

    Sfx.prototype.mute = function() {
        if (this.sfx != null) {
            this.sfx.volume(0);
        }
    };

    Sfx.prototype.unmute = function() {
        if (this.sfx != null) {
            this.sfx.volume(SFX_VOLUME);
        }
    };

    return Sfx;

});
