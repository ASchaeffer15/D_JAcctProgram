angular.module('ExpenseApp', []).controller('userController', function($scope, $http) {


          $scope.headers = ["FIRST_NAME","LAST_NAME","INITIALS"];

         $scope.getUsers = function(){
          $http.get("/getData/users").then(function(response) {
              $scope.users = response.data;
          });
         };

          $scope.terminal = function(){
              $http.get("/sql");
          }


         var table = {
         name: "USERS",
         headers: $scope.headers,
         data:[]
         }


        $scope.addUser = function (){
            table.data = [{
                row: [{
                    header: "FIRST_NAME",
                    value: $scope.firstName
                },{
                    header: "LAST_NAME",
                    value: $scope.lastName
                },{
                    header: "INITIALS",
                    value: $scope.initials
                }]
            }]
            $http.post('/addData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
            alert("User added");
            $scope.getUsers();
        };

        $scope.tab = 1;


        $scope.setTab = function (tabId) {
            $scope.tab = tabId;
        };


        $scope.isSet = function (tabId) {
            return $scope.tab === tabId
        }

        $scope.removeUser = function(row) {
            table.data = [$scope.users[row]]
            $http.post('/removeData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
            alert("User removed");
            $scope.getUsers();
        }


})