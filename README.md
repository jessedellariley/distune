# distune
A music discovery app with a dating-app-style user matching system based on shared genres, songs, and artists.
The goal is to get music recommendations from real people with shared taste.

## Table of Contents
1. [Product Spec](#product-spec)
2. [Navigation](#wireframes)
3. [Schema](#schema)

## Product Spec

### 1. User Stories

- [x] User signs in through Spotify to access their account
- [x] User picks their top 3 songs for each of their favorite genres
- [x] User can discover new users to follow by swiping right or left on their top 3 songs
- [x] User can display profile information (bio, following, followers)
- [x] User's playlists appear in profile and can be clicked on and listened to
- [x] User can access the music and profiles of those they are following
- [x] User can log out

### 2. Screen Archetypes

* Spotify login
* Discover
   * Allows user to discover other users with similar music taste by swiping right or left
* Profile
   * Allows user to view their playlists, bio, followers, and following. Also allows user to edit their top 3 songs for each of their favorite genres
* Followers
   * Allows user to view the profiles of those following them
* Following
   * Allows user to view the profiles of users they are following

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Discover
* Profile
* Logout

**Flow Navigation** (Screen to Screen)

* Discover
   * If swipe right, choice between continuing to discover or going to that user's profile
* Profile
   * Link to edit profile
   * Link to followers page
   * Link to following page
* Followers
   * List of users where each item is a link to that user's profile
* Following
   * List of users where each item is a link to that user's profile

## Schema 
### Models
#### User

   | Property       | Type     | Description |
   | -------------  | -------- | ------------|
   | objectId       | String   | unique id for the user (default field) |
   | name           | String   | user's full name |
   | username       | String   | username |
   | accessToken    | String   | Spotify access token for HTTP requests |
   | image          | File     | profile image |
   | followersCount | Number   | number of followers the user has |
   | followingCount | Number   | number of people he user is following |
   | topSongs       | Array of Files | user's top 3 favorite songs |
   | genres         | JSON Object | includes all genres the user listens to, along with their top 3 songs in each genre |
   | followers      | Array of Pointers to Users | followers |
   | following      | Array of Pointers to Users | following |
   | bio            | String | user's bio |
   | dislikedUsers  | Array of Pointers to Users | list of users that logged in user swiped left on |

#### Playlist

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user playlist (default field) |
   | user          | Pointer to User | user that created the playlist |
   | image         | String     | url to playlist image |
   | id            | String    | Spotify id for playlist |
   | name          | String | the name of the playlist |
   | public        | Boolean | whether the playlist is publically accessible |
   | description   | String | A description of the playlist set by user |
   | genres        | Array of Strings   | all the genres on the playlist |
   
#### Track

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user song (default field) |
   | playlist      | Pointer to Playlist | playlist on which the song is saved |
   | image         | String     | url to album cover of song |
   | id            | String    | Spotify id for song |
   | name          | String | the name of the song |
   | artist        | String | the song's artist |

### Networking
#### List of network requests by screen
   - Discover Screen
      - (Read/GET) Query a finite number of unfollowed users with shared genres
      - (Update/PUT) Update followers if a new user is liked
   - Profile Screen
      - (Read/GET) Query user's top 3 songs
      - (Read/GET) Query user's bio
   - Profile Screen (Playlist section)
      - (Read/GET) Query all playlists with this user as an author
      - (Delete) Delete existing playlist (if logged in user owns playlist)
   - Profile Screen (Followers Section)
      - (Read/GET) Query all followers of logged in user
      - (Update/PUT) Follow a follower
      - (Delete) Block a follower
   - Profile Screen (Following Section)
      - (Read/GET) Query all other users followed by logged in user
      - (Delete) Unfollow
   - Create New Playlist Screen
      - (Create/POST) Create new playlist
   - Individual Playlist Screen
      - (Create/POST) Add a new song to this playlist if owned by logged in user
      - (Read/GET) Get list of songs on this playlist
      - (Create/POST) Add song from this playlist to another playlist owned by logged in user
   - Edit Profile Screen
      - (Update/PUT) Change top 3 favorite songs, bio, favorite genres, or username
 
