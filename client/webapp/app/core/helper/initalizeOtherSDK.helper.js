(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("initothersdk", InitOtherSDK);

	function InitOtherSDK(facebookAppId, userauth, adminauth, dataservice, 
		$rootScope) {

		var isFbsdkAlreadyLoaded = false;
		var deferredObject = $.Deferred();

		var service = {
			start: start
		};
		return service;

		function start() {
			initFacebookSDK(facebookAppId);
			return deferredObject.promise();
		}

		function finishInitalize() {

			// make sure all third party finished load
			if (isFbsdkAlreadyLoaded) {

				// Get admin and user session
				var adminSession = adminauth.getAdminSession();
				var userSession = userauth.getUserSession();

				var promise;
				promise = $.Deferred();

				// Cek session order by high priority (admin first)
				if (adminSession) {
					// dataservice.adminCekLogin();
				} else if (userSession) {
					// If has user session then chek the user who loggin in this app
					promise = dataservice.userCekLogin().then(prcessUserLogin);
				} 

				promise.then(resolve);

				// Dont has any user logged and resolve the promise
				if (!adminSession && !userSession) {
					promise.resolve();
				}
			}
		}

		// Resolve main promise and will return to apprun
		function resolve() {
			deferredObject.resolve();
		}

		function prcessUserLogin(result) {
			if (200 === result.status) {
				// change userLogged flag
				$rootScope.isUserLogged = true; 

				// render logged user
				userauth.setLoggedUser(result.data);

				// fetch profile picture
				var userNetworkType = result.data.userNetwork.type;
				var userID = result.data.userNetwork.userNetworkID;
				return dataservice.fetchProfilePicture(userNetworkType, userID);
			}
		}

		function initFacebookSDK(facebookAppId) {
			window.fbAsyncInit = function() {
				FB.init({
					appId: facebookAppId,
					cookie: true,  // enable cookies to allow the server to access 
												 // the session
					xfbml : false,  // parse social plugins on this page
					version : 'v2.2' // use version 2.2
				});

				// set flag finish = true
				isFbsdkAlreadyLoaded = true;
				
				// call finish init function
				finishInitalize();
			};

			(function(d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id)) return;
				js = d.createElement(s); js.id = id;
				js.src = "//connect.facebook.net/en_US/sdk.js";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));
		}

	}
})();
