# Home Use

This is a project created to learn the account linking between 2 OAuth Providers. As a prototype, this project attempts
to Sign in with Google using the spring security API and links with Honeywell Thermostat API integration.

## Features

Here is the wishlist for the features that are planned to be built.

- [x] Sign in using Google
- [x] Persist the user details on to the database
- [x] Containerize the application development using `Docker`
- [x] Encrypt the credentials using the Spring Cloud Security
- [x] Persist the Account linking details
- [x] Perform OAUTH token renewal for expired tokens
- [x] Query for the Locations on the Honeywell account
- [ ] Display the locations linked to the Honeywell account
- [ ] Provide UI to view the location wise devices
- [ ] Allow user to manage devices at each location
- [ ] Build API for users to manage devices
- [ ] Provide our own never expiring API credentials to users to interact using API
- [ ] Provide option to reset their secrets
- [ ] Host it on cloud
- [ ] Socialize the app on network

## Setting up credentials

1. Please create a `.env` file with key `ENCRYPT_KEY` as and use your own `secret password` as value.
2. Register with Google and create OAUTH Login setup
3. Register with Honeywell Developer Portal get your credentials
4. Encrypt and save the credentials on the configuration files
    1. Using `SpringEncryptDecrypt` run it inside IDE or terminal to encrypt your credentials
5. Add the environment variable key values from `.env` file to your Run Configuration on IDE
