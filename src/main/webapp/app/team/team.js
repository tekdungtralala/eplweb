(function() {
    'use strict';

    angular
        .module('app.team')
        .controller('Team', Team);

    function Team(dataservice) {
        var vm = this;

        vm.halfTeam = [];
        for(var i = 1; i <= 20; i++){
            vm.halfTeam.push(i);
        }

        vm.containerLbl = ['Overview', 'Squad', 'Statistic', 'Map', 'Video'];
        vm.container = [false, true, true, true, true]; // true to hide

        $('[data-toggle="tooltip"]').tooltip();

        activate();
        function activate() {
            // var promises = [getAllPassedWeek(),getRanksByWeekNmr(null)];
            // return dataservice.ready(promises).then(function(result){
            //     processWeekData(result[0]);
            //     var lastWeek = parseInt(vm.weeks[0].weekNumber);
            //     processRankData(result[1], lastWeek);
            // });
        }

        vm.selectElement = function(index){
            _.each(vm.container, function(i, contIndex){
                vm.container[contIndex] = true;
            });
            vm.container[index] = false;
        }

    }
})();
