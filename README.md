# TransactionStats
A spring boot application which fetches transaction stats in O(1) time.

# Steps to build and run the tests
./gradlew clean build

# Step to run the application 
./gradlew bootRun


# API Endpoints exposed :
a) The below endpoint is used to add transactions.
``` 
/api/v1/transactions 
``` 

b) The below endpoint is used to get transactions statistics for the last 60 seconds.
``` 
/api/v1/stats
``` 
