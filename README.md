# Java Practical Test Assignment

This project is a Java-based test assignment that implements user management functionality with Spring Boot.

## Requirements To The Test Assignment

1. **Fields:**
    - Email (required): Validates against email pattern.
    - First name (required).
    - Last name (required).
    - Birth date (required): Value must be earlier than current date.
    - Address (optional).
    - Phone number (optional).

2. **Functionality:**
    - Create user: Registers users who are more than 18 years old (age threshold configurable in properties file).
    - Update user fields: Supports updating individual or all user fields.
    - Delete user.
    - Search for users by birth date range: Validates that "From" date is less than "To" date, returning a list of objects.

3. **Unit Tests:** Code is covered by unit tests using Spring framework.

4. **Error Handling:** Code has error handling for REST API.

5. **API Responses:** Responses are in JSON format.

6. **Database:** Data persistence layer is not required.

7. **Spring Boot:** Any version of Spring Boot can be used, with Java version of your choice.

8. **Spring Initializr:** You can use Spring Initializr utility to create the project.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Lahutina/TestAssigment
