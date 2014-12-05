'use strict';

/* Controllers */

var readerControllers = angular.module('readerControllers', []);


readerControllers.controller('UserCtrl', ['$scope', '$http','$routeParams',
    function ($scope, $http,$routeParams) {
        var User = this;
        $http.get("/user/channels").success(function (data) {
            User.channels = data;
            User.channelId = $routeParams.channelId;
        });
    }]);


readerControllers.controller('ItemListCtrl', ['$scope', '$routeParams',
    'ItemFac', function ($scope, $routeParams, ItemFac) {
        ItemFac.get({
            channelId: $routeParams.channelId,
            page:$routeParams.page
        }, function (data) {
            $scope.itemPage = data;
            $scope.channelId= $routeParams.channelId;
        });
    }]);

readerControllers.controller('DefaultCtrl', function ($scope) {

});
