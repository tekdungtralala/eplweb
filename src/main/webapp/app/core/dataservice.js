(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('dataservice', Dataservice);

    function Dataservice($http, $q, $rootScope) {
        var isPrimed = false;
        var primePromise;

        var service = {
            // admin login
            adminLogin: adminLogin,
            adminCekLogin: adminCekLogin,
            // First load
            getInitData: getInitData,
            // Page /matchday
            // Page / {dashboard}
            getMatchdayByWeekNmr: getMatchdayByWeekNmr,
            // Page /ranks
            getTeamStat: getTeamStat,
            getRanksByWeekNmr: getRanksByWeekNmr,
            getAllPassedWeek: getAllPassedWeek,
            ready: ready
        };

        return service;

        function adminCekLogin(session) {
            $rootScope.promise = $http.get('api/admin/login/' + session)
                .then(process)
                .catch(process);
            return $rootScope.promise;

            function process(result) {
                if (200 === result.status){
                    $rootScope.showAdminMenu = true; 
                } else {
                    $rootScope.showAdminMenu = false; 
                }
            }
        }


        function adminLogin(email, psswd) {
            var data = {
                adminEmailEncode: email,
                adminPaswdEncode: psswd
            };

            var req = {
                method: 'POST',
                url: 'api/admin/login',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                data: $.param(data),
            }

            $rootScope.promise = $http(req)
                    .then(getData)
                    .catch(getData);
            return $rootScope.promise;

            function getData(result) {
                return result;
            }
        }

        function getInitData(page) {
            $rootScope.promise = $http.get('api/page/' + page)
                    .then(getData)
                    .catch(function(message) {
                        if (message.status == 404) window.location.href = "404.jsp";
                    });
            return $rootScope.promise;

            function getData(result) {
                return result.data;
            }
        }

        // Page /
        function getMatchdayByWeekNmr(weekNumber) {
            var query = '';
            if (weekNumber) 
                query = '/' + weekNumber;
            $rootScope.promise = $http.get('api/matchday' + query)
                .then(getData)
                .catch(function(message) {
                });
            return $rootScope.promise;

            function getData(result) {
                return result.data;
            }
        }

        // Page /ranks
        function getTeamStat(weekNumber, teamId) {
            $rootScope.promise = $http.get('api/chart/week/' + weekNumber + '/team/' + teamId)
                .then(getData)
                .catch(function(message) {
                });
            return $rootScope.promise;

            function getData(result) {
                return result.data;
            }
        }
        function getRanksByWeekNmr(weekNumber) {
            var query = '';
            if (weekNumber) 
                query = '/' + weekNumber;
            $rootScope.promise = $http.get('api/ranks' + query)
                .then(getData)
                .catch(function(message) {
                });
            return $rootScope.promise;

            function getData(result) {
                return result.data;
            }
        }

        function getAllPassedWeek() {
            $rootScope.promise = $http.get('api/passedWeeks')
                .then(getData)
                .catch(function(message) {
                });
            return $rootScope.promise;

            function getData(result) {
                return result.data;
            }
        }


        // Default
        function ready(nextPromises) {
            var readyPromise = primePromise || prime();

            return readyPromise
                .then(function() { return $q.all(nextPromises); })
                .catch();
        }

        function prime() {
            // This function can only be called once.
            if (primePromise) {
                return primePromise;
            }

            primePromise = $q.when(true).then(success);
            return primePromise;

            function success() {
                isPrimed = true;
            }
        }
    }
})();
