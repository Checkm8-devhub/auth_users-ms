# USERS Microservice

- **Keycloak** stores username, password, email
- ```/users``` stores app related stuff like ELO, ...? 

# KEYCLOAK

## DEV
```
docker run -p 127.0.0.1:8083:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin --name checkm8_keycloak quay.io/keycloak/keycloak start-dev
```
```
docker start checkm8_keycloak
```

## REGISTRATION
```
http://localhost:8083/realms/auth/protocol/openid-connect/registrations?client_id=main_client&response_type=code
```

## LOGIN
- get auth code:
```
http://localhost:8083/realms/auth/protocol/openid-connect/auth?client_id=main_client&response_type=code&scope=openid&redirect_uri=<redirect_url>
```

- get JWT from auth code
```
curl -X POST "http://localhost:8083/realms/auth/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "client_id=main_client" \
  -d "code=a89c5452-9102-40f0-52d4-8ceb5a19eace...." \
  -d "redirect_uri=http://localhost:8080/"
```
- access token == JWT

## ACCOUNT
```
http://localhost:8083/realms/auth/account
```

# ```/users```

- CRUD operations on the ```/users``` endpoint
