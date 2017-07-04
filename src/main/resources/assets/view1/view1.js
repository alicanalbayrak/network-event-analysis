'use strict';

var view1 = angular.module('myApp.view1', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {
    templateUrl: 'view1/view1.html',
    controller: 'View1Ctrl'
  });
}])

.controller('View1Ctrl', [function() {

}]);



myApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

myApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadCode ,uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('uploadCode', uploadCode);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(){

        })
        .error(function(){

        });
    }
}]);

myApp.controller('myCtrl', ['$scope', 'fileUpload', function($scope, fileUpload){
    
    $scope.uploadFile = function(){
        var file = $scope.myFile;
        console.log('file is ' );
        console.dir(file);

        var uploadCode = $scope.uploadCode;
        console.log('uploadCode is ' + uploadCode);

        var uploadUrl = "/service/file/upload";
        fileUpload.uploadFileToUrl(file, uploadCode, uploadUrl);
    };
    
}]);

myApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadCode ,uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('uploadCode', uploadCode);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function(){

            })
            .error(function(){

            });
    }
}]);


myApp.factory('connectionFactory', ConnectionFactory);
myApp.service('connectionService', ConnectionService);
myApp.controller('connectionController', ConnectionController);