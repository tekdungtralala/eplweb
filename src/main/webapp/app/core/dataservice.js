(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('dataservice', Dataservice);

    function Dataservice($http, $q) {
        var isPrimed = false;
        var primePromise;

        var service = {
            // Page /matchday
            getAllWeek: getAllWeek, 
            // Page / {dashboard}
            getFiveBigTeamData: getFiveBigTeamData,
            getHighestRanks: getHighestRanks,
            getMatchdayByWeekNmr: getMatchdayByWeekNmr,
            // Page /ranks
            getRanksByWeekNmr: getRanksByWeekNmr,
            getAllPassedWeek: getAllPassedWeek,
            ready: ready
        };

        return service;

        function getAllWeek() {
            return $http.get('api/weeks')
                .then(getData)
                .catch(function(message) {
                });

            function getData(result) {
                return result.data;
            }
        }

        // Page /
        function getFiveBigTeamData() {
            return $http.get('api/chart/fiveBigestTeam')
                .then(getData)
                .catch(function(message) {
                });

            function getData(result) {
                return result.data;
            }
        }

        function getHighestRanks() {
            return $http.get('api/highestRanks')
                .then(getData)
                .catch(function(message) {
                });

            function getData(result) {
                return result.data;
            }
        }

        function getMatchdayByWeekNmr(weekNumber) {
            var query = '';
            if (weekNumber) 
                query = '/' + weekNumber;
            return $http.get('api/matchday' + query)
                .then(getData)
                .catch(function(message) {
                });

            function getData(result) {
                return result.data;
            }
        }


        // Page /ranks
        function getRanksByWeekNmr(weekNumber) {
            var query = '';
            if (weekNumber) 
                query = '/' + weekNumber;
            return $http.get('api/ranks' + query)
                .then(getData)
                .catch(function(message) {
                });

            function getData(result) {
                return result.data;
            }
        }

        function getAllPassedWeek() {
            return $http.get('api/passedWeeks')
                .then(getData)
                .catch(function(message) {
                });

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
