(function() {
    'use strict';

    angular
        .module('app.rank')
        .controller('Rank', Rank);

    function Rank(dataservice, datautil) {
        var vm = this;
        vm.ranks = [];
        vm.weeks = [];
        vm.nextRankDisable = true;
        vm.prevRankDisable = false;
        vm.currWeek = null;
        vm.selectedWeek = null;
        vm.currTeam = null;
        vm.changeWeek = changeWeek;
        vm.showChart = showChart;

        activate();
        function activate() {
            return getInitData().then(function(result){
                processWeekData(result.weeks);
                var lastWeek = parseInt(vm.weeks[0].weekNumber);
                processRankData(result.ranks, lastWeek);
            });
        }

        function showChart(teamIndex){
            vm.currTeam = vm.ranks[teamIndex];
            getTeamStat(vm.currWeek, vm.currTeam.team.id).then(function(data){
                initChart(data.series, data.categories);
                $('#epl-modal-id').modal('show');
            });
        }

        function initChart(series, categories){
            $('#epl-chart-container').highcharts({
                series: series,
                xAxis: {
                    categories: categories
                },
                chart: {
                    type: 'column'
                },
                title: {
                    text: '',
                    style: {
                        'display': 'none'
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Rainfall (mm)'
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                exporting: { enabled: false }
            });
        }

        function processRankData(ranks, currWeek){
            vm.ranks = ranks;
            initCurrPrevNext(currWeek);
        }

        function processWeekData(weeks){
            vm.weeks = weeks;
            _.each(vm.weeks, function(w){
                // Set dateView attribute
                w.dateView = getFormattedWeek(w);
            });
        }

        function initCurrPrevNext(currWeek){
            vm.currWeek = currWeek;

            vm.nextRankDisable = false;
            vm.prevRankDisable = false;

            var lastWeek = parseInt(vm.weeks[0].weekNumber);
            if (currWeek == 1) {
                vm.prevRankDisable = true;
            } else if (currWeek == lastWeek) {
                vm.nextRankDisable = true;
            }

            var currWeek = _.find(vm.weeks, function(w){
                return parseInt(w.weekNumber) === vm.currWeek;
            });
            vm.selectedWeek = getFormattedWeek(currWeek);
        }

        function changeWeek(otherWeek){
            otherWeek = parseInt(otherWeek);
            getRanksByWeekNmr(otherWeek).then(function(data){
                processRankData(data, otherWeek);
            });
        }

        function getFormattedWeek(w){
            return datautil.getFormattedWeek(w.startDay, w.weekNumber);
        }

        function getTeamStat(weekNumber, teamId) {
            return dataservice.getTeamStat(weekNumber, teamId).then(function(data) {
                return data;
            });
        }

        function getRanksByWeekNmr(otherWeek){
            return dataservice.getRanksByWeekNmr(otherWeek).then(function(data) {
                return data.ranks;
            });
        }

        function getInitData(){
            return dataservice.getInitData('rank').then(function(data) {
                return data;
            });
        }
    }
})();
