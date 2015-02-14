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
            var promises = [getHighestRanks(), getCurrentMatchday()];
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
        function getCurrentMatchday() {
            return dataservice.getCurrentMatchday().then(function(data) {
                return data.model;
            });
        }
    }

})();
