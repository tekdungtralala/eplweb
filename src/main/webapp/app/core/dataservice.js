(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('dataservice', Dataservice);

    function Dataservice($http, $q) {
        var isPrimed = false;
        var primePromise;

        var service = {
            // First load
            getInitData: getInitData,
            // Page /matchday
            getAllWeek: getAllWeek, 
            // Page / {dashboard}
            getMatchdayByWeekNmr: getMatchdayByWeekNmr,
            // Page /ranks
            getTeamStat: getTeamStat,
            getRanksByWeekNmr: getRanksByWeekNmr,
            getAllPassedWeek: getAllPassedWeek,
            ready: ready
        };

        return service;

        function getInitData(page) {
            return $http.get('api/page/' + page)
                .then(getData)
                .catch(function(message) {
                });

            function getData(result) {
                return result.data;
            }
        }

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
        function getTeamStat(weekNumber, teamId) {
            return $http.get('api/chart/week/' + weekNumber + '/team/' + teamId)
                .then(getData)
                .catch(function(message) {
                });

            function getData(result) {
                console.log("getdata : ", result.data);
                return result.data;
            }
        }
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
