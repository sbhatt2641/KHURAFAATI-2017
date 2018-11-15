var express = require('express'); // Express web server framework
var request = require('request'); // "Request" library
var querystring = require('querystring');
var cookieParser = require('cookie-parser');

const https = require("https"), fs = require("fs-extra");

//environment variables
process.env.NODE_ENV = 'development';
//process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

//config variables
const config = require('../config/config.js');

var client_id = global.gConfig.oauth2.CLIENT_ID; // Your client id
var client_secret = global.gConfig.oauth2.CLIENT_SECRET; // Your client secret
var redirect_uri = global.gConfig.oauth2.REDIRECT_URI;
var port = parseInt(global.gConfig.node_port || 7999, 10);

var options = {
    key: fs.readFileSync('./ssl/private.pem'),
    cert: fs.readFileSync('./ssl/public.pem'),
};
/**
 * Generates a random string containing numbers and letters
 * @param  {number} length The length of the string
 * @return {string} The generated string
 */
var generateRandomString = function(length) {
  var text = '';
  var possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

  for (var i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  }
  return text;
};

var stateKey = 'state';

var app = express();

app.use(express.static(__dirname + '/public'))
   .use(cookieParser());

app.get('/login', function(req, res) {

  var state = generateRandomString(16);
  res.cookie(stateKey, state);

  // your application requests authorization
  var scope = 'read';
  res.redirect('https://directsales-ub-local.ncw.webapps.rr.com:8081/oauth/authorize?' +
    querystring.stringify({
      response_type: 'code',
      client_id: client_id,
      scope: scope,
      redirect_uri: redirect_uri,
      state: state
    }));
});


app.get('/callback', function(req, res) {

  // your application requests refresh and access tokens
  // after checking the state parameter
  var code = req.query.code || null;
  var state = req.query.state || null;
  var storedState = req.cookies ? req.cookies[stateKey] : null;
  var auth_token = 'fooClientId:fooSecret';
  //auth_token = window.btoa(auth_token);
  auth_token = new Buffer(auth_token).toString('base64');
  if (state === null || state !== storedState) {
    res.redirect('/#' +
      querystring.stringify({
        error: 'state_mismatch'
      }));
  } else {
    res.clearCookie(stateKey);
    var authOptions = {
      strictSSL: false,
      rejectUnhauthorized : false,
      url: 'https://directsales-ub-local.ncw.webapps.rr.com:8081/oauth/token',
      form: {
        code: code,
        redirect_uri: redirect_uri,
        grant_type: 'authorization_code',
        client_id: client_id,
        client_secret: client_secret
      },
      headers: { 'Authorization': 'Basic ' + auth_token },
      json: true
    };

    request.post(authOptions, function(error, response, body) {
    	//console.log(response);
      if (!error && response.statusCode === 200) {

        var access_token = body.access_token, refresh_token = body.refresh_token;

        var options = {
          url: 'https://directsales-ub-local.ncw.webapps.rr.com:8082/api',
          headers: { 'Authorization': 'Bearer ' + access_token },
          json: true
        };

        // use the access token to access the Spotify Web API
        request.get(options, function(error, response, body) {
          console.log(body);
        });

        // we can also pass the token to the browser to make requests from there
        res.redirect('/#' +
          querystring.stringify({
            access_token: access_token,
            refresh_token: refresh_token
          }));
      } else {
        res.redirect('/#' +
          querystring.stringify({
            error: 'invalid_token'
          }));
      }
    });
  }
});

app.get('/refresh_token', function(req, res) {

  // requesting access token from refresh token
  var refresh_token = req.query.refresh_token;
  var authOptions = {
    url: 'https://directsales-ub-local.ncw.webapps.rr.com:8081/oauth/token',
    headers: { 'Authorization': 'Basic ' + (new Buffer(client_id + ':' + client_secret).toString('base64')) },
    form: {
      grant_type: 'refresh_token',
      refresh_token: refresh_token
    },
    json: true
  };

  request.post(authOptions, function(error, response, body) {
    if (!error && response.statusCode === 200) {
      var access_token = body.access_token;
      res.send({
        'access_token': access_token
      });
    }
  });
});

const server = https.createServer(options, app).listen(port, () => {
	var address = server.address();
	const url = `https://${address.host || 'localhost'}:${port}`;
	console.info(`Listening at ${url}/`);
	//console.log("Listening at %s", port);
});