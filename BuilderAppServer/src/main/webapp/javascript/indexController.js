myApp.controller('indexController', ["$scope", "$window", "$http", 'sharedProperties', function($scope, $window, $http, sharedProperties) {
                $http.get("api/build?budget=1000&computerType=GAMING", config).then(function(response) {
                    $window.alert(response.data);
                });

            }
