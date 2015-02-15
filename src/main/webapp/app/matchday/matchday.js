(function() {
    'use strict';

    angular
        .module('app.matchday')
        .controller('Matchday', Matchday);

    function Matchday(dataservice) {
        var vm = this;
        vm.weeks = [];
        vm.model = [];
        vm.selectedWeek = null;
        vm.currWeek = null;
        vm.nextRankDisable = false;
        vm.prevRankDisable = false;

        activate();
        function activate() {
            var promises = [getAllWeek(), getMatchdayByWeekNmr()];
            return dataservice.ready(promises).then(function(result){
                processWeekData(result[0]);
                processMatchData(result[1]);
            });
        }

        function processMatchData(data){
            vm.model = data.model;
            vm.currWeek = parseInt(data.week.weekNumber);
            vm.selectedWeek = getFormattedWeek(data.week);

            updatePrevNexBtn();
        }

        function updatePrevNexBtn(){
            vm.nextRankDisable = false;
            vm.prevRankDisable = false;
            if (vm.currWeek == 1){
                vm.prevRankDisable = true;
            } else if (vm.currWeek  == vm.weeks.length){
                vm.nextRankDisable = true;
            }
        }

        function processWeekData(weeks){
            vm.weeks = weeks;
            _.each(vm.weeks, function(w){
                // Set dateView attribute
                w.dateView = getFormattedWeek(w);
            });
        }

        function getFormattedWeek(w){
            var date = new Date(w.startDay);
            var m = moment(date);
            return '#' + w.weekNumber + ' - ' + m.format('YYYY, DD MMM');
        }

        // ngClick
        vm.changeWeek = function(otherWeek){
            otherWeek = parseInt(otherWeek);
            getMatchdayByWeekNmr(otherWeek).then(function(data){
                processMatchData(data);
            });
        }

        // Get weeks through service
        function getAllWeek() {
            return dataservice.getAllWeek().then(function(data) {
                return data.weeks;
            });
        }

        // Get matchday through service
        function getMatchdayByWeekNmr(weekNumber) {
            return dataservice.getMatchdayByWeekNmr(weekNumber).then(function(data) {
                return data;
            });
        }
    }
})();
