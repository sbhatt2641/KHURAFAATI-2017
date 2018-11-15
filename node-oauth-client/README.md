Mendeley Show Me Access Tokens
==============================
# Introduction #
This is a simple Node application that uses the Mendeley API and can be deployed to [Heroku](http://www.heroku.com). 

It demonstrates how to use the OAuth 2.0 [Authorisation Code Flow](http://dev.mendeley.com/reference/topics/authorization_auth_code.html) and the [Implicit Grant Type](http://dev.mendeley.com/reference/topics/authorization_implicit.html). 

In each case it will make a call to the [profiles service](http://dev.mendeley.com/methods/#profiles). 


## Prerequisites ##

1. [Install NPM](http://blog.npmjs.org/post/85484771375/how-to-install-npm)

## Getting started ##
 1. Register your client at the [developer portal](http://dev.mendeley.com). Use `dummy` as your redirect URI. This will give you a client ID and secret.
 
2. Click on the button below to deploy the app to Heroku.  You will need to fill in the client ID and secret from the previous step.
 
[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy?template=git@github.com:MendeleyStack/mendeley-show-me-access-tokens.git)

3. Back at the developer portal, change your app's redirect URI to the Heroku HTTPS URL. For example, if your Heroku app's name is:
 
    `secure-citadel-6801`

    then your redirect URI should be:
    
    `https://secure-citadel-6801.herokuapp.com/`

Your app should now be deployed.  You can now follow the instructions in the Heroku console to clone and edit the code.


## Run authorisation code flow locally ##
1. Clone the app using the instructions on the "Code" tab in the Heroku console.
2. Register another client ID and secret, with the redirect URL `http://localhost:5000/callback` and store in a `local.env` file e.g. 

	- CLIENT_ID=xxxx
	- CLIENT_SECRET=xxxxx
	- REDIRECT_URI=http://localhost:5000/callback
	
 3. Run `heroku local -e local.env ` to start the app, and go to `http://localhost:5000` in your browser. 

 
## Run implicit flow locally ##
1. Clone the app using the instructions on the "Code" tab in the Heroku console.
2. Register another client ID and secret, with the redirect URL `http://localhost:8888` and store in a `local.env` file e.g. 

	- CLIENT_ID=xxxx
	- REDIRECT_URI=http://localhost:8888
	- PORT=8888
	
 3. Change the command to point to the `app.js` in the implicit_grant folder e.g. `web: node implicit_grant/app.js $PORT $REDIRECT_URI $CLIENT_ID` 
 
 4. Run `heroku local -e local.env ` to start the app, and go to `http://localhost:8888` in your browser. 

 

## Further reading ##
- Developer portal: [http://dev.mendeley.com](http://dev.mendeley.com)
- Heroku: [https://devcenter.heroku.com/](https://devcenter.heroku.com/)
# mendeley-show-me-access-tokens
