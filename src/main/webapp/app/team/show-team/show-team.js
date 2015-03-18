(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("ShowTeam", ShowTeam);

		function ShowTeam(xhrTeams, xhrSlideShow, dataservice, datautil, $state, 
			$stateParams) {

			var vm = this;

			vm.containerLbl = ["overview", "squad", "statistic", "map", "video"];

			// Container below carousel/slideshow, false for active
			vm.container = [false, true, true, true, true];

			vm.showEditBtn = false;
			vm.currTeam = null;
			vm.rank = null;
			vm.position = null;
			vm.carousel = [];

			vm.nextMatchday = [];
			vm.prevMatchday = [];
			vm.currWeek = null;
			vm.currWeekView = null;

			vm.squads = [];
		
			vm.changeCarousel = changeCarousel;
			vm.selectContainer = selectContainer;
			vm.gotoEditTeam = gotoEditTeam;

			activate();
			function activate() {
				// Find current team
				vm.currTeam = _.find(xhrTeams.result, function(t) {
					return t.id === parseInt($stateParams.id);
				});

				// Initialize slide show
				var allImage = xhrSlideShow.result;
				_.each(allImage, function(image, i) {
					vm.carousel.push({
						isActive: i == 0,
						src: dataservice.getImageById(image.id)
					});
				});

				var promises = [
					getInitData(), 
					dataservice.hasAdminRole()
				];

				dataservice.ready(promises).then(function(result){
					processRankData(result[0]);

					$state.go("team.show-team." + vm.containerLbl[0], $stateParams);

					processAdmnRole(result[1]);
				});
			}

			function gotoEditTeam() {
				$state.go("team.show-team.edit-team");
			}

			function processAdmnRole(result) {
				if (result && result.status === 200) {
					vm.showEditBtn = true;
				} else {
					vm.showEditBtn = false;
				}
			}

			function processRankData(data) {
				var id = $stateParams.id;
				vm.position = 0;
				vm.rank = _.find(data.ranks, function(r, i){
					vm.position++;
					return r.team.id === parseInt(id);
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

			function changeCarousel(to){
				$(".carousel").carousel(to);
			}

			function selectContainer(index) {
				// Change class of each button nav
				_.each(vm.container, function(i, contIndex){
					vm.container[contIndex] = true;
				});
				vm.container[index] = false;

				// Move to selected state
				if (1 === index) {
					if (!vm.squads.length > 0) {
						getPlayersByTeamId(vm.currTeam.id)
							.then(processSquadData);
					}
					$state.go("team.show-team." + vm.containerLbl[index], $stateParams);
				} else if (2 === index) {
					getTeamStat(vm.currWeek.weekNumber, vm.currTeam.id)
						.then(processChartData);
					$state.go("team.show-team." + vm.containerLbl[index], $stateParams);
				} else {
					$state.go("team.show-team." + vm.containerLbl[index], $stateParams);
				}
			}

			function processSquadData(data) {
				vm.squads[0] = [];vm.squads[1] = [];

				var index = 0;
				var pos = 0;
				_.each(data.result, function(d) {

					var position = d.position.charAt(0).toUpperCase() + 
						d.position.slice(1).toLowerCase();
					d.position = position;

					vm.squads[pos][index] = d;
					index++;
					if (index > data.result.length / 2) {
						index = 0;
						pos = 1;
					}
				});
			}

			function processChartData(data) {
				initChart(data.series, data.categories);
			}

			function initChart(series, categories) {
				$("#epl-chart-container").highcharts({
					series: series,
					xAxis: {
						categories: categories
					},
					chart: {
						type: "column"
					},
					title: {
						text: "",
						style: {
							"display": "none"
						}
					},
					yAxis: {
						min: 0,
						title: {
							text: "Rainfall (mm)"
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

			function getPlayersByTeamId( teamId){
				return dataservice.getPlayersByTeamId(teamId).then(function(data) {
					return data;
				});
			}

			function getTeamStat(weekNumber, teamId){
				return dataservice.getTeamStat(weekNumber, teamId).then(function(data) {
					return data;
				});
			}

			function getInitData(){
				var id = $stateParams.id;
				var simpleName = $stateParams.simpleName;
				return dataservice.getInitData("team/" + id + "/" + simpleName)
					.then(function(data) {
						return data;
					});
			}
		}
})();
