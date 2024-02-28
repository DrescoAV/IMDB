# IMDB Application - OOP Project Overview

This application mimics IMDB's functionality, managing a comprehensive database of movies, series, actors, and user interactions. It supports different user roles—Regular Users, Contributors, Administrators—with distinct permissions. The system enables adding, deleting, and updating production and actor details, handling user requests, and more.

## Features

- **User Authentication**: Secure login with role-based access.
- **Production and Actor Management**: Add/remove database entries.
- **Request Management**: Users submit and track requests.
- **User Notifications**: Alerts on updates and changes.
- **Favorites Management**: Personalize favorite lists.
- **Ratings and Reviews**: Community feedback enriches the database.
- **Detailed Views**: Comprehensive production and actor information.

## Implementation Details

- **Class Hierarchy and Inheritance**: For managing user roles.
- **Design Patterns**: Observer for notifications, Strategy for experience, Builder for user's data and Factory for users types creation. 
- **JSON Reading**: Database construction via Jackson library.

## Graphical Interface

- **Custom Design**: Modern, black and orange theme.
- **TikTok-style Scrolling**: Unique browsing experience.
- **Notifications**: Automatic retractable panel and popup alerts.
- **Animations**: Smooth transitions for user interaction.