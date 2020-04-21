Feature: Update user visited countries information
    Update existing visited countries information for the login user

    Background:
        Given an user with the following attributes
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |

        And with the following visited countries
            | ARM | 2 |
            | BRA | 1 |

        When user already exists

    Scenario: User add a new visited country information
        Given log in user wants to add a new visited country with the following attributes
            | login | country |
            | test  | BGR     |
        When user add a new visited country
        Then the update is 'SUCCESSFUL'

        When user wants to get account details of 'test'
        Then the response is 'SUCCESSFUL'
        And following account details is returned
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |
        And with the following visited countries
            | ARM | 2 |
            | BRA | 1 |
            | BGR | 1 |

    Scenario: User add an information for an already visited country
        Given log in user wants to add a new visited country with the following attributes
            | login | country |
            | test  | BRA     |
        When user add a new visited country
        Then the update is 'SUCCESSFUL'

        When user wants to get account details of 'test'
        Then the response is 'SUCCESSFUL'
        And following account details is returned
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |
        And with the following visited countries
            | ARM | 2 |
            | BRA | 2 |

    Scenario: User remove an already visited country information
        Given log in user wants to remove a visited country with the following attributes
            | login | country |
            | test  | BRA     |
        When user add a new visited country
        Then the update is 'SUCCESSFUL'

        When user wants to get account details of 'test'
        Then the response is 'SUCCESSFUL'
        And following account details is returned
            | id  | login | firstName | lastName | email         | imageUrl        | description    | dateOfBirth | placeOfBirth |
            | 300 | test  | TestFirst | TestLast | test@test.com | http://test.com | TestDesciption | 1990-01-01  | GRL          |
        And with the following visited countries
            | ARM | 2 |
