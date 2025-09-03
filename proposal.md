
# **Bookwyrm Capstone Project Proposal**



## **Problem Statement**
A reader wishes to engage more actively in the reading community, but is having trouble finding a centralized
hub for creating or joining online book clubs. Many book clubs are in person, scattered across various websites,
or locked behind paywalls. The user wants a place where they can search through book clubs and find ones that
fit their interests, join scheduled online meetings, and post reviews and discussions asynchronously.

## **Proposed Solution: Book Club App**

This app will serve as a centralized platform for book lovers to find, join, create, and manage book clubs,
while also offering a space for book reviews and discussions.

### **Features**
- **Centralized Book Club Directory:**
    - A searchable and filterable directory of book clubs based on genres, locations (for in-person clubs), and online-only options. Users can easily find clubs that match their interests and availability.
      Club Creation and Management:
    - Users can create new book clubs, set meeting schedules, manage members, and share club information.
    - Group admins can assign featured books for each month, and users can view previous or upcoming months.
- **Integrated Book Review System:**
    - Members can leave reviews for books they’ve read, which can be featured in the club's reading list. Reviews can include ratings, detailed comments, and discussion prompts.
 

- **Discussion:**
    - Each book club will be able to set up a meeting time with their members and meet through either a zoom/google meet link or in person if they choose. Helps for structure and building connections with other members of the group.
    - Clubs will be able to create and contribute to discussion threads, so members can share their thoughts on books asynchronously.

### **Stretch Goals**
**OpenLibraryApi:**
- This API can be utilized by giving us a ton of information about a book with just the user submitting an ISBN.
This information includes author, title, subjects, and even a thumbnail of the cover.

**Hosting:**
- We would host our app through a cloud web hosting service such as google cloud.

## **Glossary**


**1. Book Club:**
A group dedicated to the discussion of books of a certain genre. A club has members that host meetings.

**2. Member:**
A reader associated with a book club. Readers can be a member of multiple clubs

**3. Book:**
A book distinguished by their title, author, and description. A book can be featured by a club as a target of discussion and reviews.

**4. Meeting:**
A gathering of members at a set time and location(if offline). Can have an address if offline and a link to a meeting if online.

**5. Review:**
A rating by a member that consists of a numbered rating and a reasoning for their rating.

**6. Discussion:**
A collection of messages regarding a book within a club.

## **High Level Requirements**
- Create a club(Member, Admin)
- Delete a club(Admin)
- Edit a club(Club Leader)
- Join a club(Member)
- Sign up to be a member(anyone)
- Start a discussion(Member)
- Setup a meeting(member)
- Submit a review(member)
- Feature a book(club leader)

## **User Stories/Scenarios**

**Admin:**
- As an admin
- I want to add, update, remove, or delete clubs, and or their comments in discussion boards
- So I can filter out negative comments and or profanity towards others in the community. Also I would be able to assist other members with editing their club.
  **Member:**
- As a member
- I want to update a password, join a club(s), leave reviews and discuss books with my club
- So I can be secure and connect with other members or be informative about a book

**Create a club**
1. Create a club that members can join
2. Suggested Data
- Name: Harry Potter Pals
- Description: A club to discuss books of the Harry Potter series. Readers of all ages welcome
- Genre: Fantasy
- Location: New York
- Online: checkbox to set if meetings are online/offline
  Precondition: User must be logged in either as a member or admin.
  Post-condition: None

**Set up a Meeting**
1. Choose a time and place if meeting in-person. Choose a time and link if meeting virtually.
   Precondition: User must be logged in either as a member or admin.
   Post-condition: None

**Create a review**
1. Select a book that you would like to review.
2. Enter a rating for the book
3. Enter any additional comments to supplement the review
   Precondition: User must be logged in either as a member or admin.
   Post-condition: None












