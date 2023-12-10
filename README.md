
# Mutual Fund Porfolio Tracker

A Spring Boot REST API service that helps you store your mutual fund data and generate a report of how your investments are doing.


## Table of Contents
- [Authentication](#authentication)
- [Database](#database)
- [Usage](#usage)
  - [API Endpoints](#api-endpoints)
    - [Create Mutual Fund Entry](#create-mutual-fund-entry)
    - [Update Mutual Fund Entry](#update-mutual-fund-entry)
    - [Delete Mutual Fund Entry](#delete-mutual-fund-entry)
    - [Read All Mutual Funds](#read-all-mutual-funds)
    - [Read Specific Mutual Fund](#read-specific-mutual-fund)
    - [Generate Mutual Fund Report](#generate-mutual-fund-report)


## Authentication
The project uses Spring Security for OAuth2 authentication. 
The OAuth2 provider is Google. We need to authenticate via Google to access the API.


## Database
The application is connected to a MongoDB cluster for data storage.



## API Endpoints

#### Create Mutual Fund Entry

- **Endpoint:** `mutualFundData/create`
- **Method:** `POST`
- **Body Format:** `application/json`
- **JSON Format:**
  ```json
  {
    "mutualFundId": "122639",
    "mutualFundName": "Parag Parikh Flexi Cap Fund - Direct Plan - Growth",
    "investedUnits": 344.6,
    "investedValue": 19749
  }
  
- **Description:** Creates an entry for the provided mutual fund.

#### Update Mutual Fund Entry

- **Endpoint:** `mutualFundData/update`
- **Method:** `POST`
- **Body Format:** `application/json`
- **JSON Format:**
  ```json
  {
    "mutualFundId": "122639",
    "mutualFundName": "Parag Parikh Flexi Cap Fund - Direct Plan - Growth",
    "investedUnits": 344.6,
    "investedValue": 19749
  }
  ```
- **Description:** Updates the already present entry.

#### Delete Mutual Fund Entry

- **Endpoint:** `mutualFundData/delete/{mutualFundCode}`
- **Type:** `POST`
- **Description:** Deletes the already present entry.

#### Read All Mutual Funds

- **Endpoint:** `mutualFundData/readAll`
- **Description:** Gives data about all the mutual funds present.

#### Read Specific Mutual Fund

- **Endpoint:** `mutualFundData/read/{mutualFundCode}`
- **Description:** Gives data about a particular mutual fund.

#### Generate Mutual Fund Report

- **Endpoint:** `mutualFundData/getMutualFundReport`
- **Description:** Generates a mutual fund report.
