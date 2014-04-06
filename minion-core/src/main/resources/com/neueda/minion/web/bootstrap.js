/* global define, google */
define([
  'jquery',
  'underscore',
], function($, _) {
  'use strict';

  console.info('Loading extensions');
  $.getJSON('/bootstrap', function(manifest) {
    require(manifest.extensions);
  });


  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(pos) {
      var geocoder = new google.maps.Geocoder();
      var latLng = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
      geocoder.geocode({'latLng': latLng}, function(results, status) {
        if (status === google.maps.GeocoderStatus.OK) {
          findLocality(results[0]);
        }
      });
    });
  }

  var loc;
  var findLocality = function(geoResult) {
    var locality = _.find(geoResult.address_components, function(component) {
      return _.contains(component.types, 'locality');
    });
    if (locality != null) {
      loc = locality.long_name;
    }
  };

  var splash = $('#splash');
  window.setInterval(function() {
    var date = new Date();
    var h = leadingZero(date.getHours());
    var m = leadingZero(date.getMinutes());
    var html = h + ':' + m;
    if (loc) {
      html += '\n' + loc;
    }
    splash.html(html);
  }, 1000);

  var leadingZero = function(n) {
    return n < 10 ? '0' + n : n;
  };


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
