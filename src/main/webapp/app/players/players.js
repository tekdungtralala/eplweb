(function() {
    'use strict';

    angular
        .module('app.players')
        .controller('Players', Players);

    function Players(dataservice, adminauth) {
        var vm = this;

        activate();
        function activate() {
        	adminauth.adminMustLogedIn();
        }
    }
})();
