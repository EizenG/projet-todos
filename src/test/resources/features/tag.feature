# Par preference nous allons rester sur la langue anglaise pour suivre la logique de cette application

# language: en

Feature: API to manage Tag Items

  All the CRUD operation related to the tags here

  # find all operation
  @REFFROUPE5-00001
  Scenario: Find All Should Return Correct List
    Given acicd_tags table contains data:
      | id                                   | name  | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag 1 | Description tag 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | tag 2 | Description tag 2 |
    When call find all tags with page = 0 and size = 5 and sort="name,asc"
    Then the returned http tags status is 200
    And the returned tags list has 2 elements
    And  that tags list contains values
      | id                                   | name  | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag 1 | Description tag 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | tag 2 | Description tag 2 |


  @REFFROUPE5-00002
  Scenario Outline: Find all with pageable should return correct list
    Given acicd_tags table contains data:
      | id                                   | name  | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag 1 | Description tag 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | tag 2 | Description tag 2 |
    When call find all tags with page = <page> and size = <size> and sort="name,asc"
    Then the returned http tags status is 200
    And the returned list has <returned_list_size> elements
    And that tags list contains tags with name="<name>" and description="<description>"
    Examples:
      | page | size | returned_list_size | name  | description       |
      | 0    | 1    | 1                  | tag 1 | Description tag 1 |
      | 1    | 1    | 1                  | tag 2 | Description tag 2 |
      | 2    | 2    | 0                  |       |                   |

  @REFFROUPE5-00003
  Scenario Outline: Find all with sorting should return correct list
    Given acicd_tags table contains data:
      | id                                   | name   | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | name 1 | description Tag 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | name 2 | description Tag 2 |
    When call find all tags with page = <page> and size = <size> and sort="<sort>"
    Then the returned http tags status is 200
    And the returned tags list has <returned_list_size> elements
    And that tags list contains tags with name="<name>" and description="<description>"
    Examples:
      | page | size | sort      | returned_list_size | name   | description       |
      | 0    | 1    | name,asc  | 1                  | name 1 | description Tag 1 |
      | 0    | 1    | name,desc | 1                  | name 2 | description Tag 2 |

  # find by id operation
  @REFFROUPE5-00004
  Scenario Outline: Find tag by id  should return correct tag
    Given acicd_tags table contains data:
      | id                                   | name  | description   |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag 1 | description 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | tag 2 | description 2 |
    When call find tag by id with id="<id>"
    Then the returned http tags status is 200
    And the returned tag has properties name="<name>", description="<description>"
    Examples:
      | id                                   | name  | description   |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag 1 | description 1 |

  @REFFROUPE5-00005
  Scenario Outline: Find by id with a non-existing id should return 404
    Given acicd_tags table contains data:
      | id                                   | name  | description   |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag 1 | description 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | tag 2 | description 2 |
    When call find tag by id with id="<bad_id>"
    Then the returned http tags status is 404
    Examples:
      | bad_id                               |
      | 27a281a6-0882-4460-9d95-9c28f5852db1 |
      | 28a281a6-0882-4460-9d95-9c28f5852db1 |

  # add tag operation
  @REFFROUPE5-00006
  Scenario Outline: Add tag should return 201
    Given acicd_tags table contains data:
      | id                                   | name  | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | Tag 1 | Description Tag 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | Tag 2 | Description Tag 2 |
    And tag name = "<name>"
    And tag description = "<description>"
    When call add tag
    Then the returned http tags status is 201
    And the created tag has properties name="<name>", description="<description>"
    Examples:
      | name  | description       |
      | Tag 3 | Description Tag 3 |
      | Tag 4 | Description Tag 4 |

  @REFFROUPE5-00007
  Scenario: Add tag with an existing name should return 409
    Given acicd_tags table contains data:
      | id                                   | name  | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | Tag 1 | Description Tag 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | Tag 2 | Description Tag 2 |
    When tag name = "Tag 1"
    And tag description = "Description Tag 1.1"
    When call add tag
    Then the returned http tags status is 409

  @REFFROUPE5-00008
  Scenario: add tag with name exceeding 50 characters should return 400
    Given tag name contains 51 characters
    And tag description contains 255 characters
    When call add tag
    Then the returned http tags status is 400

  @REFFROUPE5-00009
  Scenario: add tag with name less than 2 characters should return 400
    Given tag name contains 1 characters
    And tag description contains 255 characters
    When call add tag
    Then the returned http tags status is 400

  @REFFROUPE5-000010
  Scenario: add tag with description exceeding 255 characters should return 400
    Given tag name contains 50 characters
    And tag description contains 256 characters
    When call add tag
    Then the returned http tags status is 400

  #uptade tag operation
  @REFFROUPE5-000011
  Scenario Outline: update an existing tag should return 202
    Given acicd_tags table contains data:
      | id                                   | name       | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag name 1 | tag description 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | tag name 2 | tag description 2 |
    And tag name = "<name>"
    And tag description = "<description>"
    When call update tag with id="<id>"
    Then the returned http tags status is 202
    And the updated tag has properties name="<name>", description="<description>"
    Examples:
      | id                                   | name         | description         |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag name 1.1 | tag description 1.1 |

  @REFFROUPE5-000012
  Scenario: update a non existing tag should return 404
    Given tag name = "tag name 1"
    And tag description = "new description"
    When call update tag with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http tags status is 404

  @REFFROUPE5-000013
  Scenario: update tag with name exceeding 50 characters should return 400
    Given acicd_tags table contains data:
      | id                                   | name   | description   |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | name 1 | description 1 |
    And tag name contains 51 characters
    And tag description contains 255 characters
    When call update tag with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http tags status is 400

  @REFFROUPE5-000014
  Scenario: update tag with name less than 2 characters should return 400
    Given acicd_tags table contains data:
      | id                                   | name   | description   |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | name 1 | description 1 |
    And tag name contains 1 characters
    And description contains 255 characters
    When call update tag with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http tags status is 400

  @REFFROUPE5-000015
  Scenario: update tag with description exceeding 255 characters should return 400
    Given acicd_tags table contains data:
      | id                                   | name   | description   |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | name 1 | description 1 |
    And tag name contains 50 characters
    And description contains 256 characters
    When call update tag with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http tags status is 400

  #delete tag operation
  @REFFROUPE5-000016
  Scenario: delete an existing tag should return 204
    Given acicd_tags table contains data:
      | id                                   | name       | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag name 1 | tag description 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | tag name 2 | tag description 2 |
    When call delete tag with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http tags status is 204

  @REFFROUPE5-000017
  Example: delete a non existing tag should return 404
    Given acicd_tags table contains data:
      | id                                   | name       | description       |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | tag name 1 | tag description 1 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | tag name 2 | tag description 2 |
    When call delete tag with id="27a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http tags status is 404




#find all
#find by id
#add
#update
#delete
