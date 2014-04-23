/* global define */
define([
  'minion',
  'underscore',
  '//cdnjs.cloudflare.com/ajax/libs/howler/1.1.17/howler.min.js'
], function(Minion, _, howler) {
  'use strict';

  Minion.command('com.neueda.minion.ext.player', function(action) {
    switch (action.type) {
      case 'stream':
        playLive(action.format, action.url);
        break;
      case 'sfx':
        playSfx(action.path, action.cached);
        break;
      case 'tts':
        speak(action.text, action.voice);
        break;
      case 'stop':
        stop();
        break;
    }
  });

  var currentUrl, liveStream;
  var liveVolume = 0.5;
  var playLive = function(format, url) {
    var newLiveStream = new howler.Howl({
      format: format,
      urls: [url],
      volume: liveVolume, // TODO Should be configurable
      autoplay: false,
      onload: function() {
        if (url !== currentUrl) {
          if (liveStream != null) {
            liveStream.unload();
          }
          currentUrl = url;
          liveStream = newLiveStream;
          liveStream.play();
        }
      }
    });
  };

  var suspendLive = function() {
    if (liveStream != null) {
      liveStream.volume(0);
    }
  };

  var restoreLive = function() {
    if (liveStream != null) {
      liveStream.volume(liveVolume);
    }
  };

  var sfxCache = {};
  var sfx;
  var playSfx = function(path, cached) {
    if (sfx != null) {
      sfx.stop();
    }
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

  var newSfx = function(path, cached) {
    var sfx = new howler.Howl({
      format: 'mp3',
      urls: [path],
      autoplay: false,
      onplay: suspendLive,
      onend: function() {
        if (!cached) {
          sfx.unload();
        }
        restoreLive();
      }
    });
    return sfx;
  };

  var speak = function(text, voiceName) {
    var msg = new window.SpeechSynthesisUtterance();
    msg.text = text;
    msg.voice = _.find(window.speechSynthesis.getVoices(), function(voice) {
      return voice.name.toLowerCase() === voiceName;
    });
    msg.onstart = suspendLive;
    msg.onend = restoreLive;
    window.speechSynthesis.speak(msg);
  };

  var stop = function() {
    if (liveStream != null) {
      liveStream.unload();
      currentUrl = null;
    }
    if (sfx != null) {
      sfx.stop();
    }
  };

});
