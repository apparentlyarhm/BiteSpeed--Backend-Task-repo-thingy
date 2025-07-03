### This pretty much attempts to follow the instructions given in the docs.

---
## Why google cloud?

Well i already have other apps deployed right now so much of the ps scripts were reused here

Well nothing more to say.

don't forget to check the `/misc/swagger-ui/index.html#/` endpoint to actually see the main endpoint details

---

**as of writing this readme the hostname is:**

`https://ihavenoclue-501405668172.asia-south1.run.app`

---

## For Convenience

use postman (or curl if cultured or `Invoke-Request` if super cultured) to test the endpoint and send the following data in order

send these in order
 1. primary and secondary merge

```json
{
    "email": "lorraine@hillvalley.edu", 
    "phoneNumber": "123456"
}
```

```json
 {
     "email": "mcfly@bitespeed.com",
     "phoneNumber": "123456"
 }
```

 2. Identity Reconcilation

```json
 {
     "email": "george@hillvalley.edu",
     "phoneNumber": "919191"
 }
```
```json
 {
     "email": "biffsucks@bitespeed.com",
     "phoneNumber": "717171"
 }
```

```json
{
    "email": "george@hillvalley.edu",
    "phoneNumber": "717171"
}
```
I'm pretty sure there are bugs tho