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

  var states = {};
  Minion.setStates = _.once(function(map) {
    states = map;
  });

  Minion.getState = function(qualifier) {
    return states[qualifier];
  };

  return Minion;
});
