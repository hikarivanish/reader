'use strict';

/* Controllers */

var readerControllers = angular.module('readerControllers', []);


readerControllers.controller('UserCtrl', ['$scope', '$http',
    function ($scope, $http) {
        var User = this;
        $http.get("/user").success(function (data) {
            User.channels = data;
        });
    }]);


readerControllers.controller('ItemListCtrl', ['$scope', '$routeParams',
    'ItemFac', function ($scope, $routeParams, ItemFac) {
        ItemFac.get({
            channelId: $routeParams.channelId
        }, function (data) {
            $scope.items = data['_embedded'].rssItems;
        });
    }]);

readerControllers.controller('DefaultCtrl', function ($scope) {

});
