angular.module('ExpenseApp', ['720kb.datepicker']).controller('expenseController', function($scope, $http) {

        $scope.headers = ["DATE","SUPPLIER_NAME","SUPPLIER_CATEGORY","EXPENSE_AMT","PERCENTAGE_AMT","USER"]

        $scope.getExpenses = function(){

        $http.get("/getData/expenses").then(function(response) {
            $scope.expenses = response.data;
        });
        };

        $scope.users = []

        $http.get("/getData/users").then(function(response) {

             angular.forEach( response.data, function(value,key){
             $scope.users.push(value.row[0].value)
             })
        });

        $scope.supplierNames = []
        $scope.suppliers = []

        $http.get("/getData/supplier").then(function(response) {

             angular.forEach( response.data, function(value,key){

             var name = "";
             var category= "";
             var percentage = "";

             angular.forEach( value.row, function(value2,key2){
                if( value2.header === "NAME") {
                    name = value2.value
                }
                if( value2.header === "CATEGORY") {
                    category = value2.value
                }
                if( value2.header === "MULTIPLIER_AMT") {
                    percentage = value2.value
                }

             })
             $scope.suppliers.push({name: name, category: category, percentage: percentage})
             $scope.supplierNames.push(name)
             })
        });

        $scope.$watch('supplierName', function(newVal, oldVal){
        angular.forEach( $scope.suppliers, function(value,key){
            if(value.name == newVal)
            $scope.category = value.category
            $scope.percent = value.percentage
        });
        });

        $scope.tab = 1;


        $scope.setTab = function (tabId) {
            $scope.tab = tabId;
        };


        $scope.isSet = function (tabId) {
            return $scope.tab === tabId
        }


         var table = {
         name: "EXPENSES",
         headers: $scope.headers,
         data:[]
         }

        $scope.addExpense = function (){
        table.data = [{
            row: [{
                header: "DATE",
                value: $scope.date
            },{
                header: "SUPPLIER_NAME",
                value: $scope.supplierName
            },{
                header: "SUPPLIER_CATEGORY",
                value: $scope.category
            },{
                header: "EXPENSE_AMT",
                value: $scope.amount
            },{
                header: "PERCENTAGE_AMT",
                value: $scope.percent
            },{
                header: "USER",
                value: $scope.user
            }]
        }]
        $http.post('/addData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
        alert("Expense added");
        $scope.getExpenses();
        }

        $scope.removeExpenses = function(row) {
            table.data = [$scope.expenses[row]]
            $http.post('/removeData', table, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}});
            alert("Expense removed");
            $scope.getExpenses();
        }

})