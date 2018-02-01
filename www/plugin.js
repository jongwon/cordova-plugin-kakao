
var exec = require('cordova/exec');

var PLUGIN_NAME = 'KakaoPlugin';

var KakaoPlugin = {

	login: function(s, f) {
		exec(s, f, PLUGIN_NAME, 'login', []);
	},

	logout: function(s, f) {
		exec(s, f, PLUGIN_NAME, 'logout', []);
	},

	share: function(s, f) {
		exec(s, f, PLUGIN_NAME, 'share', []);
	}
	
};

module.exports = KakaoPlugin;
