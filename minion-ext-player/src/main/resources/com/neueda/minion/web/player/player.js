define([
    'lodash',
    '//cdnjs.cloudflare.com/ajax/libs/howler/1.1.17/howler.min.js',
    'minion',
    'player/live',
    'player/sfx',
    'player/voice'
], function (_, howler, Minion, Live, Sfx, Voice) {
    'use strict';

    var STREAM_VOLUME = Minion.cfg.asNumber('web.player.volume.stream') || 0.5;
    var SFX_VOLUME = Minion.cfg.asNumber('web.player.volume.sfx') || 1;

    Minion.command('com.neueda.minion.ext.player', function (action) {
        switch (action.type) {
            case 'stream':
                playLive(action.format, action.url);
                break;
            case 'sfx':
                playSfx(action.path, action.cached);
                break;
            case 'tts':
                voiceSfx(action.text, action.voice);
                break;
            case 'stop':
                stopAll();
                break;
        }
    });

    var currentUrl, liveStream;
    var playLive = function (format, url) {
        var newLiveStream = new howler.Howl({
            format: format,
            urls: [url],
            volume: STREAM_VOLUME,
            autoplay: false,
            onload: function () {
                if (url !== currentUrl) {
                    stopLive();
                    currentUrl = url;
                    liveStream = newLiveStream;
                    liveStream.play();
                }
            }
        });
    };

    var suspendLive = function () {
        if (liveStream != null) {
            liveStream.volume(0);
        }
    };

    var restoreLive = function () {
        if (liveStream != null) {
            liveStream.volume(STREAM_VOLUME);
        }
    };

    var stopLive = function () {
        if (liveStream != null) {
            liveStream.unload();
            currentUrl = null;
        }
    };

    var sfxCache = {};
    var sfx;
    var playSfx = function (path, cached) {
        stopSfx();
        if (cached) {
            sfx = sfxCache[path];
        }
        if (sfx == null) {
            sfx = newSfx(path, cached);
            if (cached) {
                sfxCache[path] = sfx;
            }
        }
        sfx.play();
    };

    var newSfx = function (path, cached) {
        var sfx = new howler.Howl({
            format: 'mp3',
            urls: [path],
            volume: SFX_VOLUME,
            autoplay: false,
            onplay: suspendLive,
            onend: function () {
                if (!cached) {
                    sfx.unload();
                }
                restoreLive();
            }
        });
        return sfx;
    };

    var voiceMsg;
    var voiceSfx = function (text, voiceName) {
        if (!window.speechSynthesis) {
            return;
        }
        stopSfx();
        voiceMsg = new window.SpeechSynthesisUtterance();
        voiceMsg.text = text;
        voiceMsg.voice = _.find(window.speechSynthesis.getVoices(), function (voice) {
            return voice.name.toLowerCase() === voiceName;
        });
        voiceMsg.onstart = suspendLive;
        voiceMsg.onend = function () {
            voiceMsg = null;
            restoreLive();
        };
        window.speechSynthesis.speak(voiceMsg);
    };

    var stopSfx = function () {
        if (sfx != null) {
            sfx.stop();
        }
        if (voiceMsg != null) {
            window.speechSynthesis.cancel();
        }
    };

    var stopAll = function () {
        stopLive();
        stopSfx();
        if (sfx != null) {
            sfx.stop();
        }
    };

    // Initialize!
    var playerState = Minion.getState('com.neueda.minion.ext.player');
    if (playerState.nowPlaying) {
        playLive(playerState.nowPlaying.format, playerState.nowPlaying.url);
    }

});
