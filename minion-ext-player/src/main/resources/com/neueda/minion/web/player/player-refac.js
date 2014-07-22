define([
    'lodash',
    '//cdnjs.cloudflare.com/ajax/libs/howler/1.1.17/howler.min.js',
    'minion',
    'player/stream',
    'player/sfx',
    'player/tts'
], function(_, howler, Minion, Stream, Sfx, Tts) {
    'use strict';

    var STREAM_VOLUME = Minion.cfg.asNumber('web.player.volume.stream') || 0.5;
    var SFX_VOLUME = Minion.cfg.asNumber('web.player.volume.sfx') || 1;

    var PLAY_TYPES = {
        stream: Stream,
        sfx: Sfx,
        tts: Tts
    };

    var Player = {};

    var nowPlaying = [];
    Player.play = function(track) {
        nowPlaying.stop();
        if (track.isStream) {
            
        }
    };

    Player.stop = function() {
        nowPlaying.forEach(function(track) {
            track.stop();
        });
    };

    Minion.command('com.neueda.minion.ext.player', function(opts) {
        if (opts.type === 'stop') {
            Player.stop();
        } else if (PLAY_TYPES[opts.type] != null) {
            Player.play(new PLAY_TYPES[opts.type](opts));
        }
    });

});
