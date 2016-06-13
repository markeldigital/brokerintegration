# Broker Integration

[![Build Status](https://travis-ci.org/markeldigital/brokerintegration.svg?branch=master)](https://travis-ci.org/markeldigital/brokerintegration)

## Dev Server Setup

### Dependencies

- Oracle JDK (>= 1.7)
- Maven (>= 3.3.9)

### Running

1. GIT Clone the repository located at: https://github.com/markeldigital/brokerintegration.git.
2. Go to the root folder of the newly cloned repository.
3. Execute `mvn package`.
4. Execute `java -jar devserver/target/devserver-0.0.0-SNAPSHOT.jar server`.
5. Connect to the running service by making a request to: http://localhost:8080/policy-reference.
6. You should see something like the following:

```javascript
{
    "number":"",
    "timezone":"UTC",
    "reference":"",
    "expiry":0,
    "inception":0,
    "insured":{
        "reference":"",
        "fullname":"",
        "email":"",
        "mailingAddress":{
            "city":"",
            "country":"",
            "lines":[],
            "province":"",
            "postcode":""
        }
    }
}
```


## User Flow

![Quote Journeys](QuoteJourneys.png)

## Systems Integration

![Systems Integration](SystemsIntegration.png)

## Terminology

<dl>
  <dt>Pre-shared key (PSK)</dt>
  <dd>This is a key that is used to validate the authenicity of the request and should be treated as a private secret that only Markel and the broker have visibility of. It *MUST NOT* be made visible in the clients browser.</dd>
  <dt>Signature</dt>
  <dd>Signature is a token that is a computed value using the field values and the PSK. It is a method to transfer values between systems ensuring no intermediaries have tampered with the values.</dd>
</dl>

## Security

All data is to be transferred securely using HTTPS/TLS encryption between the broker and Markel's systems. Authenticating the data will be achieved using HMAC signing with a pre-shared key (PSK).

### JSON POST

Method: POST

<pre><code>
https://${urlbase}?ts=${epoch}&sig=${signature}&v=1

// Body
{
    "number": ${policy_number},
    "timezone": ${timezone},
    "reference": ${policy_reference_number},
    "expiry": ${policy_expiry},
    "inception": ${policy_inception},
    "insured":{
        "reference": ${customer_reference_number},
        "fullname": ${customer_fullname},
        "email": ${customer_email},
        "mailingAddress":{
            "city": ${customer_mailing_city},
            "country": ${customer_mailing_country},
            "lines":[${customer_mailing_line_0},${customer_mailing_line_1},...],
            "province":${customer_mailing_province},
            "postcode": ${customer_mailing_postcode}
        }
    }
}

// signature

base64(sha256hmac(${psk}, ${customer_mailing_city}+${customer_mailing_country}+${customer_reference_number}+${policy_expiry}+${policy_inception}+${customer_email}+${customer_fullname}+${customer_mailing_line_0}+${customer_mailing_line_1}+${policy_number}+${policy_reference_number}+${customer_mailing_postcode}+${customer_mailing_province}))
</code></pre>
