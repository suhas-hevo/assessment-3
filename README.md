# Assessment3

## Problem Statement:

Storing contacts locally on a certain device limits accessibility and portability of contacts information required by a user constraining the user to one device to manage and access contacts data. 
Storing it on a server/cloud account which can be interacted with by multiple devices brings in the advantage of increased accessibility, portability of contacts info and lesser local hardware requirement. This would thus enable a user to login from any device and manage their contacts information.


## Functional Requirements

1. Create Contact
Description:- The system should allow a user to save a new contact by specifying the required fields in the body of request.

Input : Relevant fields such as id,first name, last name and email id (all fields are compulsory).

The input is validated against constraints, 
if not satisfactory server must return Response - 400 Bad Request.
If valid the record must be inserted into databse  and server must respond with Response code - 201.

2. Retrieve All Contacts
Description:- The system should allow a user to retrieve and view all contacts saved.

The User can make a GET query to the system and the system must return all the records saved by the user.

3. Retrieve a Contact by name:
Description:- The system should allow a user to retrieve and view contacts records by specifying the firstname or lastname or both firstname and lastname.

Input : The user can specify either firstname or lastname or both firstname and lastname.

The query parameters sent by the user are checked
if the user query specifies both first and last name, then all records with matching first and last names are returned
if the user query specifies only first then all records with matching first are returned.
if the user query specifies only last then all records with matching last are returned.


4. Edit Contact
Description:- The system should allow a user to edit information of any contact record(i.e., change fields like name, email_id etc.,).

Input : contact id,first name, last name and email id

The input parameters in the body( id,first name, last name and email id) are each validated against a set of constraints 
if not satisfactory server must return Response - 400 Bad Request.
If satisfactory the system must check if a record exists for the specified id
- If no matching record exists server must return Response - 400 Bad Request
- If it does exist then parameters such as first name, last name and email must be modified for the matching record in the database.

5. Delete Contact
Description:- The system should allow a user to delete any specific contact record by specifying the contact id.

Input: The user must specify the contact id.

The contact id of the user is validated to check if a contact exists with the given id-
If no contact exists then the system should respond with response code - 400
If a contact does exist with the specified id, the record must be deleted and server must return response code - 200









## Security Requirements

The system should use Basic authentication to authenticate users.

The system must allow access to only authenticated valid users to use authorised resources only.
Authorization is done based on roles.

The system must ensure that a user should not be able to access or manipulate another user’s contacts.
(For Example : 
A with user-id = 1 must be allowed to access only URL’s that the user is authorized to such as - user/1/* and should be prevented from accessing any other user’s URLs such as - user/2/*,user/3/* etc.,)
