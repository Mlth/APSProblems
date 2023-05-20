from sys import stdin
from collections import defaultdict
import math

p = 10**9+9
a = 31
def doHashing(line, repLength):
    hashMap = defaultdict(int)
    longestRepetition = 0
    hash = 0
    multiplier = 1 

    for i in range(repLength):
        hash = (hash * a + ord(line[i])) % p
        multiplier = (multiplier * a) % p
    
    hashMap[hash] += 1
    longestRepetition += 1

    for i in range(0, len(line) - repLength):
        hash = (hash * a - (ord(line[i]) * multiplier) % p) % p
        hash = (hash + ord(line[i + repLength])) % p
        hashMap[hash] += 1
        hashRepitions = hashMap.get(hash)
        if(hashRepitions > longestRepetition):
            longestRepetition = hashRepitions

    return longestRepetition

for line in stdin:
    if(len(line) == 0):
        break
    for i in range(1, len(line)):
        longestRepetition = doHashing(line.replace(" ", ""), i)
        if(longestRepetition > 1):
            print(longestRepetition)
        else:
            print()
            break