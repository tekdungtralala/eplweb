(function() {
    'use strict';

    angular
        .module('app.matchday')
        .controller('Matchday', Matchday);

    function Matchday(dataservice) {
        var vm = this;

        // activate();
        // function activate() {
        //     var promises = [getAllWeek(),getRanksByWeekNmr(null)];
        //     return dataservice.ready(promises).then(function(result){
        //         processWeekData(result[0]);
        //         var lastWeek = parseInt(vm.weeks[0].weekNumber);
        //         processRankData(result[1], lastWeek);
        //     });
        // }
    }
})();
