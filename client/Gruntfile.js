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

	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		watch: {
			files: ['webapp/app/app.module.js'],
			tasks: ['concat:dist']
		},
		concat: {
			options: {
				separator: '\n\n'
			},
			dist: {
				src: ['webapp/app/app.module.js'],
				dest: 'webapp/eplweb.js'
			},
			appjs: {
				// src: 'webapp/app/**/*.js',
				src: appjsFiles,
				dest: 'webapp/eplweb_components/js/eplweb.js'
			}
		}
	});

	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-concat');

	grunt.registerTask('default', []);
}