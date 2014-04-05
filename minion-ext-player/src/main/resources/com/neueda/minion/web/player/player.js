/* global define */
define(['minion', 'jquery'], function(Minion, $) {
  'use strict';

  Minion.command('player', function(data) {
    console.log('Player got: ' + data);
  });
});
