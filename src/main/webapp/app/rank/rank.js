(function() {
    'use strict';

    angular
        .module('app.rank')
        .controller('Rank', Rank);

    // rank.$inject = [];

    function Rank(dataservice) {
        var vm = this;
        vm.ranks = [];
        vm.weeks = [];
        vm.nextRankDisable = true;
        vm.prevRankDisable = false;
        vm.nextWeek = null;
        vm.prevWeek = null;
        vm.currWeek = null;

        activate();
        function activate() {
            var promises = [getAllWeek(),getRanksByWeekNmr(null)];
            return dataservice.ready(promises).then(function(result){
                processWeekData(result[0]);
                var lastWeek = parseInt(vm.weeks[0].weekNumber);
                processRankData(result[1], lastWeek);
            });
        }

        function processRankData(ranks, currWeek){
            vm.ranks = ranks;
            initCurrPrevNext(currWeek);
        }

        function processWeekData(weeks){
            vm.weeks = weeks;
            _.each(vm.weeks, function(w){
                // Set dateView 
                var date = new Date(w.startDay);
                var m = moment(date);
                w.dateView = '#' + w.weekNumber + ' - ' + m.format('YYYY, DD MMM');
            });
        }

        function initCurrPrevNext(currWeek){
            vm.currWeek = currWeek;
            vm.nextWeek = vm.currWeek + 1;
            vm.prevWeek = vm.currWeek - 1;

            var lastWeek = parseInt(vm.weeks[0].weekNumber);
            if (currWeek == 1) {
                vm.prevRankDisable = true;
                vm.nextRankDisable = false;
            } else if (currWeek == lastWeek) {
                vm.prevRankDisable = false;
                vm.nextRankDisable = true;
            } else {
                vm.prevRankDisable = false;
                vm.nextRankDisable = false;
            }
        }

        // ngClick
        vm.changeWeek = function(otherWeek){
            otherWeek = parseInt(otherWeek);
            getRanksByWeekNmr(otherWeek).then(function(data){
                processRankData(data, otherWeek);
            });
        }

        // Get weeks through service
        function getAllWeek() {
            return dataservice.getAllWeek().then(function(data) {
                return data.weeks;
            });
        }

        // Get ranks through service
        function getRanksByWeekNmr(weekNumber) {
            return dataservice.getRanksByWeekNmr(weekNumber).then(function(data) {
                return data.ranks;
            });
        }
    }
})();
