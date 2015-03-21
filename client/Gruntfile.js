module.exports = function(grunt) {

	var appjsFiles = [
		// Firstly, init app module 
		'webapp/app/app.module.js',
		// Then all sub module
		'webapp/app/**/*.module.js',
		// Core app
		'webapp/app/core/**/*.js',
		// And the last is all other .js
		'webapp/app/**/*.js',
	];

	var jsVendorFiles = [
		'webapp/bower_components/jquery/dist/jquery.min.js',
		'webapp/bower_components/jquery-ui/jquery-ui.min.js',
		'webapp/bower_components/jquery-validation/dist/jquery.validate.min.js',
		'webapp/bower_components/jquery-validation/dist/additional-methods.min.js',
		'webapp/bower_components/bootstrap/dist/js/bootstrap.min.js',
		'webapp/bower_components/angular/angular.min.js',
		'webapp/bower_components/angular-animate/angular-animate.min.js',
		'webapp/bower_components/angular-cookies/angular-cookies.min.js',
		'webapp/bower_components/angular-ui-router/release/angular-ui-router.min.js',
		'webapp/bower_components/angular-busy/dist/angular-busy.min.js',
		'webapp/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
		'webapp/bower_components/underscore/underscore.js',
		'webapp/bower_components/moment/moment.js',
		'webapp/bower_components/slimScroll/jquery.slimscroll.js',
		'webapp/bower_components/fastclick/lib/fastclick.js',
		'webapp/bower_components/ng-file-upload/angular-file-upload-shim.min.js',
		'webapp/bower_components/ng-file-upload/angular-file-upload.min.js',

		'webapp/eplweb_components/js/highcharts.js',
		'webapp/eplweb_components/js/exporting.js',
		'webapp/eplweb_components/js/slider.js',
		'webapp/eplweb_components/js/app.js', //Admin LTE
	];

	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		watch: {
			files: appjsFiles,
			tasks: ['concat:appjs']
		},
		concat: {
			options: {
				separator: '\n\n'
			},
			vendorjs: {
				src: jsVendorFiles,
				dest: 'webapp/eplweb_components/js/vendors.js'
			},
			appjs: {
				src: appjsFiles,
				dest: 'webapp/eplweb_components/js/eplweb.js'
			}
		}
	});

	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-concat');

	grunt.registerTask('default', []);
}