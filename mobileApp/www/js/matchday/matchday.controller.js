(function() {
	"use strict";

	angular.module('app.matchday')
		.controller('MatchdayCtrl', MatchdayCtrl);

	function MatchdayCtrl($ionicPlatform, dataservice) {
		var vm = this;
		vm.datas = [];

		activate();
		function activate() {
			// fetchMatchdays();	
			$ionicPlatform.ready(function() {
				facebookConnectPlugin.login(["public_profile"], 
					function(a,b,c,d) {
						console.log("SUCCESS");
						console.log("a : ", a);
						console.log("b : ", b);
						console.log("c : ", c);
						console.log("d : ", d);
					},
					function(a,b,c,d) {
						console.log("Fail");
						console.log("a : ", a);
						console.log("b : ", b);
						console.log("c : ", c);
						console.log("d : ", d);
					});
			});
		};

		function setUpMatchdays() {
			console.log("_ : ", _)
		}

		function fetchMatchdays() {
			dataservice.fetchMatchdays().then(processMatchdays);
		}

		function processMatchdays(result) {
			if (200 === result.status) {
				vm.datas = result.data.matchdayModelView.model;
				setUpMatchdays();
			}
		};
	};
	
})();