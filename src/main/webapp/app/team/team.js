(function() {
    'use strict';

    angular
        .module('app.team')
        .controller('Team', Team);

    function Team(dataservice, datautil, $routeParams) {
        var vm = this;

        vm.containerLbl = ['Overview', 'Squad', 'Statistic', 'Map', 'Video'];

        // Container below carousel/slideshow, false for active
        vm.container = [false, true, true, true, true];

        var id = $routeParams.id;
        var simpleName = $routeParams.name;

        vm.currTeam = null;
        vm.teams = [];
        
        vm.rank = null;
        vm.position = null;

        vm.nextMatchday = [];
        vm.prevMatchday = [];
        vm.currWeek = null;
        vm.currWeekView = null;

        vm.chartData = null;
        vm.selectContainer = selectContainer;
        vm.changeCarousel = changeCarousel;
        

        $('[data-toggle="tooltip"]').tooltip();

        activate();
        function activate() {
            return getInitData().then(function(result){
                processRankData(result);
            });
        }

        function processRankData(data){
            vm.teams = data.teams;
            vm.position = 0;
            vm.rank = _.find(data.ranks, function(r, i){
                vm.position++;
                return r.team.id === parseInt(id);
            });

            vm.currTeam = _.find(vm.teams, function(t){
                return t.id === parseInt(id);
            });

            _.each(data.matchdays, function(m){
                m.formatedWeek = getFormattedWeek(m.week);
                if (m.awayGoal === -1 || m.homeGoal === -1) {
                    vm.nextMatchday.push(m);
                } else {
                    vm.prevMatchday.push(m);

                    vm.currWeek = m.week;
                }
            });

            vm.currWeekView = getFormattedWeek(vm.currWeek);
        }

        function selectContainer(index){
            _.each(vm.container, function(i, contIndex){
                vm.container[contIndex] = true;
            });
            vm.container[index] = false;

            // Selected statistic tab
            if (2 === index) {
                if (vm.chartData == null) {
                    getTeamStat(vm.currWeek.weekNumber, vm.currTeam.id).then(function(data){
                        vm.chartData = data;
                        initChart(vm.chartData.series, vm.chartData.categories);
                    });
                } else {
                    initChart(vm.chartData.series, vm.chartData.categories);
                }
            }
        }

        function changeCarousel(to){
            $('.carousel').carousel(to)
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

        function getFormattedWeek(w){
            return datautil.getFormattedWeek(w.startDay, w.weekNumber);
        }

        function getTeamStat(weekNumber, teamId){
            return dataservice.getTeamStat(weekNumber, teamId).then(function(data) {
                return data;
            });
        }

        function getInitData(){
            return dataservice.getInitData('team/' + id + '/' + simpleName)
                .then(function(data) {
                    return data;
                });
        }



        vm.halfTeam = [];
        for(var i = 1; i <= 20; i++){
            vm.halfTeam.push(i);
        }

        vm.carousel = [];
        vm.carousel[0] = {
            isActive: true,
            src:'eplweb_components/image/slideshow/default/slideshow-1.jpg'
        };
        vm.carousel[1] = {
            isActive: false,
            src:'eplweb_components/image/slideshow/default/slideshow-1.jpg'
        };
        vm.carousel[2] = {
            isActive: false,
            src:'eplweb_components/image/slideshow/default/slideshow-1.jpg'
        };
    }
})();
