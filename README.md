The main method in the class: will get a # number of msgs from twitter public api and get the statistics.

TODO: Make the process work with threads:
  1. One Thread process msg and store them in memory.
  1. Second Thread, read memory msgs, mark as ready those already processed and fill the statistics.
