The main method in the class TwitterAPIExample: will receive a # number of msgs from twitter public api and get the statistics, default is 100 if not parameters entered.

TODO: Make the process work with threads:
  1. One Thread process msg and store them in memory.
  2. Second Thread, read memory msgs, mark as ready those already processed and fill the statistics.
  3. Add extra statistics.
