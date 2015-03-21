module.exports = function(grunt) {
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		watch: {
			files: ['webapp/app/app.module.js'],
			tasks: ['concat:dist']
		},
		concat: {
			options: {
				separator: ''
			},
			dist: {
				src: ['webapp/app/app.module.js'],
				dest: 'webapp/eplweb.js'
			},
			css: {
				src: [
					'bower_components/bootstrap/dist/css/bootstrap.css',
					'bower_components/font-awesome/css/font-awesome.min.css',
					'bower_components/ionicons/css/ionicons.min.css',
					'bower_components/angular-busy/dist/angular-busy.min.css',
					'bower_components/jquery-ui/themes/smoothness/jquery-ui.css',
					'webapp/eplweb_components/css/AdminLTE.css',
					'webapp/eplweb_components/css/skin-purple.css',
					'webapp/eplweb_components/css/index.css',
					'webapp/eplweb_components/css/epl-animate.css'
				],
				dest: 'webapp/eplweb_components/css/eplweb.css'
			}
		}
	});

	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-concat');

	grunt.registerTask('default', []);
}