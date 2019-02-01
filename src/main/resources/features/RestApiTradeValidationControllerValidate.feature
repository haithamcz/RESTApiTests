Feature: REST API Trade Validation Controller - Validate
  The web service validates the trade information for FX Spot, Forward, Options.

  @Common
  Scenario Outline:: validate single sample data - Common
    Given Accessible Sample valid data payload <payload>
    When Send payload <payload> to validate service
    Then Get response HTTP return code <code> from validate service
    And Response message status is <status> from validate service
    And Response message contains <msg>

    Examples:
      | payload                          | code | status  | msg                                          |
      | SinglePayload.json               | 200  | SUCCESS | null                                         |
      | PayloadValueDateVsTradeDate.json | 200  | ERROR   | it has to be after trade date                |
      | PayloadWeekend.json              | 200  | ERROR   | cannot fall on Saturday/Sunday               |
      | PayloadCounterparty.json         | 200  | ERROR   | Supported counterparties: [[PLUTO2, PLUTO1]] |
      | PayloadLegalEntity.json          | 200  | ERROR   | only one legal entity is used: CS Zurich     |
      | PayloadCCY.json                  | 200  | ERROR   | Invalid currency pair [EURXBT]               |
#  value date cannot be before trade date
#  value date cannot fall on weekend or non-working day for currency
#  if the counterparty is one of the supported ones. Supported counterparties (customers) are : PLUTO1,
#  PLUTO2
#  only one legal entity is used: CS Zurich
#  validate currencies if they are valid ISO codes (ISO 4217)

  @SPOT,FORWARD
  Scenario Outline:: validate single sample data - SPOT, FORWARD
    Given Accessible Sample valid data payload <payload>
    When Send payload <payload> to validate service
    Then Get response HTTP return code <code> from validate service
    And Response message status is <status> from validate service
    And Response message contains <msg>

    Examples:
      | payload        | code | status  | msg  |
      | PayloadEU.json | 200  | SUCCESS | null |
#  validate the value date against the product type.

  @Optionsspecific
  Scenario Outline:: validate single sample data - Options specific
    Given Accessible Sample valid data payload <payload>
    When Send payload <payload> to validate service
    Then Get response HTTP return code <code> from validate service
    And Response message status is <status> from validate service
    And Response message contains <msg>

    Examples:
      | payload                         | code | status  | msg                                                                           |
      | PayloadEU.json                  | 200  | SUCCESS | null                                                                          |
      | PayloadUS.json                  | 200  | SUCCESS | null                                                                          |
      | PayloadUSExerciseStartDate.json | 200  | ERROR   | exerciseStartDate has to be after the trade date                              |
      | PayloadAsian.json               | 200  | ERROR   | Invalid option style [ ASIAN ]. Valid option styles are: [AMERICAN, EUROPEAN] |
      | PayloadExpiryDate.json          | 200  | ERROR   | Expiry date 2017-09-21 has to be before delivery date 2017-08-22              |
      | PayloadPremiumDate.json         | 200  | ERROR   | Premium date 2017-09-12 has to be before delivery date 2017-08-11             |
#   the style can be either American or European
#   American option style will have in addition the exerciseStartDate, which has to be after the trade date but before the expiry date
#   expiryDate and premium date shall be before deliveryDate.

  @RegressionTest
  Scenario: Validate batch service
    Given Accessible Sample valid data payload MultiPayload.json
    When Send payload MultiPayload.json to validate batch service
    Then Get response HTTP return code 200 from validate batch service
    And Response message status should not contain ERROR from validate batch service
