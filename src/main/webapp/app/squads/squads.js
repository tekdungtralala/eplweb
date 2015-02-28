(function() {
    'use strict';

    angular
        .module('app.squads')
        .controller('Squads', Squads)
        .controller('SquadsTeam', SquadsTeam);

    function SquadsTeam(teams, $stateParams, dataservice) {
        var vm = this;
        var teamId = $stateParams.teamId;

        activate();
        function activate() {
            getPlayersByTeamId(teamId).then(processAllSquad);
        }

        function processAllSquad(data) {
            vm.squads = data;
        }

        function getPlayersByTeamId(teamId) {
            return dataservice.getPlayersByTeamId(teamId).then(function(data) {
                return data.result;
            });
        }
    }

    function Squads(teams) {
        var vm = this;
        vm.teams = teams.result;
    }

})();
