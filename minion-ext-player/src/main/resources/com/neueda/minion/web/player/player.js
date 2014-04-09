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
        playSfx(action.path);
        break;
    }
  });

  var currentUrl, liveStream;
  var playLive = function(format, url) {
    var newLiveStream = new howler.Howl({
      format: format,
      urls: [url],
      volume: 0.5, // TODO Should be configurable
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

  var sfxMap = {};
  var sfx;
  var playSfx = function(path) {
    if (sfx != null) {
      sfx.stop();
    }
    sfx = sfxMap[path];
    if (sfx == null) {
      sfx = new howler.Howl({
        format: 'mp3',
        urls: [path],
        onplay: function() {
          if (liveStream != null) {
            liveStream.volume(0);
          }
        },
        onend: function() {
          if (liveStream != null) {
            liveStream.volume(1);
          }
        }
      });
      sfxMap[path] = sfx;
    }
    sfx.play();
  };
});
