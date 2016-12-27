angular.module('ExpenseApp', []).controller('paymentController', function($scope, $http) {

          $scope.headers = ["PAYMENT_TYPE"];


        $scope.getPaymentType = function(){
        $http.get("/getData/payments").then(function(response) {
            $scope.payments = response.data;
        });
        }

        $scope.tab = 1;


        $scope.setTab = function (tabId) {
            $scope.tab = tabId;
        };


        $scope.isSet = function (tabId) {
            return $scope.tab === tabId
        }


         var table = {
         name: "PAYMENTS",
         headers: $scope.headers,
         data:[]
         }

        $scope.addPaymentType = function (){
        table.data = [{
            row: [{
                header: "PAYMENT_TYPE",
                value: $scope.paymentType
            }]
        }]
        $http.post('/addData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
        alert("Payment Type added");
        $scope.getPaymentType();
        }


        $scope.removePaymentType = function(row) {
            table.data = [$scope.payments[row]]
            $http.post('/removeData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
            alert("Payment Type removed");
            $scope.getPaymentType();
        }


})