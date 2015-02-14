(function() {
    'use strict';

    angular
        .module('app.dashboard')
        .controller('Dashboard', Dashboard);

    function Dashboard(dataservice) {
    	var vm = this;
    	vm.ranks = [];

        activate();
        function activate() {
            var promises = [getHighestRanks()];
            return dataservice.ready(promises).then(function(result){
            	vm.ranks = result[0];
            });
        }

        // Get ranks through service
        function getHighestRanks() {
            return dataservice.getHighestRanks().then(function(data) {
                return data.ranks;
            });
        }
    }

})();
