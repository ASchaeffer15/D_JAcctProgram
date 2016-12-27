angular.module('ExpenseApp', []).controller('supplierCategoriesController', function($scope,$http) {

        $scope.headers = ["SUPPLIER_CATEGORY"]

        $scope.getCategory = function(){
        $http.get("/getData/category").then(function(response) {
            $scope.supplierCategories = response.data;
        });
        };

        $scope.tab = 1;


        $scope.setTab = function (tabId) {
            $scope.tab = tabId;
        };


        $scope.isSet = function (tabId) {
            return $scope.tab === tabId
        }

         var table = {
         name: "CATEGORY",
         headers: $scope.headers,
         data:[]
         }

        $scope.addCategory = function (){
        table.data = [{
            row: [{
                header: "SUPPLIER_CATEGORY",
                value: $scope.supplierCategory
            }]
        }]
        $http.post('/addData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
        alert("Category added");
        $scope.getCategory();
        }


         $scope.removeCategory = function(row) {
             table.data = [$scope.supplierCategories[row]]
             $http.post('/removeData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
             alert("Category added");
             $scope.getCategory();
         }

})