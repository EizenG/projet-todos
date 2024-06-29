#see https://cucumber.io/docs/gherkin/reference/
# see https://cucumber.io/docs/gherkin/languages/
# language: en

# cucumber
Feature: API to manage Todo Items

  @REFEPTGITDIC12024-00001
  Scenario Outline: Find by id should return correct entity
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|true       |
    When call find by id with id="<id>"
    Then the returned http status is 200
    And the returned todo has properties title="<title>",description="<description>" and completed="<completed>"
    Examples:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |


    #examples = boucles
  @REFEPTGITDIC12024-00002
  Scenario Outline: Find by id with an non existing id should return 404
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    When call find by id with id="<bad_id>"
    Then the returned http status is 404
    Examples:
      |bad_id                              |
      |27a281a6-0882-4460-9d95-9c28f5852db1|
      |28a281a6-0882-4460-9d95-9c28f5852db1|


  @REFEPTGITDIC12024-00003
  Scenario: Find all should return correct list
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|true       |
    When call find all with page = 0 and size = 10 and sort="title,asc"
    Then the returned http status is 200
    And the returned list has 2 elements
    And that list contains values:
      | title   | description   | completed  |
      | title 1 | description 1 | false      |
      | title 2 | description 2 | true       |

  @REFEPTGITDIC12024-00004
  Scenario Outline: Find all with pageable should return correct list
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|true       |
    When call find all with page = <page> and size = <size> and sort="title,asc"
    Then the returned http status is 200
    And the returned list has <returned_list_size> elements
    And that list contains todo with title="<title>" and description="<description>" and completed="<completed>"
    Examples:
      |page| size  | returned_list_size | title   | description   | completed |
      |0   | 1     |  1                 | title 1 | description 1 | false     |
      |1   | 1     |  1                 | title 2 | description 2 | true      |
      |1   | 3     |  0                 |         |               |           |
      |2   | 1     |  0                 |         |               |           |


  @REFEPTGITDIC12024-00005
  Scenario Outline: Find all with sorting should return correct list
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|true       |
    When call find all with page = <page> and size = <size> and sort="<sort>"
    Then the returned http status is 200
    And the returned list has <returned_list_size> elements
    And that list contains todo with title="<title>" and description="<description>" and completed="<completed>"
    Examples:
      |page| size  | sort      | returned_list_size  | title   | description   | completed |
      |0   | 1     |title,asc  |  1                  |title 1  | description 1 | false     |
      |0   | 1     |title,desc |  1                  |title 2  | description 2 | true      |


  @REFEPTGITDIC12024-00006
  Scenario: delete an existing todo should return 204
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
   When call delete with id="17a281a6-0882-4460-9d95-9c28f5852db1"
   Then the returned http status is 204

  @REFEPTGITDIC12024-00007
  Example: delete an non existing todo should return 404
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed   |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    When call delete with id="27a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http status is 404

  @REFEPTGITDIC12024-00008
  Scenario: complete an existing todo should return 202
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    When call complete with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http status is 202
    And the completed todo has property completed="true"


  @REFEPTGITDIC12024-00009
  Scenario: complete an non existing todo should return 404
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
    When call complete with id="27a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http status is 404

    #Template and Outline are synonumous
  @REFEPTGITDIC12024-00010
  Scenario Template: add todo should return 201
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    And title = "<title>"
    And  description = "<description>"
    When call add todo
    Then the returned http status is 201
    And the created todo has properties title="<title>", description="<description>", completed="<completed>"
    Examples:
      |title    |description    |completed  |
      |title 11 |description 11 |false      |
      |title 12 |description 12 |false      |

  @REFEPTGITDIC12024-00011
  Scenario: add todo with an existing title should return 409
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false     |
    When title = "title 1"
    And  description = "description 1.1"
    When call add todo
    Then the returned http status is 409

  @REFEPTGITDIC12024-00012
  Scenario Outline: update an existing todo should return 202
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false     |
    And  title = "<title>"
    And  description = "<description>"
    When call update todo with id="<id>"
    Then the returned http status is 202
    And the updated todo has properties title="<title>", description="<description>", completed="<completed>"
    Examples:
      |id                                   |title     |description     |completed|
      |17a281a6-0882-4460-9d95-9c28f5852db1 |title 1.1 |description 1.1 | false   |

  @REFEPTGITDIC12024-00013
  Scenario: update an non existing todo should return 404
    Given title = "title 1"
    And  description = "description 1"
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http status is 404

  @REFEPTGITDIC12024-00014
  Scenario: add todo with title exceeding 80 characters should return 400
    Given title contains 81 characters
    And description contains 255 characters
    When call add todo
    Then the returned http status is 400

  @REFEPTGITDIC12024-00015
  Scenario: add todo with title less than 2 characters should return 400
    Given title contains 1 characters
    And description contains 255 characters
    When call add todo
    Then the returned http status is 400

  @REFEPTGITDIC12024-00016
  Scenario: add todo with description exceeding 255 characters should return 400
    Given title contains 50 characters
    And description contains 256 characters
    When call add todo
    Then the returned http status is 400

  @REFEPTGITDIC12024-00017
  Scenario: update todo with title exceeding 80 characters should return 400
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
    And title contains 81 characters
    And description contains 255 characters
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http status is 400

  @REFEPTGITDIC12024-00018
  Scenario: update todo with title less than 2 characters should return 400
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
    And title contains 1 characters
    And description contains 255 characters
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http status is 400

  @REFEPTGITDIC12024-00019
  Scenario: update todo with description exceeding 255 characters should return 400
    Given acicd_todos table contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
    And title contains 50 characters
    And description contains 256 characters
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the returned http status is 400
