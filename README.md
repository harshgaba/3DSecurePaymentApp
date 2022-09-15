# 3DSecurePaymentApp

An app screen that accepts card details input
It include the following elements:
● an input field for card number
● an input field for expiration date
● an input field for cvv
● a pay button

Upon completing the card details, clicking the pay button should trigger a HTTP POST
request to our sample back end API:
URL: https://integrations-cko.herokuapp.com/pay

Different card schemes (Visa, Amex) have different ways of formatting the cards and
different validation requirements. Potentially consider:
● Formatting the card input field
● Doing validation based on the card type
● Doing date validations
