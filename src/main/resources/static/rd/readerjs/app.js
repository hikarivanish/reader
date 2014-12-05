'use strict';

/* App Module */

var readerApp = angular.module('readerApp',
		[ 'ngRoute', 'ngSanitize', 'ngAnimate', 'readerControllers',
				'readerServices', /* 'readerAnimations','readerFilters' */]);

readerApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/channel/:channelId/:page', {
		templateUrl : '/rd/partials/itemlist.html',
		controller : 'ItemListCtrl'
	}).when('/channel/:channelId', {
		redirectTo:'/channel/:channelId/0'
	}).otherwise({
		templateUrl : '/partials/default.html',
		controller : 'DefaultCtrl'
	});
} ]);
