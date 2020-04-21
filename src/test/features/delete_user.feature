Feature: Delete user
    Delete user account details with login identifier

    Background:
        Given an user with the following attributes
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |

        When user already exists

    Scenario: Delete by login
        When admin wants to delete account details of 'test'
        Then the delete is 'SUCCESSFUL'
