var app = angular.module('sloth', ['angularMoment'], function($interpolateProvider) {
	$interpolateProvider.startSymbol('[[');
	$interpolateProvider.endSymbol(']]');
});

app.filter('slice', function() {
	return function(arr, start, end) {
		return arr.slice(start, end);
	};
});

app.filter('capitalize', function() {
  return function(token) {
      return token.charAt(0).toUpperCase() + token.slice(1);
   }
});

app.controller('demo', function($scope, $http, $location) {
	var remote = "http://api.ghorbanzade.com";
	$scope.loading = true;
	$scope.url = $location.absUrl();
	$scope.api = remote;
	$http.get($scope.api).success(function(response) {
		$scope.data = angular.fromJson(response);
	}).finally(function() {
		$scope.loading = false;
	});
	$scope.formatDate = function(date) {
		return new Date(date.split("-").join("/"));
	};
  $scope.isRecent = function(date) {
    var diff = 1 * 60 * 60 * 1000;
    var eventTime = new Date(date.split("-").join("/"));
    var thresholdTime = new Date();
    thresholdTime.setTime(thresholdTime.getTime() - diff);
    return (eventTime > thresholdTime) ? true : false;
  }
});
