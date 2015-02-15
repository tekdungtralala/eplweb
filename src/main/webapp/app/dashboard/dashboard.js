(function() {
    'use strict';

    angular
        .module('app.dashboard')
        .controller('Dashboard', Dashboard);

    function Dashboard(dataservice) {
    	var vm = this;
    	vm.ranks = [];
        vm.model = [];

        activate();
        function activate() {
            var promises = [getHighestRanks(), getMatchdayByWeekNmr()];
            return dataservice.ready(promises).then(function(result){
            	vm.ranks = result[0];
                vm.model = result[1];
            });
        }

        // Get ranks through service
        function getHighestRanks() {
            return dataservice.getHighestRanks().then(function(data) {
                return data.ranks;
            });
        }

        // Get matchday through service
        function getMatchdayByWeekNmr() {
            return dataservice.getMatchdayByWeekNmr().then(function(data) {
                return data.model;
            });
        }
    }

})();
