Feature: Get user
    Get user details from the platform

    Background:
        Given user with the following attributes
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |

        When user already exists

    Scenario: Get by login
        When user wants to get account details of 'test'
        Then the response is 'SUCCESSFUL'
        And following account details is returned
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |


    Scenario: Get by not existing login
        When user wants to get account details of 'not_exist'
        Then the response is 'FAIL'


    Scenario: Get current
        When user 'test' is login and wants to see its account details
        Then the response is 'SUCCESSFUL'
        And following account details is returned
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |
