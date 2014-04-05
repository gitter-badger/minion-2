/* global define */
define([
  'minion',
  '//cdnjs.cloudflare.com/ajax/libs/howler/1.1.17/howler.min.js'
], function(Minion, howler) {
  'use strict';

  Minion.command('player', function(url) {
    new howler.Howl({
      urls: [url]
    }).play();
  });
});
