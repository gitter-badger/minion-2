define([
    'lodash',
    'minion',
    'player/stream',
    'player/sfx',
    'player/tts',
    'player/silence'
], function(_, Minion, Stream, Sfx, Tts, Silence) {
    'use strict';

    var PLAY_TYPES = {
        stream: Stream,
        sfx: Sfx,
        tts: Tts
    };

    var Player = {
        track: new Silence(true),
        interrupt: new Silence(false)
    };

    Player.play = function(sound) {
        if (sound.isStream) {
            if (this.track.id === sound.id) {
                return;
            }
            sound.load(function() {
                this.track.stop();
                this.track = sound;
                this.track.play();
            }.bind(this));
        } else {
            sound.load(function() {
                this.track.mute();
                this.interrupt.stop();
                this.interrupt = sound;
                this.interrupt.play(function() {
                    this.track.unmute();
                }.bind(this));
            }.bind(this));
        }
    };

    Player.stop = function() {
        this.stopTrack();
        this.stopInterrupt();
    };

    Player.stopTrack = function() {
        this.track.stop();
        this.track = new Silence(true);
    };

    Player.stopInterrupt = function() {
        this.interrupt.stop();
        this.interrupt = new Silence(false);
    };

    Minion.command('com.neueda.minion.ext.player', function(opts) {
        if (opts.type === 'stop') {
            Player.stop();
        } else if (PLAY_TYPES[opts.type] != null) {
            Player.play(new PLAY_TYPES[opts.type](opts));
        }
    });

    var playerState = Minion.state('com.neueda.minion.ext.player');
    if (playerState.nowPlaying != null) {
        Player.play(new Stream(playerState.nowPlaying));
    }

});
