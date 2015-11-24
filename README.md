# Broker Integration

## User Flow

iframe or link

## Terminology

<dl>
  <dt>Pre-shared key</dt>
  <dd>This is a key that is used to validate the authenicity of the request and should be treated as a private secret that only Markel and the broker have visibility of.</dd>
</dl>

## Security

All data is to be transferred securely using HTTPS/TLS encryption between the broker and Markel's systems. Authenticating the data will be achieved using signing with a 32 character pre-shared key (PSK).

### User Data Transfer

Method: POST

<pre><code>
https://${host}/product/officepack/?sig=${signature}

// Body
{
  “policy”: ${policy_number},
  “first_name”: ${first_name},
  “middle”: ${middle_name},
  “surname”: ${surname},
  “email”: ${email},
  “business_telephone”: ${business_telephone},
  “business_fax”: ${business_fax},
  “residental_address”: {
    “line1”: ${line1},
    “line2”: ${line2},
    “city”: ${city},
    “province”: ${province},
    “post_code”: ${post_code}
  },
  “mailing_address”: {
    “line1”: ${line1},
    “line2”: ${line2},
    “city”: ${city},
    “province”: ${province},
    “post_code”: ${post_code}
  },
  “correspondence_locale”: ${correspondence_locale}
}

// signature

base64(sha256(${psk}+${policy_number}+${first_name}+${middle}+${surname}+${email}+${business_telephone}+${business_fax}+${residential_address::line1}+${residential_address::line2}+${residential_address::city}+${residential_address::province}+${residential_address::post_code}+${mailing_address::line1}+${mailing_address::line2}+${mailing_address::city}+${mailing_address::province}+${mailing_address::post_code}+${correspondence_locale}))
</code></pre>

#### HTTP Status Codes

* 201 Created - Reference data saved correctly.
* 400 Bad Request - do not retry the pre shared key needs to be updated.
* 410 Gone - session has expired do not retry.
* 5xx Server Error - retry later.

## Quote Journey

Method: GET

<pre><code>
https://${host}/product/officepack/?ts=${epoch}&policyId=${policy_number}&locale=${locale}&sig=${signature}

// Signature

base64(sha256(${psk}+${epoch}+${policy_number}+${locale}))
</code></pre>

### HTTP Status Codes

* 201 Created - Reference data saved correctly.
* 400 Bad Request - do not retry the pre shared key needs to be updated.
* 410 Gone - session has expired do not retry.
* 5xx Server Error - retry later.
