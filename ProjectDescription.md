# Project – Book Reviews

## Brief description:
Create Web application that will allow users to add reviews to books.

## Main system functions:
- **Home** – homepage will show all available books (table of books) that can be reviewed. Each book will have fields like: Title, Publish Year, Author Name, Rating
- **Adding new book into database** – can be done by administrator
- **Adding book review** – can be done by registered user (a registered user will leave a review with their first name), OPTIONAL FEATURE: A non-registered user will leave an anonymous review.
- **Deleting book review** – can be done by administrator (any review) or user (only own reviews, when registered).

## Entities: (Proposal)
- **Book**
- **User**
- **Review**

## Mapping:
- `Book -> Review (@OneToMany)`
- `Review -> Book (@ManyToOne)`

- `User -> Review (@OneToMany)`
- `Review -> User (@ManyToOne)`

## Fields:
**Book:**
- `bookId`
- `title`
- `publishYear`
- `authorName`
- `isbn`
- `overallRating`

**Review:**
- `reviewId`
- `text`
- `rating` (from 1 to 10)
- `bookId`
- `userId`

**User:**
- `userId`
- `firstName`
- `lastName`
- `email`
- `password`

## Additional/Optional features:
- Ability to review movies, etc.

## Technologies
- **Backend**: Spring + Hibernate, JWT
- **Frontend**: React
