# Desktop client for the TCP authentication project

Java desktop client for my university course project on registration and authentication over TCP sockets.

The client connects directly to two backend services:

- authentication service on TCP port 9090;
- user/profile service on TCP port 9091.

It sends JSON requests over sockets and keeps the access/refresh token state on the client side.

## Features

- registration and login;
- JWT session handling;
- profile view/edit flow;
- separate socket clients for auth and user operations;
- simple Swing UI.

## Stack

Java, Swing, Gradle, Jackson/JSON and TCP sockets.

Backend repository: `kurs-bntu-backend`.

## Status

Finished as part of the same university course project. The UI is intentionally simple — the main purpose of the project was client-server networking and authentication rather than desktop design.
