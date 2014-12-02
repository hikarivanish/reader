'use strict';

/* App Module */

var readerApp = angular.module('readerApp',
		[ 'ngRoute', 'ngSanitize', 'ngAnimate', 'readerControllers',
				'readerServices', /* 'readerAnimations','readerFilters' */]);

readerApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/channel/:channelId', {
		templateUrl : '/rd/partials/itemlist.html',
		controller : 'ItemListCtrl'
	}).otherwise({
		templateUrl : '/partials/default.html',
		controller : 'DefaultCtrl'
	});
} ]);
