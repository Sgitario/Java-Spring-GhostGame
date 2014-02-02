Java-Spring-GhostGame
=====================

Ghost game version in Spring MVC named Fantasmico.

In the game of Ghost, two players take turns building up an English word from left to right. 
Each player adds one letter per turn. The goal is to not complete the spelling of a word: if you 
add a letter that completes a word (of 4+ letters), or if you add a letter that produces a string 
that cannot be extended into a word, you lose.

The architecture of this solution is composed by Presentation, Solution, Infrastructure layers and a transversal 
one with utilities and entities. 

The frameworks that have been used are: Spring MVC, perf4j, junit, jQuery and Knockout.
