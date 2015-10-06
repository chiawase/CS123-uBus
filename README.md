# CS123-uBus
> Requirement for CS 123: Intro to Software Engineering

The Bus Passenger Queuing System is a system that automates the process of reserving bus seats and paying bus tickets. Its objectives are as follows:

- to smoothen the process of buying bus tickets;
- to provide the commuters schedules of available provincial buses at the convenience of their homes;
- to allow the commuters to reserve bus seats ahead of time;
- to allow the commuters to perform payment transactions online; and
- to ensure a smooth and efficient management of bus commuters (for management).

The system can only be implemented to provincial buses. It works on the assumption that all buses will be on schedule. Separate buses must be provided for aforementioned reserved seats.

The system will cater to three users: **the commuters**, **the management**.

For **the commuters**, with this system, they will be able to view available provincial bus schedules, reserve bus seats on their chosen schedule, and pay for these reservations, all on the comfort of simply using their phones. Upon installing this mobile application, commuters are required to share their full name and phone number to create their own, unique account. Each account is provided an account number and an initial balance of Php 0.00. Loading money into the account can be done through designated loading stations.

For **the managers**, with this system, they will be provided with a web application that will allow them to continuously update the buses’ schedule for the commuters to access, and, subsequently, allow to manage these commuters and the buses assigned to them to ensure a smoother process. The management application is also responsible for loading money into the commuter’s accounts.

Ideally, there must be a separate system for loading as, in the real-world setting, the management is not necessarily responsible for load transaction. However, for the sake of optimization, this feature will be included in the management’s web application.

### Minimum Viable Product:
This system should at least provide a way:
- create separate accounts for users
- to reserve bus seats for the commuters
- for the management to change/update the schedules of buses
to load users’ accounts

---

## Components

# Main Mobile App

**Platform**: Native Android
**Will contain**: reservation of bus seats, checking of load balance, having a user account (attached to the user's cellphone number)

# Web App

The main interface that the management will use in interacting with the bus schedules and addition of load in user accounts.
