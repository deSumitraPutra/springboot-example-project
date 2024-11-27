# Spring Boot Standardize Exception Response

In this branch i try to explore how we standardize exception response through all the service.

### Key Points

* ExceptionPolicy: the least amount of things included in error response body.
* BusinessExceptionPolicy: Business logic error response field
* ApplicationExceptionPolicy: Application error response
* BusinessException: Business logic exception that can be extended to make custom exception(e.g BookNotFoundException)

