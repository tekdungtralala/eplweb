(function() {
    'use strict';

    angular
        .module('app.admin.dashboard')
        .controller('AdminDashboard', AdminDashboard);

    function AdminDashboard(dataservice, adminutil) {
    	var vm = this;

        activate();
        function activate() {
        }
    }
})();
