(function() {
	"use strict";

	/*
	 * This factory only can be used on List-Matchday Controller
	 */
	angular
		.module("app.matchday")
		.factory("commenthelper", CommentHelper);

		function CommentHelper(dataservice) {
			var maxCommentLength = 500;
			var newComment = null;
			var showPostCommentBtn = false;
			var remainingChars = null; // Will be change when user start typing
			var deferred = null;
			var allComments = [];

			var service = {
				initNewComment: initNewComment,
				isShowPostCmntBtn: isShowPostCmntBtn,
				focusOnNewComment: focusOnNewComment,
				userTypeNewComment: userTypeNewComment,
				postNewComment: postNewComment,
				getRemainingChars: getRemainingChars,
				fetchComments: fetchComments,
				getAllComments: getAllComments
			}
			return service;

			function getAllComments() {
				return allComments;
			}

			function fetchComments(matchdayId) {
				allComments = null;
				deferred = $.Deferred();
				dataservice.fetchComments(matchdayId).then(afterFetchComments);
				return deferred;
			}

			function afterFetchComments(resp) {
				if (200 === resp.status) {
					allComments = resp.data.comments;
				}
				deferred.resolve(resp);
			}

			function getRemainingChars() {
				return remainingChars;
			}

			function postNewComment() {
				console.log("postNewComment : ", newComment);
			}

			function userTypeNewComment(value) {
				newComment = value;
				var length = (newComment && newComment.length) 
					? newComment.length
					: 0;
				remainingChars = maxCommentLength - length;
			}

			function focusOnNewComment() {
				showPostCommentBtn = true;
				userTypeNewComment(newComment);
			}

			function isShowPostCmntBtn() {
				return showPostCommentBtn;
			}

			function initNewComment() {
				newComment = null;
				showPostCommentBtn = false;
			}
		}
})();