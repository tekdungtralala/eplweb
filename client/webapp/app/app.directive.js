(function() {
	"use strict";

	angular.module("app")
		.directive("eplwebScrollPosition", eplwebScrollPosition);

	function eplwebScrollPosition($window) {
		return {
			scope: {
				value: '='
			},
			link: function(scope, element, attrs) {
				var windowEl = angular.element($window);
				var handler = function() {
					scope.value = windowEl.scrollTop();
				}
				windowEl.on('scroll', scope.$apply.bind(scope, handler));
				handler();
			}
		};
	}
	
})();