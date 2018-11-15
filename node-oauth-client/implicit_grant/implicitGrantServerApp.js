var express = require('express'); // Express web server framework
const https = require("https"), fs = require("fs-extra");
//environment variables
process.env.NODE_ENV = 'development';

//config variables
const config = require('../config/config.js');

//console.log(process.env);
const port = parseInt(global.gConfig.node_port || 7999, 10);

var options = {
    key: fs.readFileSync('./ssl/private.pem'),
    cert: fs.readFileSync('./ssl/public.pem'),
};

var app = express();
app.use(express.static(__dirname + '/public'));


app.get('/config', (req, res) => {
    res.json(global.gConfig);
});

/*const server = app.listen(port, () => {
	var address = server.address();
	const url = `http://${address.host || 'localhost'}:${port}`;
	console.info(`Listening at ${url}/`);
	console.log("Listening at %s", port);
});*/
	
const server = https.createServer(options, app).listen(port, () => {
	var address = server.address();
	const url = `https://${address.host || 'localhost'}:${port}`;
	console.info(`Listening at ${url}/`);
	//console.log("Listening at %s", port);
});