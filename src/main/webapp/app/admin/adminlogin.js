(function() {
    'use strict';

    angular
        .module('app.adminlogin')
        .controller('AdminLogin', AdminLogin);

    function AdminLogin(dataservice, datautil) {
    	var vm = this;

        activate();
        function activate() {

            // return getInitData().then(function(result){
            // });
        }

    }
})();
