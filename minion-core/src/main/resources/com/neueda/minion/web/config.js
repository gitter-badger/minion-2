/* global requirejs */
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: '//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.0/jquery.min',
        lodash: '//cdnjs.cloudflare.com/ajax/libs/lodash.js/2.4.1/lodash.min'
    }
});
requirejs(['bootstrap']);
