/* global define */
define([
  'minion',
  '//cdnjs.cloudflare.com/ajax/libs/howler/1.1.17/howler.min.js'
], function(Minion, howler) {
  'use strict';

  Minion.command('com.neueda.minion.ext.player', function(action) {
    switch (action.type) {
      case 'stream':
        playLive(action.format, action.url);
        break;
      case 'sfx':
        playSfx(action.path, action.cached);
        break;
      case 'stop':
        stop();
        break;
    }
  });

  var currentUrl, liveStream;
  var playLive = function(format, url) {
    var newLiveStream = new howler.Howl({
      format: format,
      urls: [url],
      volume: 0.5, // TODO Should be configurable
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
      onplay: function() {
        if (liveStream != null) {
          liveStream.volume(0);
        }
      },
      onend: function() {
        if (!cached) {
          sfx.unload();
        }
        if (liveStream != null) {
          liveStream.volume(1);
        }
      }
    });
    return sfx;
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
