(function() {
    'use strict';

    angular
        .module('app.team')
        .controller('Team', Team);

    function Team(dataservice, $routeParams) {
        // window.location.href = "#/rank";
        // console.log($routeParams);
        var vm = this;

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
        vm.container = [true, true, false, true, true]; 

        vm.statContainerLbl = ['Played', 'Won', 'Drawn', 'Lost', 'Points'];

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

        // ngClick
        vm.selectContainer = function(index){
            _.each(vm.container, function(i, contIndex){
                vm.container[contIndex] = true;
            });
            vm.container[index] = false;
        }

    }
})();
