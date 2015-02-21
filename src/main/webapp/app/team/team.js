(function() {
    'use strict';

    angular
        .module('app.team')
        .controller('Team', Team);

    function Team(dataservice, datautil, $routeParams) {
        var vm = this;
        var id = $routeParams.id;
        var name = $routeParams.name;
        vm.teams = [];
        vm.currTeam = null;
        vm.rank = null;
        vm.nextMatchday = [];
        vm.prevMatchday = [];

        $('[data-toggle="tooltip"]').tooltip();

        activate();
        function activate() {
            return getInitData().then(function(result){
                console.log(result);
                vm.teams = result.teams;
                vm.rank = result.rank;

                vm.currTeam = _.find(vm.teams, function(t){
                    return t.id === parseInt(id);
                });

                _.each(result.matchdays, function(m){
                    m.formatedWeek = getFormattedWeek(m.week);
                    if (m.awayGoal === -1 || m.homeGoal === -1) {
                        vm.nextMatchday.push(m);
                    } else {
                        vm.prevMatchday.push(m);
                    }
                });

                console.log(vm.prevMatchday);
            });
        }


        // ngClick
        vm.selectContainer = function(index){
            _.each(vm.container, function(i, contIndex){
                vm.container[contIndex] = true;
            });
            vm.container[index] = false;
        }

        function getFormattedWeek(w){
            return datautil.getFormattedWeek(w.startDay, w.weekNumber);
        }

        function getInitData(){
            return dataservice.getInitData('team/' + id + '/' + name)
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

        // ngclick
        vm.changeCarousel = function(to){
            $('.carousel').carousel(to)
        };


        vm.containerLbl = ['Overview', 'Squad', 'Statistic', 'Map', 'Video'];

        // Container below carousel/slideshow, false for active
        vm.container = [false, true, true, true, true]; 

        vm.statContainerLbl = ['Played', 'Won', 'Drawn', 'Lost', 'Points'];

        initChart(
            [ {
                "data" : [ 53.0, 16.0, 2.0, 5.0, 52.0, 20.0 ],
                "name" : "Chelsea"
            }, {
                "data" : [ 31.0, 8.0, 8.0, 6.0, 29.0, 29.0 ],
                "name" : "Other Team"
            } ]
            ,
            [ "Points", "Win Rate", "Win Lose", "Win Draw", "Goal Scored", "Goal Against" ]
            );

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

    }
})();
