module.exports = function(grunt) {
  grunt.initConfig({
    ts: {
      default : {
        tsconfig: true
      }
    },
    bower_concat: {
      all: {
        dest: 'dist/bower_components.js',
        cssDest: 'dist/bower_components.css',
      }
    },
    copy: {
      main: {
        files: [
          {src: ['package.json'], dest: 'dist/'},
          {src: ['index.html'], dest: 'dist/'}
        ]
      }
    }
  });
  grunt.loadNpmTasks("grunt-ts");
  grunt.loadNpmTasks('grunt-bower-concat');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.registerTask("default", ["ts", "bower_concat", "copy"]);
};
