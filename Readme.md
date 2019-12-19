# SCC210 Game

## Building, Running, and Testing

Use `./gradlew build` to build the game

Use `./gradlew run` to build the game and run it

Use `./gradlew test` to run test cases

## Adding test cases

To add a test case, create a class in `src/test/java/scc210game/`

Import `org.junit.Test` and add `@Test` above any methods of that
class that are test cases.

Look at the existing test cases for examples.

## Adding features

1. Create a new branch based off master

1.1. `git checkout master` (if not already on master)

1.2. `git checkout -b do-my-thing`

2. Commit features to branch

3. At any time, push branch to remote

4. When pushed to remote, if not complete: create a merge request like: `WIP: Do
   my thing`
   
5. Otherwise, if the changes complete, create the merge request with the title
   `Do my thing`, and add everyone as an assignee and add the tag
   `waiting-on-review`
