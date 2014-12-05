'use strict';

/* Services */

var readerServices = angular.module('readerServices', [ 'ngResource' ]);
/*

readerServices.factory('UserFac', [ '$resource', function($resource) {
	return $resource('user', {}, {
		query : {
			method : 'GET',
			params : {},
			isArray : false
		}
	});
} ]);

*/

readerServices.factory('ItemFac', [ '$resource', function($resource) {
	return $resource('/rssChannel/:channelId/items?sort=publishedDate,desc&size=40&page=:page',
			{}, {
	});
} ]);

