/* global define */
define(['jquery', 'underscore'], function($, _) {
  'use strict';

  var Minion = {};

  var commandEvents = new window.EventSource('/commands');
  Minion.command = function(event, fn) {
    commandEvents.addEventListener(event, function(e) {
      var data = JSON.parse(e.data);
      fn(data);
    });
  };

  return Minion;
});
