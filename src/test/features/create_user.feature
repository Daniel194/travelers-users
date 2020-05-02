Feature: Create user account
    Create user account based on information received from Gateway

    Background:
        Given user with the following attributes
            | id  | login | firstName  | lastName  | email           | imageUrl         | description     | dateOfBirth | placeOfBirth |
            | 400 | other | OtherFirst | OtherLast | other@other.com | http://other.com | OtherDesciption | 1990-01-01  | GRL          |

        When user already exists

    Scenario Outline: Create user account <testCase> <expectedResult>
        Given user wants to create an account with the following attributes
            | login   | firstName   | lastName   | email   | imageUrl   |
            | <login> | <firstName> | <lastName> | <email> | <imageUrl> |
        When user save the new account '<testCase>'
        Then the save is '<expectedResult>'
        Examples:
            | testCase                 | expectedResult | login  | firstName  | lastName  | email           | imageUrl         |
            | WITH ALL FIELDS          | SUCCESSFUL     | test1  | TestFirst1 | TestLast1 | test1@test.com  | http://test1.com |
            | WITH ALL REQUIRED FIELDS | SUCCESSFUL     | test2  |            |           | test2@test.com  |                  |
            | WITHOUT EMAIL            | FAIL           | test3  |            |           |                 |                  |
            | WITH INVALID EMAIL       | FAIL           | test4  |            |           | test&test<com   |                  |
            | WITH EXISTING EMAIL      | FAIL           | test5  |            |           | other@other.com |                  |
            | WITHOUT LOGIN            | FAIL           |        |            |           | test@test.com   |                  |
            | WITH INVALID LOGIN       | FAIL           | test$& |            |           | test@test.com   |                  |
            | WITH EXISTING LOGIN      | FAIL           | other  |            |           | test@test.com   |                  |
            | WITHOUT ALL FIELDS       | FAIL           |        |            |           |                 |                  |
