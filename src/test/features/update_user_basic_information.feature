Feature: Update user basic information
    Update existing basic information for an user account

    Background:
        Given users with the following attributes
            | id  | login | firstName  | lastName  | email           | imageUrl         | description     | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst  | TestLast  | test@test.com   | http://test.com  | TestDesciption  | 1990-01-01  | GRL          |
            | 400 | other | OtherFirst | OtherLast | other@other.com | http://other.com | OtherDesciption | 1990-01-01  | GRL          |

        When users already exists

    Scenario Outline: Update user basic information <testCase> <expectedResult>
        Given user wants to update basic information with the following attributes
            | login   | firstName   | lastName   | email   | imageUrl   |
            | <login> | <firstName> | <lastName> | <email> | <imageUrl> |
        When user update the account with the new basic information '<testCase>'
        Then the update is '<expectedResult>'
        Examples:
            | testCase                 | expectedResult | login   | firstName    | lastName    | email            | imageUrl           |
            | WITH ALL FIELDS          | SUCCESSFUL     | test123 | TestFirst123 | TestLast123 | test123@test.com | http://test123.com |
            | WITH ALL REQUIRED FIELDS | SUCCESSFUL     | test123 |              |             | test123@test.com |                    |
            | WITHOUT EMAIL            | FAIL           | test123 |              |             |                  |                    |
            | WITH INVALID EMAIL       | FAIL           | test123 |              |             | test&test<com    |                    |
            | WITH EXISTING EMAIL      | FAIL           | test123 |              |             | other@other.com  |                    |
            | WITHOUT LOGIN            | FAIL           |         |              |             | test@test.com    |                    |
            | WITH INVALID LOGIN       | FAIL           | test$&  |              |             | test@test.com    |                    |
            | WITH EXISTING LOGIN      | FAIL           | other   |              |             | test@test.com    |                    |
            | WITHOUT ALL FIELDS       | FAIL           |         |              |             |                  |                    |
