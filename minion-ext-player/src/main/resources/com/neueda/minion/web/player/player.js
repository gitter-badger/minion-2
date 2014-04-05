/* global define */
define([
  'minion',
  '//cdnjs.cloudflare.com/ajax/libs/howler/1.1.17/howler.min.js'
], function(Minion, howler) {
  'use strict';

  Minion.command('player', function(action) {
    switch (action.type) {
      case 'sfx':
        playSfx(action.path);
        break;
    }
  });

  var sfxMap = {};
  var sfx;
  var playSfx = function(path) {
    if (sfx != null) {
      sfx.stop();
    }
    sfx = sfxMap[path];
    if (sfx == null) {
      sfx = new howler.Howl({
        urls: [path]
      });
      sfxMap[path] = sfx;
    }
    sfx.play();
  };
});
