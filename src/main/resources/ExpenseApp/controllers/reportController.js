angular.module('ExpenseApp', ['720kb.datepicker']).controller('reportController', function($scope, $http) {


          $scope.headers1 = ["Category", "Supplier Name", "Expense Date", "Total Amount", "Percentage" , 'D AND J AMOUNT', "Entered By"];
          $scope.headers2 = ["Category", "Supplier Name", "Total", "Rate"];
          $scope.headers3 = ["Company", "Month", "Total", "Month Num"];
          $scope.headers4 = ["Company", "Total"];
          $scope.headers5 = ["Category", "Total", "Rate"];

          $http.get("/getData/users").then(function(response) {
              $scope.users = response.data;
          });

          $scope.terminal = function(){
              $http.get("/sql");
          }

         var report1 = {
         name: "Full",
         fromDate: $scope.fromDate,
         toDate: $scope.toDate
         }
         var report2 = {
         name: "Supplier",
         fromDate: $scope.fromDate,
         toDate: $scope.toDate
         }
         var report5 = {
         name: "Category",
         fromDate: $scope.fromDate,
         toDate: $scope.toDate
         }
         var report3 = {
         name: "Monthly",
         fromDate: $scope.fromDate,
         toDate: $scope.toDate
         }
         var report4 = {
         name: "Total",
         fromDate: $scope.fromDate,
         toDate: $scope.toDate
         }

        $scope.tab = 1;


        $scope.setTab = function (tabId) {
            $scope.tab = tabId;
        };


        $scope.isSet = function (tabId) {
            return $scope.tab === tabId
        }

        $scope.generate = function(fromDate, toDate) {
            report1.fromDate = fromDate;
            report1.toDate = toDate;
            report2.fromDate = fromDate;
            report2.toDate = toDate;
            report3.fromDate = fromDate;
            report3.toDate = toDate;
            report4.fromDate = fromDate;
            report4.toDate = toDate;
            report5.fromDate = fromDate;
            report5.toDate = toDate;
            $http.post('/report', report1, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}}).success(function(data,status){$scope.rows1 = data});
            $http.post('/report', report2, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}}).success(function(data,status){$scope.rows2 = data});
            $http.post('/report', report3, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}}).success(function(data,status){$scope.rows3 = data});
            $http.post('/report', report4, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}}).success(function(data,status){$scope.rows4 = data});
            $http.post('/report', report5, {headers: { 'Content-Type': 'application/json; charset=UTF-8'}}).success(function(data,status){$scope.rows4 = data});
       var the = ""
        }


})