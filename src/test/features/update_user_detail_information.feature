Feature: Update user detail information
    Update existing detail information for the login user

    Background:
        Given user with the following attributes
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |

        And with the following social platforms
            | facebook  | http://facebook.com/test  |
            | instagram | http://instagram.com/test |

        When user already exists

    Scenario: Update user detail information
        Given user wants to update detail information with the following attributes
            | description       | dateOfBirth | placeOfBirth |
            | TestDesciptionNew | 1990-02-02  | GRL          |
        And with the following social platforms
            | facebook  | http://facebook.com/test  |
            | instagram | http://instagram.com/test |
            | youtube   | http://youtube.com/test   |

        When user update the account with the new detail information
        Then the update is 'SUCCESSFUL'
        And following account details is returned
            | id  | login | firstName | lastName | email         | imageUrl        | description       | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciptionNew | 1990-02-02  | GRL          |
        And with the following social platforms
            | facebook  | http://facebook.com/test  |
            | instagram | http://instagram.com/test |
            | youtube   | http://youtube.com/test   |
