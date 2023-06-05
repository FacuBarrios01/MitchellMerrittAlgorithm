# RaymondsAlgorithm

Author: Facundo Agustin Rodriguez

<img src="https://avatars.githubusercontent.com/u/61329804?s=400&u=ab90b2b6c5a46caa07206e7f218613ebad14ba23&v=4" alt="Profile picture" style="width:100px; height:100px">

### Recomendation
I recomend using a .md previewer,
In vscode you can use the integrated previewer with the key combination (Ctrl + Shft + V)

## Dataset
First line must contain "Number of processes:" immediately followed by a integer value ex."Number of processes:7".
Starting from the next line we describe our graph as a Adjacency Matrix
Afther describing the initial state of the graph, we will introduce new edges to our graph representing dependencies.
We will add them with the following format: $DEPENDING_PROCESS - $BLOCKER_PROCESS
The first value must be the depending process id, followed by a '-' and the id of the blocker process. Ej.: 2 - 3
Ex. "Process 2 is waiting for a resource blocked by process 3".
Process id MUST NOT exceed the value of the number of processes. See example dataset in the documentation.

## Code coments
For a better understanding of the code I recomend start reviewing it starting from the main method inside the Node.java file