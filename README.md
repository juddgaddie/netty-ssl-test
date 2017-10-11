#Testing Netty SSL

How to create a stub server to connect to:


```
openssl s_server -Verify 1 -accept 12345 -CAfile ca.pem -cert server.pem -key server.key -cipher 'ECDHE-RSA-AES256-GCM-SHA384'


```
Generating Keys

Generating Client/Server certificates with a local CA 

Generate a CA 
1)    openssl req -out ca.pem -new -x509 
        -generates CA file "ca.pem" and CA key "privkey.pem"

Generate server certificate/key pair 
        - no password required. 
2)    openssl genrsa -out server.key 1024 
3)    openssl req -key server.key -new -out server.req 
4)    openssl x509 -req -in server.req -CA CA.pem -CAkey privkey.pem -CAserial file.srl -out server.pem 
        -contents of "file.srl" is a two digit number.  eg. "00"

Generate client certificate/key pair

5)    Either choose to encrypt the key(a) or not(b) 
        a. Encrypt the client key with a passphrase 
            openssl genrsa -des3 -out client.key 1024 
        b. Don't encrypt the client key 
            openssl genrsa -out client.key 1024 
6)    openssl req -key client.key -new -out client.req 
7)    openssl x509 -req -in client.req -CA CA.pem -CAkey privkey.pem -CAserial file.srl -out client.pem 
        -contents of "file.srl" is a two digit number.  eg. "00"

8)    DONE



