# BlacklaneChallenge

## -- STEP 1 --
Given the following REST API, develop a simple application that fetches the
posts from the API and displays them in a list.
The API is here: https://jsonplaceholder.typicode.com/posts
You can use any design spec that you like. To make things more challenging, do
not use any data-binding or view-model libraries. You can still use data biding and
MVVM if you like, but no libraries are allowed for this. Kotlin is preferred over Java.
### Base requirements:
1) User interface is built using native Android SDK
2) You should include pull-to-refresh functionality on the list
3) App should be performant and not cause ANRs
4) Each item in the list should have 'title' and 'body' properties shown (no IDs)
5) Proper separation of concerns and architecture approach are important
6) App should handle errors, i.e. being offline
7) Business logic should be covered by unit tests
### Optional:
1) UI tests using Espresso
## -- STEP 2 --
After committing the first part (and this is important because we want to see
git history), continue building on that base.
### Base requirements:
1) List can be filtered by typing text into a text box - filter by title using contains()
2) When a list item is clicked, the app displays a dialog showing the post ID