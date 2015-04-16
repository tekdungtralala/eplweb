(function() {
	"use strict";

	/*
	 * This factory only can be used on List-Matchday Controller
	 */
	angular
		.module("app.matchday")
		.factory("commenthelper", CommentHelper);

		function CommentHelper() {
			var maxCommentLength = 500;
			var newComment = null;
			var showPostCommentBtn = false;
			var remainingChars = null; // Will be change when user start typing

			var service = {
				initNewComment: initNewComment,
				isShowPostCmntBtn: isShowPostCmntBtn,
				focusOnNewComment: focusOnNewComment,
				userTypeNewComment: userTypeNewComment,
				postNewComment: postNewComment,
				getRemainingChars: getRemainingChars
			}
			return service;

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
				userTypeNewComment(null);
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