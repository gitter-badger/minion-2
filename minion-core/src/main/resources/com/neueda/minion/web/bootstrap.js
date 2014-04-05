/* global define */
define([
  'jquery'
], function($) {
  'use strict';

  console.info('Loading extensions');
  $.getJSON('/bootstrap', function(manifest) {
    require(manifest.extensions);
  });


  var body = document.body;

  body.requestFullscreen =
    body.requestFullscreen ||
    body.mozRequestFullscreen ||
    body.mozRequestFullScreen ||
    body.webkitRequestFullscreen;

  body.requestPointerLock =
    body.requestPointerLock ||
    body.mozRequestPointerLock ||
    body.webkitRequestPointerLock;

  var onFullscreenChange = function() {
    body.requestPointerLock();
  };

  document.addEventListener('fullscreenchange', onFullscreenChange);
  document.addEventListener('mozfullscreenchange', onFullscreenChange);
  document.addEventListener('webkitfullscreenchange', onFullscreenChange);

  $(window).keypress(function(e) {
    if (e.altKey && e.which === 13) {
      if (body.webkitRequestPointerLock) {
        body.requestPointerLock();
      } else {
        body.requestFullscreen();
      }
    }
  });
});
