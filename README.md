# PlumeAlerts-API

# Generating public/private key
**In working directory**
```
openssl genrsa 2048 > rsa.private
```
#### Private Key
```
openssl pkcs8 -topk8 -inform PEM -outform PEM -in rsa.private -nocrypt > private.pem
```
#### Public Key
```
openssl rsa -inform PEM -in rsa.private -pubout > public.pem
```