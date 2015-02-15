(function() {
    'use strict';

    angular
        .module('app.dashboard')
        .controller('Dashboard', Dashboard);

    function Dashboard(dataservice) {
    	var vm = this;
    	vm.ranks = [];
        vm.model = [];
        vm.chartData = {}

        activate();
        function activate() {
            var promises = [
                getHighestRanks(), 
                getMatchdayByWeekNmr(), 
                getFiveBigTeamData()
            ];

            return dataservice.ready(promises).then(function(result){
            	vm.ranks = result[0];
                vm.model = result[1];

                vm.chartData.categories = result[2].categories;
                vm.chartData.series = result[2].series;

                initChart();
            });
        }

        function initChart(){
            $('#epl-chart-container').highcharts({
                title: {
                    text: 'Five Biggest Teams',
                    style: {
                        display: 'none'
                    }
                },
                xAxis: {
                    title: {
                        text: 'Week'
                    },
                    categories: vm.chartData.categories
                },
                yAxis: {
                    title: {
                        text: 'Point'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                },
                tooltip: {
                    valueSuffix: ' Pts'
                },
                legend: {
                    layout: 'horizontal',
                    
                    verticalAlign: 'top',
                    borderWidth: 0
                },
                series: vm.chartData.series
            });
        }

        // Get chart data through service
        function getFiveBigTeamData() {
            return dataservice.getFiveBigTeamData().then(function(data) {
                return data;
            });
        }


        // Get ranks through service
        function getHighestRanks() {
            return dataservice.getHighestRanks().then(function(data) {
                return data.ranks;
            });
        }

        // Get matchday through service
        function getMatchdayByWeekNmr() {
            return dataservice.getMatchdayByWeekNmr().then(function(data) {
                return data.model;
            });
        }
    }

})();
