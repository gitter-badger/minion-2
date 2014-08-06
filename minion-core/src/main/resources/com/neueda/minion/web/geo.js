/* global google */
define(['lodash'], function(_) {
    'use strict';

    var Geo = function() {
    };

    Geo.prototype.geolocate = function(cb) {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(pos) {
                cb(null, pos);
            }, function(err) {
                cb(err);
            });
        }
    };

    Geo.prototype.geocode = function(pos, cb) {
        var geocoder = new google.maps.Geocoder();
        var latLng = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
        geocoder.geocode({'latLng': latLng}, function(results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                cb(null, results[0]);
            } else {
                cb(status);
            }
        });
    };

    Geo.prototype.findLocality = function(loc) {
        /* jshint camelcase: false */
        var locality = _.find(loc.address_components, function(component) {
            return _.contains(component.types, 'locality');
        });
        return locality != null ? locality.long_name : null;
    };

    return Geo;
});
