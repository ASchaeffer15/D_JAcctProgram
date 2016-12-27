angular.module('ExpenseApp', []).controller('supplierController', function($scope,$http) {

        $scope.headers = ["NAME","PAYMENT_TYPE","CATEGORY","RECURRING","MULTIPLIER_AMT"]


        $scope.getSupplier = function(){
        $http.get("/getData/supplier").then(function(response) {
            $scope.suppliers = response.data;
        });
        };

        $scope.supplierCategories = []

        $http.get("/getData/category").then(function(response) {

             angular.forEach( response.data, function(value,key){
             $scope.supplierCategories.push(value.row[0].value)
             })

             $scope.supplierCategories
        });

         $scope.paymentTypes = []

         $http.get("/getData/payments").then(function(response) {

              angular.forEach( response.data, function(value,key){
              $scope.paymentTypes.push(value.row[0].value)
              })

              $scope.supplierCategories
         });


        $scope.tab = 1;


        $scope.setTab = function (tabId) {
            $scope.tab = tabId;
        };


        $scope.isSet = function (tabId) {
            return $scope.tab === tabId
        }


         var table = {
         name: "SUPPLIER",
         headers: $scope.headers,
         data:[]
         }

        $scope.addSupplier = function (){
        table.data = [{
            row: [{
                header: "NAME",
                value: $scope.name
            },{
               header: "PAYMENT_TYPE",
               value: $scope.paymentType
           },{
               header: "CATEGORY",
               value: $scope.category
           },{
               header: "RECURRING",
               value: $scope.recurring
           },{
               header: "MULTIPLIER_AMT",
               value: $scope.multiplierAmt
           }]
        }]
        $http.post('/addData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
        alert("Supplier added");
        $scope.getSupplier();
        }

        $scope.removeSupplier = function(row) {
            table.data = [$scope.suppliers[row]]
            $http.post('/removeData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
            alert("Supplier Removed");
            $scope.getSupplier();
        }

})