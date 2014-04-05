/* global define */
define(['jquery', 'underscore'], function($, _) {
  'use strict';

  console.info('Loading extensions');
  $.getJSON('/bootstrap', function(manifest) {
    require(manifest.extensions);
  });
});
