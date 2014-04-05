/* global requirejs */
requirejs.config({
  baseUrl: '/',
  paths: {
    jquery: '//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.0/jquery.min',
    underscore: '//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.6.0/underscore-min'
  }
});
requirejs(['bootstrap']);
