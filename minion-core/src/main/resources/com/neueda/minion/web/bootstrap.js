define([
    'jquery',
    'lodash',
    'minion',
    'geo',
    'clock'
], function($, _, Minion, Geo, Clock) {
    'use strict';

    console.info('Loading extensions');
    $.getJSON('/bootstrap', function(manifest) {
        Minion.start(manifest);
        require(manifest.resources);
    });

    var clock = new Clock('#splash');
    clock.start();

    var geo = new Geo();
    geo.geolocate(function(err, pos) {
        if (err) {
            return;
        }
        geo.geocode(pos, function(err, loc) {
            if (err) {
                return;
            }
            var locality = geo.findLocality(loc);
            clock.setSuffix(locality);
        });
    });

});
